package transactionserver.repository;

import org.springframework.stereotype.Repository;
import transactionserver.model.Transaction;

import java.util.List;

@Repository
public class TransactionRepository {

    private List<Transaction> transactionList;

    public void updateList(List<Transaction> queriesToTimeMap){
        transactionList = queriesToTimeMap;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
