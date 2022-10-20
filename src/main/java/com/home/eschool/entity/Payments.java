package com.home.eschool.entity;

import com.home.eschool.entity.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payments extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Students students;

    private BigDecimal paymentAmount;

    @Enumerated
    private PaymentTypeEnum paymentType;

    @Column(columnDefinition = "text")
    private String paymentPurpose;

    private Timestamp paymentDate;

    private UUID studyYearId;

    @OneToOne
    private States state;
}
