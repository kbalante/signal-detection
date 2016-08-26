Vaadin Chart application built using:

- Vaadin Framework 3.7
- Vaadin Charts (free license stored in home folder)
- Java 1.8
- JPA
- MySQL (jdbc:mysql://localhost/aimsio - see application.properties)
- Spring Boot
- Spring Initializer
- Maven
- IntelliJ

Installation and execution:

1. run "mvn install"

2. install license for Vaadin Charts in your home directory
   see: https://vaadin.com/docs/-/part/charts/java-api/charts-installing.html

3. run "mvn clean vaadin:update-widgetset vaadin:compile"

4. run "mvn package"

5. run "mvn spring-boot:run"

6. test locally on http://localhost:8080
