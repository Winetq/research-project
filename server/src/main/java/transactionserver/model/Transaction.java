package transactionserver.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
public class Transaction {
    private List<String> transactionQueries;
    private List<Long> times;

    public Long getAverageTime() {
        return (long) Math.round(times.stream().reduce(0L, Long::sum).floatValue() / times.size());
    }
}
