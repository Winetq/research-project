package transactionserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transactionserver.model.Transaction;
import transactionserver.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public void updateTransactions(List<Transaction> transactions) {
        repository.updateList(transactions);
    }

    public List<Transaction> getTransactions() {
        return repository.getTransactionList();
    }
}
