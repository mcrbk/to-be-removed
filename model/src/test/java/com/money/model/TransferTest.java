package com.money.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TransferTest {

    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String BLANK = " ";

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);

    @Test
    public void testBuild() {
        Transfer.builder()
                .setFrom(FROM)
                .setTo(TO)
                .setAmount(AMOUNT)
                .build();
    }

    @Test
    public void throwsExceptionWhenFromIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Transfer.builder()
                    .setFrom(BLANK)
                    .setTo(TO)
                    .setAmount(AMOUNT)
                    .build();
        });
    }

    @Test
    public void throwsExceptionWhenToIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Transfer.builder()
                    .setFrom(TO)
                    .setTo(BLANK)
                    .setAmount(AMOUNT)
                    .build();
        });
    }

    @Test
    public void throwsExceptionWhenLessThanZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Transfer.builder()
                    .setFrom(FROM)
                    .setTo(TO)
                    .setAmount(BigDecimal.valueOf(-100))
                    .build();
        });
    }

    @Test
    public void throwsExceptionWhenEqualToZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Transfer.builder()
                    .setFrom(FROM)
                    .setTo(TO)
                    .setAmount(BigDecimal.valueOf(0))
                    .build();
        });
    }
}
