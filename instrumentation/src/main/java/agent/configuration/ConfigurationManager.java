package agent.configuration;

import agent.configuration.exception.HttpConfigurationException;
import agent.configuration.util.Json;
import agent.transformer.PgConnectionTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private static Configuration configuration;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance(){

        if(instance == null){
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void loadConfigurationFile(String fileName) {
        try {
            InputStream fileContent = ConfigurationManager.class.getResourceAsStream(fileName);
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = fileContent.read()) != -1) {
                stringBuilder.append((char) i);
            }

            JsonNode conf = Json.parse(stringBuilder.toString());
            configuration = Json.parseJsonNodeToString(conf, Configuration.class);
        } catch (IOException e) {
            throw new HttpConfigurationException("Couldn't load configuration file");
        }
    }

    public Configuration getConfiguration(){
        if(configuration == null){
            throw new HttpConfigurationException("There is no configuration");
        }
        return configuration;
    }
}
