package transactionserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import transactionserver.model.Transaction;
import transactionserver.service.TransactionService;

import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Transaction> transactions = transactionService.getTransactions();
        model.addAttribute("transactions", transactions);
        int showingQueriesForIndex = -1;
        if (transactions != null)
            model.addAttribute("averageTimes", transactions.stream().map(Transaction::getAverageTime).toArray());
            model.addAttribute("showingQueriesForIndex", showingQueriesForIndex);

        return "get_transactions_view.html";
    }

    @PostMapping("/updateTransactions")
    public ResponseEntity<String> updateTransactions(@RequestBody List<Transaction> newTransactions) {
        transactionService.updateTransactions(newTransactions);
        return ResponseEntity.ok().build();
    }
}
