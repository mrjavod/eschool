package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsStatsPayload {

    private StudyYearsPayload studyYear;
    private List<MonthlyPayments> monthlyPayments;
}
