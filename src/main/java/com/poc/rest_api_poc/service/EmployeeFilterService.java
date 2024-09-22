package com.poc.rest_api_poc.service;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.model.EmployeeStatus;
import com.poc.rest_api_poc.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeFilterService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeFilterService.class);

    public EmployeeFilterService(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    public List<Employee> handleEq(String lhs, String rhs, List<Employee> filteredEmployees) {
        LOG.info("handleEq :: lhs: {} and rhs: {}", lhs, rhs);

        if (filteredEmployees.isEmpty()) {
            return getEmployeesFromRepository(lhs, rhs);
        }

        return filterEmployees(lhs, rhs, filteredEmployees);
    }

    private List<Employee> getEmployeesFromRepository(String lhs, String rhs) {
        return switch (lhs.toLowerCase()) {
            case "empid" -> List.of(employeeService.findEmployeeById(Integer.parseInt(rhs)));
            case "empname" -> employeeRepository.findEmployeeByName(rhs);
            case "empemail" -> employeeRepository.findEmployeeByEmail(rhs);
            case "empphone" -> employeeRepository.findEmployeeByPhone(rhs);
            case "empaddress" -> employeeRepository.findEmployeeByAddress(rhs);
            case "empstatus" -> employeeService.fetchEmployeeByStatus(EmployeeStatus.valueOf(rhs.toUpperCase()));
            default -> throw new IllegalArgumentException("Unknown filter field: " + lhs);
        };
    }

    private List<Employee> filterEmployees(String lhs, String rhs, List<Employee> filteredEmployees) {
        return filteredEmployees.stream()
                .filter(emp -> matches(emp, lhs, rhs))
                .toList();
    }

    private boolean matches(Employee emp, String lhs, String rhs) {
        return switch (lhs.toLowerCase()) {
            case "empid" -> emp.getId() == Integer.parseInt(rhs);
            case "empname" -> emp.getName().equals(rhs);
            case "empemail" -> emp.getEmail().equals(rhs);
            case "empphone" -> emp.getPhone().equals(rhs);
            case "empaddress" -> emp.getAddress().equals(rhs);
            case "empstatus" -> emp.getStatus().equals(EmployeeStatus.valueOf(rhs.toUpperCase()));
            default -> false;
        };
    }


    public List<Employee> handleSw(String lhs, String rhs, List<Employee> filteredEmployees) {
        LOG.info("handleSw :: lhs: {} and rhs: {}", lhs, rhs);
        List<Employee> employees = new ArrayList<>();

        if (lhs.equalsIgnoreCase("empname")) {
            if(filteredEmployees.isEmpty())
                return employeeRepository.findEmployeesByNameStartingWith(rhs);

            filteredEmployees.forEach(emp -> {
                if(emp.getName().startsWith(rhs)) {
                    employees.add(emp);
                }
            });

            return employees;
        }
        throw new IllegalArgumentException("Unknown filter field: " + lhs);
    }

    public List<Employee> handleEw(String lhs, String rhs, List<Employee> filteredEmployees) {
        LOG.info("handleEw :: lhs: {} and rhs: {}", lhs, rhs);
        List<Employee> employees = new ArrayList<>();

        if (lhs.equalsIgnoreCase("empname")) {
            if(filteredEmployees.isEmpty())
                return employeeRepository.findEmployeesByNameEndingWith(rhs);

            filteredEmployees.forEach(emp -> {
                if(emp.getName().endsWith(rhs)) {
                    employees.add(emp);
                }
            });

            return employees;
        }
        throw new IllegalArgumentException("Unknown filter field: " + lhs);
    }

    public List<Employee> handleContains(String lhs, String rhs, List<Employee> filteredEmployees) {
        LOG.info("handleSw :: lhs: {} and rhs: {}", lhs, rhs);
        List<Employee> employees = new ArrayList<>();

        if (lhs.equalsIgnoreCase("empaddress")) {
            if(filteredEmployees.isEmpty())
                return employeeRepository.findEmployeesByNameContains(rhs);

            filteredEmployees.forEach(emp -> {
                if(emp.getAddress().contains(rhs)) {
                    employees.add(emp);
                }
            });

            return employees;
        }
        throw new IllegalArgumentException("Unknown filter field: " + lhs);
    }
}
