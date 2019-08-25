package com.money.model;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;

@AutoValue
public abstract class Account {

    public static Account from(String number, BigDecimal balance) {
        if (balance.signum() < 1) {
            throw new IllegalArgumentException("Validation failed: balance less than or equal to 0");
        }
        return new AutoValue_Account(number, balance);
    }

    public abstract String number();

    public abstract BigDecimal balance();
}
