package agent.rest.model;

public enum QueryStatus {
    COMMIT,
    ROLLBACK,
    DEFAULT;

    public static QueryStatus of(String status) {
        for (QueryStatus queryStatus : QueryStatus.values()) {
            if (status.equals(queryStatus.name())) {
                return queryStatus;
            }
        }

        return DEFAULT;
    }
}
