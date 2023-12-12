package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import web.database.model.Account;
import web.database.model.Action;
import web.service.MvcService;

@Controller
public class MvcController {

    private final MvcService service;

    public MvcController(MvcService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String show(Model model) {
        Account account  = new Account();
        model.addAttribute("account", account);

        Action action = new Action();
        model.addAttribute("action", action);

        String[] actionTypes = { "Przelew krajowy", "Przelew walutowy", "BLIK" };
        model.addAttribute("actionTypes", actionTypes);

        return "form.html";
    }

    @PostMapping("/createNewAccount")
    public String createNewAccount(@ModelAttribute("account") Account newAccount) {
        service.createAccount(newAccount);
        return "redirect:/";
    }

    @PostMapping("/createNewAction")
    public String createNewAction(@ModelAttribute("action") Action newAction) {
        service.createAction(newAction);
        return "redirect:/";
    }
}
