package agent.rest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Transaction {
    private final List<String> originalQueries;
    @EqualsAndHashCode.Exclude private final Long time;
    private final TransactionStatus status;
}
