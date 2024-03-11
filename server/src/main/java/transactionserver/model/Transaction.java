package transactionserver.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Transaction {
    private List<String> originalQueries;
    private Long time;
    private String status;
}
