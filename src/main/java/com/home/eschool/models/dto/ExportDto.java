package com.home.eschool.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportDto {

    @Schema(required = true, example = "teachers", description = "Shu 4 ta obyektdan bittasini tanlash kere" +
            "teachers;students;subjects;classes")
    private String object;
}
