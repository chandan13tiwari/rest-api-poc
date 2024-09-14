package com.poc.rest_api_poc.controller;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.exception.EmployeeException;
import com.poc.rest_api_poc.model.EmployeeStatus;
import com.poc.rest_api_poc.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> fetchEmployeeById(@PathVariable("id") int empId) {
        try {
            return ResponseEntity.ok(employeeService.findEmployeeById(empId));
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> fetchAllEmployee() {
        try {
            return ResponseEntity.ok(employeeService.findAllEmployee());
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Employee> patchEmployee(@PathVariable("id") int empId, @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok(employeeService.updateEmployeeDetails(empId, employee));
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") int empId) {
        try {
            employeeService.deleteEmployee(empId);
            return ResponseEntity.ok().build();
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> fetchEmployeeByStatus(@PathVariable("status") EmployeeStatus empStatus) {
        return ResponseEntity.ok(employeeService.fetchEmployeeByStatus(empStatus));
    }


}
