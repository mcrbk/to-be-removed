package com.money.model;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;

@AutoValue
public abstract class Account {

    public static Account from(String number, BigDecimal balance) {
        if (number == null || balance == null) {
            throw new NullPointerException("Invalid value null: " + number + ", " + balance);
        }

        if (number.isBlank()) {
            throw new IllegalArgumentException("Invalid account number: " + number);
        }

        if (balance.signum() < 0) {
            throw new IllegalArgumentException("Invalid balance: " + balance);
        }

        return new AutoValue_Account(number, balance);
    }

    public abstract String number();

    public abstract BigDecimal balance();
}
