package com.home.eschool.models.payload;

import java.math.BigDecimal;

public interface MonthlyPayments {

    BigDecimal getSum();

    int getMonth();

    void setSum(BigDecimal sum);

    void setMonth(int month);
}
