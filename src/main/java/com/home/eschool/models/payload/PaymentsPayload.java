package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsPayload {

    private UUID id;

    private StudentsPayload student;

    private BigDecimal paymentAmount;

    private String paymentDate;
}
