package com.doghome.easybuy.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.doghome.easybuy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * 这里是生成Token的工具类
 */
public class JwtUtil {


    // 密钥
    public static final String SECRET = "ff82854752c9d95afb57290fd7b9025af9ff3ee416c889c3870beacc290ca249";

    //间隔时间 单位分钟
    public static final int INTERVAL = 30;

    //生成token
    public static String createToken(String loginName,int Type) {
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + INTERVAL * 60 * 1000);


        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(startTime)
                .withExpiresAt(endTime)
                .withClaim("loginName", loginName)
                .withClaim("Type",Type)
                .sign(Algorithm.HMAC256(SECRET));
    }

    //根据token验证是否有效
    public static DecodedJWT verifyToken(String token) {
        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        return decodedJwt;
    }



    //读取jwt中存储数据
    public static String getLoginName(String token) {
        return verifyToken(token).getClaim("loginName").asString();
    }


    public static int getType(String token){
        return verifyToken(token).getClaim("Type").asInt();
    }

    //获取到期时间
    public static Date getExpires(String token) {
        return verifyToken(token).getExpiresAt();
    }
}
