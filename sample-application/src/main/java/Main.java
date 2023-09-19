import database.repository.AccountRepository;
import database.repository.ActionRepository;
import database.repository.CustomerAccountRefRepository;
import database.repository.CustomerRepository;
import ui.MainView;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        new MainView(new AccountRepository(), new ActionRepository(), new CustomerRepository(), new CustomerAccountRefRepository());
    }

}