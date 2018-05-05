package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);
    Employee findTopByNameContains(String character);
    Employee findTopByCompanyIdOrderBySalaryDesc(Integer companyId);
    List<Employee> findBySalaryBetween(Integer low, Integer high);
}
