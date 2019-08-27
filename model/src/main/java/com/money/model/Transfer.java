package com.money.model;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;
import java.util.StringJoiner;

@AutoValue
public abstract class Transfer {

    public static Builder builder() {
        return new AutoValue_Transfer.Builder();
    }

    public abstract String from();

    public abstract String to();

    public abstract BigDecimal amount();

    public abstract TransferStatus status();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setFrom(String from);

        public abstract Builder setTo(String to);

        public abstract Builder setAmount(BigDecimal amount);

        public abstract Builder setStatus(TransferStatus status);

        public Transfer build() {
            Transfer transfer = autoBuild();

            StringJoiner illegalArguments = new StringJoiner(", ");

            if (transfer.from().isBlank()) {
                illegalArguments.add("from cannot be blank");
            }

            if (transfer.to().isBlank()) {
                illegalArguments.add("to cannot be blank");
            }

            if (transfer.amount().signum() < 1) {
                illegalArguments.add("amount less than or equal to 0");
            }

            String validationErrors = illegalArguments.toString();

            if (!validationErrors.isEmpty()) {
                throw new IllegalArgumentException("Validation failed for one or more arguments: " + validationErrors);
            }

            return transfer;
        }

        abstract Transfer autoBuild();
    }
}
