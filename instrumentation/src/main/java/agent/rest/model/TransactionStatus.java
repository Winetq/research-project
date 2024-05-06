package agent.rest.model;

public enum TransactionStatus {
    COMMIT,
    ROLLBACK,
    DEFAULT;

    public static TransactionStatus of(String status) {
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            if (status.equals(transactionStatus.name())) {
                return transactionStatus;
            }
        }

        return DEFAULT;
    }
}
