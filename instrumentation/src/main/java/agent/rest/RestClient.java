package agent.rest;

import agent.rest.model.Transaction;
import agent.rest.model.TransactionConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class RestClient {

    private static final int OK_STATUS_CODE = 200;

    public void updateTransactions(Map<String, List<Long>> transactions, Map<String, List<String>> originalQueries) {
        Client client = ClientBuilder.newClient().register(new ObjectMapper());
        WebTarget webTarget = client.target("http://localhost:8080/updateTransactions");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        List<Transaction> transactionList = new TransactionConverter().toTransaction(transactions, originalQueries);

        String transactionsString = null;
        try {
            transactionsString = new ObjectMapper().writeValueAsString(transactionList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Response response = invocationBuilder.post(Entity.entity(transactionsString, MediaType.APPLICATION_JSON));

        if(response.getStatus() != OK_STATUS_CODE) {
            System.err.printf("There was a problem during sending transactions to server, StatusCode: %d%n", response.getStatus());
        }
    }
}
