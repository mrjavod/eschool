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
public class PaymentsPayloadDetails {

    private UUID id;

    private ClassStudentsPayload student;

    private ClassesPayload studentClass;

    private BigDecimal paymentAmount;

    private String paymentDate;

    private String paymentPurpose;

    private int paymentType;

}
