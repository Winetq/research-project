import database.model.Account;
import database.model.Action;
import database.model.Customer;
import database.model.CustomerAccountRef;
import database.repository.AccountRepository;
import database.repository.ActionRepository;
import database.repository.CustomerAccountRefRepository;
import database.repository.CustomerRepository;
import ui.MainView;

import java.util.List;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        AccountRepository accountRepository = new AccountRepository();
        List<Account> accounts = accountRepository.getAllAccounts();

        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> customers = customerRepository.getAllCustomers();

        ActionRepository actionRepository = new ActionRepository();
        List<Action> actions = actionRepository.getAllActions();

        CustomerAccountRefRepository customerAccountRefRepository = new CustomerAccountRefRepository();
        List<CustomerAccountRef> customerAccountRefs = customerAccountRefRepository.getAllCustomerAccountRefs();

        new MainView(accountRepository, actionRepository, customerRepository, customerAccountRefRepository);
    }
}