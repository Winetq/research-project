package ui;
import database.model.Account;
import database.model.Customer;
import database.repository.AccountRepository;
import database.repository.ActionRepository;
import database.repository.CustomerAccountRefRepository;
import database.repository.CustomerRepository;

import database.model.Action;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import java.awt.Container;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MainView {

    private final static String FRAME_TITLE = "Banking app";
    private final static String INSERT_ACCOUNT = "Create new account";
    private final static String INSERT_ACCOUNT_BALANCE = "Balance:";
    private final static String SELECT_ALL_ACCOUNTS = "Select all accounts";
    private final static String SELECT_ALL_CUSTOMERS = "Select all customers";
    private final static String SELECT_ALL_ACTIONS = "Select all actions";
    private final static String CREATE = "Create";

    private final static String ACTION_SECTION = "Action";
    private final static String INSERT_ACTION = "Create new action";
    private final static String INSERT_ACTION_TITLE = "Title:";
    private final static String INSERT_ACTION_AMOUNT = "Amount:";
    private final static String INSERT_ACTION_TYPE = "Type:";
    private final static String INSERT_ACTION_ACCOUNT_ID = "Account id:";
    private final static String INSERT_ACTION_STATUS = "Status:";

    private final AccountRepository accountRepository;
    private final ActionRepository actionRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAccountRefRepository customerAccountRefRepository;

    public MainView(AccountRepository accountRepository,
            ActionRepository actionRepository,
            CustomerRepository customerRepository,
            CustomerAccountRefRepository customerAccountRefRepository) {

        this.accountRepository = accountRepository;
        this.actionRepository = actionRepository;
        this.customerRepository = customerRepository;
        this.customerAccountRefRepository = customerAccountRefRepository;

        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        // Components

        // select * (accounts, customers, actions)
        JButton selectAllAccountsButton = new JButton(SELECT_ALL_ACCOUNTS);
        JButton selectAllCustomersButton = new JButton(SELECT_ALL_CUSTOMERS);
        JButton selectAllActionsButton = new JButton(SELECT_ALL_ACTIONS);

        // insert account
        JLabel addAccountLabel = new JLabel(INSERT_ACCOUNT);
        JLabel addAccountBalanceLabel = new JLabel(INSERT_ACCOUNT_BALANCE);
        JTextField addAccountBalanceTextField = new JTextField("", 10);
        JButton addAccountButton = new JButton(CREATE);

        // insert action
        JLabel addActionLabel = new JLabel(INSERT_ACTION);
        JLabel addActionTitleLabel = new JLabel(INSERT_ACTION_TITLE);
        JTextField addActionTitleTextField = new JTextField("", 30);
        JLabel addActionAmountLabel = new JLabel(INSERT_ACTION_AMOUNT);
        JTextField addActionAmountTextField = new JTextField("", 30);
        JLabel addActionTypeLabel = new JLabel(INSERT_ACTION_TYPE);
        JTextField addActionTypeTextField = new JTextField("", 30);
        JLabel addActionAccountIdLabel = new JLabel(INSERT_ACTION_ACCOUNT_ID);
        JTextField addActionAccountIdTextField = new JTextField("", 30);
        JLabel addActionStatusLabel = new JLabel(INSERT_ACTION_STATUS);
        JTextField addActionStatusTextField = new JTextField("", 30);
        JButton addActionButton = new JButton(CREATE);

        // add listeners
        selectAllAccountsButton.addActionListener(e -> {
            List<Account> accountList = accountRepository.getAllAccounts();
            System.out.println(accountList);
        });

        selectAllCustomersButton.addActionListener(e -> {
            List<Customer> customerList = customerRepository.getAllCustomers();
            System.out.println(customerList);
        });

        selectAllActionsButton.addActionListener(e -> {
            List<Action> actionList = actionRepository.getAllActions();
            System.out.println(actionList);
        });

        addAccountButton.addActionListener(e -> {
            int accountBalance = Integer.parseInt(addAccountBalanceTextField.getText());
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(LocalDateTime.now());
            accountRepository.addAccount(accountBalance, date);
        });

        addActionButton.addActionListener(e -> {
            String actionTitle = addActionTitleTextField.getText();
            int actionAmount = Integer.parseInt(addActionAmountTextField.getText());
            String actionType = addActionTypeTextField.getText();
            Long actionAccountId = Long.getLong(addActionAccountIdTextField.getText());
            String actionStatus = addActionStatusTextField.getText();
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(LocalDateTime.now());
            actionRepository.addAction(actionTitle, actionAmount, actionType, actionAccountId, actionStatus, date);
        });

        // add components
        contentPane.add(addAccountLabel);
        contentPane.add(addAccountBalanceLabel);
        contentPane.add(addAccountBalanceTextField);
        contentPane.add(addAccountButton);

        contentPane.add(selectAllAccountsButton);
        contentPane.add(selectAllCustomersButton);
        contentPane.add(selectAllActionsButton);

        contentPane.add(addActionLabel);
        contentPane.add(addActionTitleLabel);
        contentPane.add(addActionTitleTextField);
        contentPane.add(addActionAmountLabel);
        contentPane.add(addActionAmountTextField);
        contentPane.add(addActionTypeLabel);
        contentPane.add(addActionTypeTextField);
        contentPane.add(addActionAccountIdLabel);
        contentPane.add(addActionAccountIdTextField);
        contentPane.add(addActionStatusLabel);
        contentPane.add(addActionStatusTextField);
        contentPane.add(addActionButton);

        // Constraints

        // select * (accounts, customers, actions)
        layout.putConstraint(SpringLayout.NORTH, selectAllAccountsButton, 16, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, selectAllAccountsButton, 16, SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.NORTH, selectAllCustomersButton, 16, SpringLayout.SOUTH, selectAllAccountsButton);
        layout.putConstraint(SpringLayout.WEST, selectAllCustomersButton, 16, SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.NORTH, selectAllActionsButton, 16, SpringLayout.SOUTH, selectAllCustomersButton);
        layout.putConstraint(SpringLayout.WEST, selectAllActionsButton, 16, SpringLayout.WEST, contentPane);

        // insert account
        layout.putConstraint(SpringLayout.WEST, addAccountLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addAccountLabel,32,SpringLayout.SOUTH, selectAllActionsButton);

        layout.putConstraint(SpringLayout.WEST, addAccountBalanceLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addAccountBalanceLabel,16,SpringLayout.SOUTH, addAccountLabel);

        layout.putConstraint(SpringLayout.WEST, addAccountBalanceTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addAccountBalanceTextField,16,SpringLayout.SOUTH, addAccountLabel);

        layout.putConstraint(SpringLayout.WEST, addAccountButton,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addAccountButton,16,SpringLayout.SOUTH, addAccountBalanceTextField);

        // insert action
        layout.putConstraint(SpringLayout.WEST, addActionLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionLabel,32,SpringLayout.SOUTH, addAccountButton);


        layout.putConstraint(SpringLayout.WEST, addActionTitleLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionTitleLabel,16,SpringLayout.SOUTH, addActionLabel);

        layout.putConstraint(SpringLayout.WEST, addActionTitleTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionTitleTextField,16,SpringLayout.SOUTH, addActionLabel);


        layout.putConstraint(SpringLayout.WEST, addActionAmountLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionAmountLabel,16,SpringLayout.SOUTH, addActionTitleLabel);

        layout.putConstraint(SpringLayout.WEST, addActionAmountTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionAmountTextField,16,SpringLayout.SOUTH, addActionTitleLabel);


        layout.putConstraint(SpringLayout.WEST, addActionTypeLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionTypeLabel,16,SpringLayout.SOUTH, addActionAmountLabel);

        layout.putConstraint(SpringLayout.WEST, addActionTypeTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionTypeTextField,16,SpringLayout.SOUTH, addActionAmountLabel);


        layout.putConstraint(SpringLayout.WEST, addActionAccountIdLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionAccountIdLabel,16,SpringLayout.SOUTH, addActionTypeLabel);

        layout.putConstraint(SpringLayout.WEST, addActionAccountIdTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionAccountIdTextField,16,SpringLayout.SOUTH, addActionTypeLabel);


        layout.putConstraint(SpringLayout.WEST, addActionStatusLabel,16,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, addActionStatusLabel,16,SpringLayout.SOUTH, addActionAccountIdLabel);

        layout.putConstraint(SpringLayout.WEST, addActionStatusTextField,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionStatusTextField,16,SpringLayout.SOUTH, addActionAccountIdLabel);


        layout.putConstraint(SpringLayout.WEST, addActionButton,6,SpringLayout.EAST, addActionAccountIdLabel);
        layout.putConstraint(SpringLayout.NORTH, addActionButton,16,SpringLayout.SOUTH, addActionStatusLabel);

        // contentPane
        layout.putConstraint(SpringLayout.EAST, contentPane,16,SpringLayout.EAST, addActionAccountIdTextField);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,16,SpringLayout.SOUTH, addActionButton);

        frame.pack();
        frame.setVisible(true);
    }

}
