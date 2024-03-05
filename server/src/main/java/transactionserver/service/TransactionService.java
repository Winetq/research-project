package transactionserver.service;

import org.springframework.stereotype.Service;
import transactionserver.model.Transaction;
import transactionserver.repository.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void updateTransactions(Map<String, List<Transaction>> queryToNewTransactions) {
        Map<List<String>, List<Transaction>> queriesToNewTransactions = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : queryToNewTransactions.entrySet()) {
            queriesToNewTransactions.put(asList(entry.getKey().split(";")), entry.getValue());
        }

        repository.setQueriesToTransactions(queriesToNewTransactions);
    }

    public Map<List<String>, List<Transaction>> getQueriesToTransactions() {
        return repository.getQueriesToTransactions();
    }
}
