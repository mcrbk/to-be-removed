#Build & Executable Jar
* gradle build
* gradle shadowJar

#Run

Start
* java -ar ~/app/build/libs/app-1.0-all.jar (should start with port 8080)

Use the following three requests to see a message in the console:
* POST http://localhost:8080/api/v1/transfer/ABC/XYZ/100 (SUCCESS), 
* POST http://localhost:8080/api/v1/transfer/ABC/NOP/100 (VALIDATION FAILURE), 
* POST http://localhost:8080/api/v1/transfer/ABC/XYZ/1000 (INSUFFICIENT FUNDS)

#Explanation

Modules
- app (bundler, Micronaut)
- rest (Micronaut Rest)
- data (JDBC)
- model (AutoValue)
- service (Java Core)

Please find some comments in the code. Left a few out.printlns in the service (for validation).
 
