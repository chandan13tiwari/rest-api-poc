package com.poc.rest_api_poc.service;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.model.EmployeeStatus;
import com.poc.rest_api_poc.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Employee> employees = new ArrayList<>();

        switch (lhs.toLowerCase()) {
            case "empid" -> {
                if(filteredEmployees.isEmpty()) {
                    return List.of(employeeService.findEmployeeById(Integer.parseInt(rhs)));
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getId() == Integer.parseInt(rhs)) {
                        employees.add(emp);
                    }
                });
            }

            case "empname" -> {
                if(filteredEmployees.isEmpty()) {
                    return employeeRepository.findEmployeeByName(rhs);
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getName().equals(rhs)) {
                        employees.add(emp);
                    }
                });
            }

            case "empemail" -> {
                if(filteredEmployees.isEmpty()) {
                    return employeeRepository.findEmployeeByEmail(rhs);
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getEmail().equals(rhs)) {
                        employees.add(emp);
                    }
                });
            }

            case "empphone" -> {
                if(filteredEmployees.isEmpty()) {
                   return employeeRepository.findEmployeeByPhone(rhs);
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getPhone().equals(rhs)) {
                        employees.add(emp);
                    }
                });
            }

            case "empaddress" -> {
                if(filteredEmployees.isEmpty()) {
                    return employeeRepository.findEmployeeByAddress(rhs);
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getAddress().equals(rhs)) {
                        employees.add(emp);
                    }
                });
            }

            case "empstatus" -> {
                if(filteredEmployees.isEmpty()) {
                    return employeeService.fetchEmployeeByStatus(EmployeeStatus.valueOf(rhs.toUpperCase()));
                }

                filteredEmployees.forEach(emp -> {
                    if(emp.getStatus().equals(EmployeeStatus.valueOf(rhs.toUpperCase()))) {
                        employees.add(emp);
                    }
                });
            }

            default -> throw new IllegalArgumentException("Unknown filter field: " + lhs);
        }

        return employees;
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
                return employeeRepository.findEmployeesByNameStartingWith(rhs);

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
