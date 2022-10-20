package com.home.eschool.models.payload;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ClassStudentsPayload {

    private final UUID id;
    private final String name;
    private final BigDecimal monthlyPayment;

    public ClassStudentsPayload(UUID id, String name, BigDecimal monthlyPayment) {
        this.id = id;
        this.name = name;
        this.monthlyPayment = monthlyPayment;
    }
}
