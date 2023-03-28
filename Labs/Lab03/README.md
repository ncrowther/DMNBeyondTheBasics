#  DMN Large rules demo

## Description

Example DMN showing how to organise and execute a large number of rules.

The requirements are derived from [Rule spreasheet](docs/HierarchicalRules.xlsx)

The large decision table in [R1.3.1.01.dmn](D:/A_Consultancy/techsales/2023/BelfiusInsurance/LargeRulesetsDMN/src/main/resources/org/acme/belfius/rules/R1.3/R1.3.1/R1.3.1.01.dmn) is generated from [Java Rule table generator](src/main/java/XMLGenerator.java)

## Top level DMN

<p align="Left"><img src="docs/images/R131.png"></p>


## Edit and view project

* Open and view the project in VSC using the DMN plugin

### Running

You will need:

* Java 11+ installed
* Environment variable JAVA_HOME set accordingly
* Maven 3.5.2+ installed

#### Compile and Run in Local Dev Mode

```sh
mvn clean compile quarkus:dev
```

## API

```sh
http://localhost:8080/q/swagger-ui/#/R%201_463_461%20Resource/post_R1_3_1
```

## Test Payload

```sh
{
  "A": "Red",
  "B": "Green",
  "C": "Green"
}
```