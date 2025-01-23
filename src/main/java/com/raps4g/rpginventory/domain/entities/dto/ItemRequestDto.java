package com.raps4g.rpginventory.domain.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {
    private Long id;
    @NotNull(message = "'name' field is required.")
    private String name;
    @NotNull(message = "'description' field is required.")
    private String description;
    @NotNull(message = "'itemCategoryId' field is required.")
    private Long itemCategoryId;
    @NotNull(message = "'itemRarityId' field is required.")
    private Long itemRarityId;
    @NotNull(message = "'validSlotId' field is required.")
    private Long validSlotId;
    @NotNull(message = "'value' field is required.")
    private Integer value;
}
