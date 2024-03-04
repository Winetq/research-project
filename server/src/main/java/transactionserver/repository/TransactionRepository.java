package transactionserver.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import transactionserver.model.Transaction;

import java.util.List;
import java.util.Map;

@Repository
@Getter
@Setter
public class TransactionRepository {
    private Map<List<String>, List<Transaction>> queriesToTransactions;
}
