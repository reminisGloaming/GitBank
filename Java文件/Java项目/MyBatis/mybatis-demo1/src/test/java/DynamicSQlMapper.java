import com.mybatis.mapper.DynamicSQLMapper;
import com.mybatis.pojo.Emp;
import com.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicSQlMapper {

    @Test
    public void test(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();
        DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);

        List<Emp> empByCondition = mapper.getEmpByCondition(new Emp(null, "", 24, "", ""));

        System.out.println(empByCondition);


    }

    @Test
    public void test2(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();
        DynamicSQLMapper dynamicSQLMapper = sqlSession.getMapper(DynamicSQLMapper.class);

        List<Emp> list = dynamicSQLMapper.getEmpByCondition(new Emp(null, "张三", 24, "男", "296136161@qq.com"));
        System.out.println(list);
    }

    @Test
    public void testInsert(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();
        DynamicSQLMapper dynamicSQLMapper = sqlSession.getMapper(DynamicSQLMapper.class);


        Emp emp1 = new Emp(null,"只因哥",23,"男","123@qq.com");
        Emp emp2 = new Emp(null,"蔡徐坤",23,"男","123@qq.com");
        Emp emp3 = new Emp(null,"得坤吧",23,"男","123@qq.com");
        Emp emp4 = new Emp(null,"掉毛",23,"男","123@qq.com");

        List<Emp> emps = Arrays.asList(emp1, emp2, emp3, emp4);

        System.out.println(dynamicSQLMapper.insertMoreByList(emps));
    }
}
