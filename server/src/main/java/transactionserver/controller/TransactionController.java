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
import java.util.Map;

@Controller
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        Map<List<String>, List<Transaction>> queriesToTransactions = service.getQueriesToTransactions();
        model.addAttribute("queriesToTransactions", queriesToTransactions);
        int showingQueriesForIndex = -1;
        if (queriesToTransactions != null) {
            model.addAttribute("showingQueriesForIndex", showingQueriesForIndex);
        }
        return "get_transactions_view.html";
    }

    @PostMapping("/updateTransactions")
    public ResponseEntity<String> updateTransactions(@RequestBody Map<String, List<Transaction>> queryToNewTransactions) {
        service.updateTransactions(queryToNewTransactions);
        return ResponseEntity.ok().build();
    }
}
