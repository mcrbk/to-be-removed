package com.money.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BalanceUpdateTest {

    private static final String NUMBER = "number";
    private static final String BLANK = " ";

    private static final BigDecimal NEW_BALANCE = new BigDecimal("0");
    private static final BigDecimal OLD_BALANCE = new BigDecimal("0");
    private static final BigDecimal INVALID_BALANCE = new BigDecimal("-1");

    @Test
    public void testBuild() {
        BalanceUpdate.builder()
                .setNumber(NUMBER)
                .setNewBalance(NEW_BALANCE)
                .setOldBalance(OLD_BALANCE)
                .build();
    }

    @Test
    public void throwsExceptionWhenNumberIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(null)
                    .setNewBalance(NEW_BALANCE)
                    .setOldBalance(OLD_BALANCE)
                    .build();
        });
    }

    @Test
    public void throwsExceptionWhenNumberIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(BLANK)
                    .setNewBalance(NEW_BALANCE)
                    .setOldBalance(OLD_BALANCE)
                    .build();
        });
    }

    @Test
    public void throwsNullPointerExceptionWhenNewBalanceIsInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(BLANK)
                    .setNewBalance(INVALID_BALANCE)
                    .setOldBalance(OLD_BALANCE)
                    .build();
        });
    }

    @Test
    public void throwsIllegalStateExceptionWhenNewBalanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(BLANK)
                    .setNewBalance(null)
                    .setOldBalance(OLD_BALANCE)
                    .build();
        });
    }

    @Test
    public void throwsNullPointerExceptionWhenOldBalanceIsInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(BLANK)
                    .setNewBalance(NEW_BALANCE)
                    .setOldBalance(INVALID_BALANCE)
                    .build();
        });
    }

    @Test
    public void throwsIllegalStateExceptionWhenOldBalanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            BalanceUpdate.builder()
                    .setNumber(BLANK)
                    .setNewBalance(NEW_BALANCE)
                    .setOldBalance(null)
                    .build();
        });
    }
}
