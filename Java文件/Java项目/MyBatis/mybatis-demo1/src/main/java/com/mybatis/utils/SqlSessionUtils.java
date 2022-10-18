package com.mybatis.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtils {

    public static SqlSession getSqlSessionUtils(){

        SqlSession sqlSession =null;
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
            sqlSession = sqlSessionFactory.openSession(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession;
    }
}
