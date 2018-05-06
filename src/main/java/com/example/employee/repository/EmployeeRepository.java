package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);
    Employee findTopByNameContains(String character);
    Employee findTopByCompanyIdOrderBySalaryDesc(Integer companyId);
    List<Employee> findBySalaryBetween(Integer low, Integer high);

    @Query(value = "SELECT e from Employee e where name like %:name%")
    Employee findEmployee(@Param("name") String name);
}
