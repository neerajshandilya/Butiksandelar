# Butiksandelar

ATG Test Project

### Task

* Create a **Spring Boot service**, that can take an arbitrary list of games as input (JSON), and produce a sorted list
  of
  games (JSON), described above.
* The JDK version used shall be **11**.
* We do not want to hardcode the "big" types/dates so this shall be configurable at service-startup.
* The functionality of the service shall be covered by JUnit 5 tests. The line coverage should be at, or over, 80% for
  each file.
* The service shall be built as a "fat-jar" using Maven.
* When/if using third-party dependencies, those shall be well-proven and considered to be standard within the industry.
* The code shall be self-explanatory, clear and easy to understand. Document the code only when needed to achieve this.

##### Assumption during implementation

* if the input JSON has a Game data which start date is in the past then we discard that input silently.
* during winterbust period if input JSON has incorrect game type(means other then allowed type) will discard that input
  as well.
* if the input JSON has a Game data which start date is game free day that will discard that input as well.

### Information

This project has the following dependency:

* Required **JDK Version** is **'17'**
* SpringBoot version is **3.0.0**
* Maven **3.8.5**

Spring Boot 3.0 require Java 17 therefore I was forced to use **Java 17**
[reference link](https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0)

### Reference Documentation

For further reference, please consider the following sections:

* [Code Repo link](https://github.com/neerajshandilya/Butiksandelar)
* [CI/CD build pipeline link](https://github.com/neerajshandilya/Butiksandelar/actions/workflows/maven.yml)

* Build command `mvn clean verify`
* Run in local `mvn spring-boot:run`
* All configuration can found `src/main/resources/application.yaml`