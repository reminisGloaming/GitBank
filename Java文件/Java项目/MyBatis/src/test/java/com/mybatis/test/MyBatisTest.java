package com.mybatis.test;

//import org.apache.ibatis.io.Resources;
import com.mybatis.mapper.UserMapper;
import com.mybatis.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest {

    @Test
    public void testMyBatis() throws IOException {
        //加载核心配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

        SqlSessionFactory sqlSessionFactory = builder.build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        int result = userMapper.insertUser();

//        sqlSession.commit();

        System.out.println(result);

    }


    @Test
    public void testUpdate() throws IOException {
        InputStream inputStream_inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream_inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

//        userMapper.updateUser();
//        System.out.println("success");

//        User userById = userMapper.getUserById();
//        System.out.println(userById);

        List<User> allUser = userMapper.getAllUser();
        allUser.forEach(user -> System.out.println(user));

    }
}
