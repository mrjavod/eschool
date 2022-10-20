package com.home.eschool.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsDto {

    @Schema(required = true)
    private BigDecimal paymentAmount;

    @Schema(required = true, description = "Naqt 0, Plastik 1")
    private int paymentType;

    @NotNull
    private String paymentPurpose;

    @NotNull
    private UUID studentId;

}
