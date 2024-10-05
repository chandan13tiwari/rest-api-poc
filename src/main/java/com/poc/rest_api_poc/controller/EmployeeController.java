package com.poc.rest_api_poc.controller;

import com.poc.rest_api_poc.entity.Employee;
import com.poc.rest_api_poc.exception.EmployeeException;
import com.poc.rest_api_poc.exception.EmployeeNotFoundException;
import com.poc.rest_api_poc.model.FilterDelimeter;
import com.poc.rest_api_poc.model.EmployeeStatus;
import com.poc.rest_api_poc.service.EmployeeFilterService;
import com.poc.rest_api_poc.service.EmployeeService;
import com.poc.rest_api_poc.utils.AppConstants;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeFilterService employeeFilterService;
    public static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService ,EmployeeFilterService employeeFilterService) {
        this.employeeService = employeeService;
        this.employeeFilterService = employeeFilterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity fetchEmployeeById(@PathVariable("id") int empId) {
        try {
            //return ResponseEntity.ok(employeeService.findEmployeeById(empId));
            return ResponseEntity.ok(employeeService.findEmployeeByIdTestExceptionHandler(empId));
        } catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException(ex.getMessage());
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }
            else if(ex.getHttpStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> fetchAllEmployee(@RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(employeeService.findAllEmployee(page, size));
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/all")
    public ResponseEntity<PagedModel> fetchAllEmployeeHATEOAS(@RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                                              Pageable pageable, PagedResourcesAssembler assembler) {
        try {
            Page<Employee> employeePage = employeeService.findAllEmployeeHATEOAS(pageable);
            return ResponseEntity.ok(assembler.toModel(employeePage));
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

    @GetMapping("/filter")
    public ResponseEntity<List<Employee>> getFilteredEmployee(@RequestParam("q") List<String> queries) {
        LOG.info("Query received: {}", queries);
        List<Employee> filteredEmployees = new ArrayList<>();
        // splitting filters
        try {
            for(String filter : queries) {
                LOG.info("Processing filter: {}", filter);
                String[] filterSplit = filter.split(AppConstants.SPACE);

                switch (FilterDelimeter.convertToFilterDelimeter(filterSplit[1])) {
                    case EQ -> {
                        LOG.info("delimeter is eq");
                        filteredEmployees = employeeFilterService.handleEq(filterSplit[0], filterSplit[2], filteredEmployees);
                    }

                    case EW -> {
                        LOG.info("delimeter is ew");
                        filteredEmployees = employeeFilterService.handleEw(filterSplit[0], filterSplit[2], filteredEmployees);
                    }

                    case SW -> {
                        LOG.info("delimeter is sw");
                        filteredEmployees = employeeFilterService.handleSw(filterSplit[0], filterSplit[2], filteredEmployees);
                    }

                    case CONTAINS -> {
                        LOG.info("delimeter is contains");
                        filteredEmployees = employeeFilterService.handleContains(filterSplit[0], filterSplit[2], filteredEmployees);
                    }

                    default -> throw new IllegalArgumentException("Unknown query delimeter: " + filterSplit[1]);
                }
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.internalServerError().build();
        } catch (EmployeeException ex) {
            if(ex.getHttpStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(filteredEmployees);
    }
}
