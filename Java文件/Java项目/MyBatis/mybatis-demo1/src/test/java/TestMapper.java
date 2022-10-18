import com.mybatis.mapper.DeptMapper;
import com.mybatis.mapper.EmpMapper;
import com.mybatis.pojo.Dept;
import com.mybatis.pojo.Emp;
import com.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class TestMapper {

    @Test
    public void test(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();

        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        List<Emp> allEmp = mapper.getAllEmp();

        allEmp.forEach(emp -> System.out.println(emp));

        System.out.println( mapper.getEmpAndDept(8));
    }

    @Test
    public void test1(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();

        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);

        List<Emp> allEmp = mapper.getAllEmp();

        allEmp.forEach(emp -> System.out.println(emp));

        System.out.println( mapper.getAllDeptAndEmp(1));
    }

    @Test
    public void test2(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSessionUtils();
        DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);

        Dept dept = deptMapper.getDeptAndEmpByStepOne(1);

        System.out.println(dept);
    }
}
