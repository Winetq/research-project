package transactionserver.model;

import lombok.Getter;

@Getter
public class Transaction {
    private String originalQuery;
    private Long time;
    private String status;
}
