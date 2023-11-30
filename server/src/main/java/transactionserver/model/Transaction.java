package transactionserver.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
public class Transaction {
    private String transactionQueries;
    private List<Long> times;
}
