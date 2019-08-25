package com.money.model;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;

@AutoValue
public abstract class BalanceUpdate {

    public static BalanceUpdate of(Account account, BigDecimal newBalance, BigDecimal oldBalance) {
        if (newBalance.signum() < 1 || oldBalance.signum() < 1) {
            throw new IllegalArgumentException("Validation failed: balance less than or equal to 0");
        }
        return new AutoValue_BalanceUpdate(account, newBalance, oldBalance);
    }

    public abstract Account account();

    public abstract BigDecimal newBalance();

    public abstract BigDecimal oldBalance();
}
