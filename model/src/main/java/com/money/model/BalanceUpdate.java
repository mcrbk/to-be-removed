package com.money.model;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;
import java.util.StringJoiner;

@AutoValue
public abstract class BalanceUpdate {

    public static BalanceUpdate.Builder builder() {
        return new AutoValue_BalanceUpdate.Builder();
    }

    public abstract String number();

    public abstract BigDecimal newBalance();

    public abstract BigDecimal oldBalance();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract BalanceUpdate.Builder setNumber(String number);

        public abstract BalanceUpdate.Builder setNewBalance(BigDecimal newBalance);

        public abstract BalanceUpdate.Builder setOldBalance(BigDecimal oldBalance);

        public BalanceUpdate build() {
            BalanceUpdate balanceUpdate = autoBuild();

            StringJoiner illegalArguments = new StringJoiner(", ");

            if (balanceUpdate.number().isBlank()) {
                illegalArguments.add("number cannot be blank");
            }

            if (balanceUpdate.newBalance().signum() < 0) {
                illegalArguments.add("newBalance cannot be less than zero");
            }

            if (balanceUpdate.oldBalance().signum() < 0) {
                illegalArguments.add("oldBalance cannot be less than zero");
            }

            String validationErrors = illegalArguments.toString();

            if (!validationErrors.isEmpty()) {
                throw new IllegalArgumentException("Validation failed for one or more arguments: " + validationErrors);
            }

            return balanceUpdate;
        }

        abstract BalanceUpdate autoBuild();
    }
}
