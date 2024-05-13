## About

This project was developing as a part of the Research Project course at Gdansk University of Technology. However, I am
still working on this because it is also my master's thesis. The topic is Automated tracking and grouping of database
transactions in Java Virtual Machine (JVM) applications. The purpose of this project is to provide a Java agent JAR that
can be attached to any Java application. Its task is to dynamically injects bytecode to capture database queries and
transactions and measure their time. You can export the telemetry data in a variety of formats. The net result is the
ability to gather telemetry data from a Java application without code changes.
 
## Getting Started

Create a Java agent JAR inside the `instrumentation` module using this command:
```
mvn clean package
```

After that you can run an example application for example from `sample-web-application` module with this agent using 
the `-javaagent` flag:
```
java -javaagent:path\to\instrumentation-1.0-SNAPSHOT-jar-with-dependencies.jar -jar sample-web-application.jar
```

Or with:
```
java -javaagent:path\to\instrumentation-1.0-SNAPSHOT-jar-with-dependencies.jar=console -jar sample-web-application.jar
```

This way all data will be exported to console. However, there is a possibility to send data to a dedicated server from
the `server` module. To run this server use the latest image:
```
docker run -d -p 8080:8080 mcwynar/research-project:agent_server
```

And change console to server:
```
java -javaagent:path\to\instrumentation-1.0-SNAPSHOT-jar-with-dependencies.jar=server -jar sample-web-application.jar
```

## Architecture Diagram

![solution_architecture](https://github.com/Winetq/research-project/assets/62242952/95be5caf-2832-4ef6-9f45-a813d762456c)

## Limitations

This agent is working only with Java applications that have a PostgreSQL database.
