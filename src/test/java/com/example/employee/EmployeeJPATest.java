package com.example.employee;

import com.example.employee.entity.Company;
import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class EmployeeJPATest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Before
    public void setUp() throws Exception {
        //本地启动mysql，创建employee_db数据库
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost:3306/employee_db","root","root");
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void should_return_employee_when_input_employee_name() throws Exception {
        //1.查询名字是小红的employee
        Employee expectedEmployee = new Employee(1, "xiaohong",19,"female",7000,1);
        Employee result = employeeRepository.findByName("xiaohong");
        String actualName = result.getName();
        assertThat(actualName).isEqualTo(expectedEmployee.getName());
        assertThat(result.getAge()).isEqualTo(expectedEmployee.getAge());

    }

    @Test
    public void should_return_employee_given_character_in_name_and_salary_large_than() throws Exception {
        //2.找出Employee表中第一个姓名包含`n`字符的雇员所有个人信息
        Employee expectedEmployee = new Employee(0,"xiaoming",20,"female",7000,1);
        Employee employee = employeeRepository.findTopByNameContains("n");
        String actualName = employee.getName();
        assertThat(employee.getAge()).isEqualTo(expectedEmployee.getAge());
        assertThat(actualName).isEqualTo(expectedEmployee.getName());
    }

    @Test
    public void should_find_employees_salary_between_6000_to_7000() {
        //找到工资在6000-7000范围内的employee
        int employeeSize = 3;
        List<Employee> employees = employeeRepository.findBySalaryBetween(6000, 7000);
        assertThat(employees.size()).isEqualTo(employeeSize);
    }


    @Test
    public void should_return_employee_name_when_employee_salary_is_max_and_given_company_id_() throws Exception {
        //3.找出一个薪资最高且公司ID是1的雇员以及该雇员的name
        Employee expectedEmployee = new Employee(1, "xiaohong",19,"female",7000,1);
        Employee employee = employeeRepository.findTopByCompanyIdOrderBySalaryDesc(1);
        String actualName = employee.getName();
        assertThat(employee.getAge()).isEqualTo(expectedEmployee.getAge());
        assertThat(actualName).isEqualTo(expectedEmployee.getName());
    }

    @Test
    public void should_return_employee_list_when_input_page_request() throws Exception {
        //4.实现对Employee的分页查询，每页两条数据，一共三页数。
        //注意：PageRequest的构造方法已经弃用了代替的是PageRequest.of,并且最后一个参数代表按照table中的哪一个字段排序
        Pageable pageable = PageRequest.of(0, 2);
        Page<Employee> EmployeePage = employeeRepository.findAll(pageable);
        assertThat(EmployeePage.getTotalPages()).isEqualTo(3);
    }

    @Test
    public void should_return_company_name_when_input_employee_name() throws Exception {
        //5.查找xiaohong的所在的公司的公司名称
        String expectedCompanyName = "alibaba";
        Employee employee = employeeRepository.findByName("xiaohong");
        Company company = employee.getCompany();

        String actualCompanyName = company.getCompanyName();
        assertThat(actualCompanyName).isEqualTo(expectedCompanyName);
    }

    @Test
    public void should_find_employee_by_query() {
        //6.通过写JPQL查询名字含有"xiaozhi"的employee
        String expectedName = "xiaozhi";
        Employee employee = employeeRepository.findEmployee("zhi");
        assertThat(expectedName).isEqualTo(employee.getName());
    }

}
