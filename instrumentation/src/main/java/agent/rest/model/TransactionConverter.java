package agent.rest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransactionConverter {

    public List<Transaction> toTransaction(Map<String, List<Long>> queryToTime){
        List<Transaction> transactionList = new ArrayList<>();

        for (Map.Entry<String, List<Long>> entry: queryToTime.entrySet()) {
            transactionList.add(new Transaction(Arrays.asList(entry.getKey().split(";")), entry.getValue()));
        }
        return transactionList;
    }
}
