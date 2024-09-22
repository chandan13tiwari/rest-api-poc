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

    @Query("SELECT emp FROM Employee emp WHERE emp.name = :empName")
    List<Employee> findEmployeeByName(@Param("empName") String employeeName);

    @Query("SELECT emp FROM Employee emp WHERE emp.email = :empEmail")
    List<Employee> findEmployeeByEmail(@Param("empEmail") String employeeEmail);

    @Query("SELECT emp FROM Employee emp WHERE emp.phone = :empPhone")
    List<Employee> findEmployeeByPhone(@Param("empPhone") String employeePhone);

    @Query("SELECT emp FROM Employee emp WHERE emp.address = :empAddress")
    List<Employee> findEmployeeByAddress(@Param("empAddress") String employeeAddress);

    @Query("SELECT emp FROM Employee emp WHERE emp.name LIKE CONCAT(:empName, '%')")
    List<Employee> findEmployeesByNameStartingWith(@Param("empName") String employeeName);

    @Query("SELECT emp FROM Employee emp WHERE emp.name LIKE CONCAT('%', :empName)")
    List<Employee> findEmployeesByNameEndingWith(@Param("empName") String employeeName);

    @Query("SELECT emp FROM Employee emp WHERE emp.name LIKE CONCAT('%', :empName, '%')")
    List<Employee> findEmployeesByNameContains(@Param("empName") String employeeName);

}
