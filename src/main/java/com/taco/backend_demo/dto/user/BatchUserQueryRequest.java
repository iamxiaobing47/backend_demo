package com.taco.backend_demo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchUserQueryRequest {
    @NotNull(message = "E014")
    private List<String> userIds;
}