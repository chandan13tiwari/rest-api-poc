package com.poc.rest_api_poc.repository;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.model.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT emp FROM Employee emp WHERE emp.status = :empStatus")
    List<Employee> findEmployeeByStatus(@Param("empStatus") EmployeeStatus employeeStatus);
}
