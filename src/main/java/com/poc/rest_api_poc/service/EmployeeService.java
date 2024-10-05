package com.poc.rest_api_poc.service;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.exception.EmployeeException;
import com.poc.rest_api_poc.exception.EmployeeNotFoundException;
import com.poc.rest_api_poc.model.EmployeeStatus;
import com.poc.rest_api_poc.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findEmployeeById(int empId) {
        LOG.info("Started looking for Employee with ID: {}", empId);

        Optional<Employee> employee = employeeRepository.findById(empId);
        if(employee.isEmpty()) {
            LOG.info("Employee with ID: {} not found!", empId);
            throw new EmployeeException("Employee not found", HttpStatus.NOT_FOUND);
        }

        LOG.info("Employee fetched for ID: {} :: {}", empId, employee.get());
        return employee.get();
    }

    public Employee findEmployeeByIdTestExceptionHandler(int empId) {
        LOG.info("Started looking for Employee with ID: {}", empId);

        Optional<Employee> employee = employeeRepository.findById(empId);
        if(employee.isEmpty()) {
            LOG.info("Employee with ID: {} not found!", empId);
            throw new EmployeeNotFoundException("Employee not found with id: " + empId);
        }

        LOG.info("Employee fetched for ID: {} :: {}", empId, employee.get());
        return employee.get();
    }

    public Page<Employee> findAllEmployee(int page, int size) {
        LOG.info("Started fetching all employees from DB");

        //Pageable pageable = PageRequest.of(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Employee> allEmployees = employeeRepository.findAll(pageable);
        if(allEmployees.isEmpty())
            LOG.info("No Employees added in DB");
        else
            LOG.info("Fetched all employees from DB");

        return allEmployees;
    }

    public Page<Employee> findAllEmployeeHATEOAS(Pageable pageable) {
        LOG.info("Started fetching all employees from DB");

        //Pageable pageable = PageRequest.of(page, size);
        Page<Employee> allEmployees = employeeRepository.findAll(pageable);
        if(allEmployees.isEmpty())
            LOG.info("No Employees added in DB");
        else
            LOG.info("Fetched all employees from DB");

        return allEmployees;
    }

    public Employee createEmployee(Employee employee) {
        LOG.info("Started creating new Employee with details: {}", employee);
        employee.setStatus(EmployeeStatus.ACTIVE);
        Employee emp = employeeRepository.save(employee);
        LOG.info("Employee created!");
        return emp;
    }

    public Employee updateEmployeeDetails(int empId, Employee updatedEmployee) {
        try {
            Employee existingEmployeeDetails = findEmployeeById(empId);
            LOG.info("Fetched employee with existing details for ID: {} :: {}", empId, existingEmployeeDetails);
            LOG.info("Updating employee with ID: {}", empId);

            Employee employee = Employee.builder()
                    .id(existingEmployeeDetails.getId())
                    .name(updatedEmployee.getName() == null || updatedEmployee.getName().isEmpty() ? existingEmployeeDetails.getName() : updatedEmployee.getName())
                    .email(updatedEmployee.getEmail() == null || updatedEmployee.getEmail().isEmpty() ? existingEmployeeDetails.getEmail() : updatedEmployee.getEmail())
                    .phone(updatedEmployee.getPhone() == null || updatedEmployee.getPhone().isEmpty() ? existingEmployeeDetails.getPhone() : updatedEmployee.getPhone())
                    .address(updatedEmployee.getAddress() == null || updatedEmployee.getAddress().isEmpty() ? existingEmployeeDetails.getAddress() : updatedEmployee.getAddress())
                    .status(updatedEmployee.getStatus() == null ? existingEmployeeDetails.getStatus() : updatedEmployee.getStatus())
                    .build();

            LOG.info("Employee with ID: {} updated successfully!", empId);
            LOG.info("updated employee details: {}", employee);

            return employeeRepository.save(employee);
        } catch (EmployeeException ex) {
            LOG.error("Unable to update Employee Details. Exception occurred while fetching employee!");
            LOG.error(ex.getMessage());
            throw new EmployeeException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteEmployee(int empId) {
        LOG.info("Looking for the Employee Details with ID: {} in DB", empId);
        try {
            Employee employee = findEmployeeById(empId);
            LOG.info("Employee found! Started deleting Employee: {}", employee);
            employeeRepository.deleteById(empId);
            LOG.info("Employee successfully deleted!");
        } catch (EmployeeException ex) {
            LOG.error("Unable to delete Employee Details. Exception occurred while fetching employee!");
            LOG.error(ex.getMessage());
            throw new EmployeeException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Employee> fetchEmployeeByStatus(EmployeeStatus employeeStatus) {
        LOG.info("Fetching all employees with status: {}", employeeStatus);
        List<Employee> allEmployees = employeeRepository.findEmployeeByStatus(employeeStatus);
        LOG.info("fetched all Employees with status: {} :: {}", employeeStatus, allEmployees);
        return allEmployees;
    }
}
