package com.poc.rest_api_poc.entity;

import com.poc.rest_api_poc.model.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "empId")
    private int id;

    @Column(name = "empName")
    private String name;

    @Column(name = "empEmail")
    private String email;

    @Column(name = "empPhone")
    private String phone;

    @Column(name = "empAddress")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "empStatus")
    private EmployeeStatus status;

    public static String filterFieldMapper(String filterField) {
        return switch (filterField.toLowerCase()) {
            case "empid" -> "id";
            case "empname" -> "name";
            case "empemail" -> "email";
            case "empphone" -> "phone";
            case "empaddress" -> "address";
            case "empstatus" -> "status";
            default -> throw new IllegalArgumentException("Unknown filter field: " + filterField);
        };
    }

}
