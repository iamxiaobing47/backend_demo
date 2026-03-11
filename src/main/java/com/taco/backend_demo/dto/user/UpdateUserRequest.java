package com.taco.backend_demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String position;
    private Long businessId; // for business_owner
    private Long locationId; // for employee
}