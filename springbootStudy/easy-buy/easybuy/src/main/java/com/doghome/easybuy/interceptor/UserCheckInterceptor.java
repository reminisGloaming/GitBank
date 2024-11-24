package com.doghome.easybuy.interceptor;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.doghome.easybuy.annotation.CheckPermission;
import com.doghome.easybuy.entity.User;
import com.doghome.easybuy.exception.TokenNotCorrectException;
import com.doghome.easybuy.service.UserService;
import com.doghome.easybuy.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 用户校验拦截器 (校验前端传来的Token)
 */
@Component
public class UserCheckInterceptor implements HandlerInterceptor {

//    public static final Logger log = LoggerFactory.getLogger(UserCheckInterceptor.class);

    @Autowired

    private StringRedisTemplate stringRedisTemplate;

    @Autowired

    private UserService userService;


    /**
     * 是否登录,是否是本人登录,登录是否超时
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
//        log.info("=====================================token:" + token);

        //验证是否登录
        if (token == null || token.equals("")) {

            // 没有登录
            throw new TokenNotCorrectException("no-token");

        }

        //验证token
        String loginName = "";
        int Type=0;
        try {

            //调用JWT工具类获取参数的方法
            loginName = JwtUtil.getLoginName(token);
            Type=JwtUtil.getType(token);
        } catch (SignatureVerificationException e) {

            //验证失败
            throw new TokenNotCorrectException("sign-error");
        } catch (TokenExpiredException e) {

            //操作超时
            throw new TokenNotCorrectException("token-expired");
        }


        //从redis里面拿到的Token不等于接收到的Token
        String serverToken = stringRedisTemplate.opsForValue().get(loginName);


        if (!token.equals(serverToken) || serverToken == null) {
            throw new TokenNotCorrectException("token-error");
        }


        //token延期
        Date exp = JwtUtil.getExpires(token);
        if (exp.getTime() - System.currentTimeMillis() <= 5 * 60 * 100) {
            //进行token置换
            String newToken = JwtUtil.createToken(loginName,Type);
            stringRedisTemplate.opsForValue().set(loginName, newToken, 30, TimeUnit.MINUTES);
            response.setHeader("Access-Control-Expose-Headers", "x-token");
            response.setHeader("x-token", newToken);
        }


        //验证权限
        boolean isHandlerMethod = handler instanceof HandlerMethod;
        if (!isHandlerMethod) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckPermission checkPermission = handlerMethod.getMethodAnnotation(CheckPermission.class);
        if (checkPermission == null) {
            return true;
        }

        //判断是否有权限
        String[] roles = checkPermission.value();
//        log.info("角色：" + Arrays.toString(roles));
        if (checkRole(loginName, roles)) {
            return true;
        } else {
            throw new TokenNotCorrectException("no-permission");
        }
    }


    /**
     * 拿出用户角色
     *
     * @param loginName
     * @param roles
     * @return
     */
    public boolean checkRole(String loginName, String[] roles) {
        User user = userService.selectUserByLoginName(loginName);
//        System.out.println(user.getType()); 这里会输出角色 为0
        for (String role : roles) {

            if (role.equals(String.valueOf(user.getType()))) {
                return true;
            }
        }
        return false;
    }
}
