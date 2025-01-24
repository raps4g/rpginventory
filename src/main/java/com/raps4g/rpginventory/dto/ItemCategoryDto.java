package com.raps4g.rpginventory.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCategoryDto {
    private Long id;
    @NotNull(message = "'name' field is required.")
    private String name;
}
