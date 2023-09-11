## How it works  

In order to run these applications you have to create a jar for instrumentation, add a path to this jar using
-javaagent argument as VM options for sample-application and run sample-application:
```
-javaagent:instrumentation\target\instrumentation-1.0-SNAPSHOT-jar-with-dependencies.jar
```