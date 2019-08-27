package com.money.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class AccountTest {

    private static final String VALID_ACCOUNT = "Account";

    private static final BigDecimal VALID_BALANCE = new BigDecimal("0");
    private static final BigDecimal INVALID_BALANCE = new BigDecimal("-1");

    @Test
    public void testBuild() {
        Account.from(VALID_ACCOUNT, new BigDecimal("123"));
    }

    @Test
    public void throwsNullPointerExceptionWhenAccountNumberIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Account.from(null, VALID_BALANCE));
    }

    @Test
    public void throwsNullPointerExceptionWhenBalanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Account.from(VALID_ACCOUNT, null));
    }

    @Test
    public void throwsIllegalArgumentExceptionWhenAccountNumberIsBlank() {
         Assertions.assertThrows(IllegalArgumentException.class, () -> Account.from("", VALID_BALANCE));
    }

    @Test
    public void throwsIllegalArgumentExceptionWhenBalanceIsLessThanOne() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Account.from(VALID_ACCOUNT, INVALID_BALANCE));
    }
}
