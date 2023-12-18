package web.configuration;

import org.springframework.stereotype.Component;
import web.database.DatabaseConnection;

import javax.annotation.PreDestroy;

@Component
public class DataCleaner {

    @PreDestroy
    public void destroy() {
        DatabaseConnection.close();
        System.out.println("Connection to PostgreSQL closed.");
    }
}
