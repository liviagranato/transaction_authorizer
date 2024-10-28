# Execution Guide

## Introduction

This project was created using Java + Spring framework and contains tests and documentation.

It has three endpoints:

- POST http://localhost:8080/authorizer/
- POST http://localhost:8080/authorizer/fallback
- POST http://localhost:8080/authorizer/merchant

They refer to the following activities, respectively: L1, L2, L3.

## Requirements

- Maven 3.9.9
- Java jdk-21
- PostgreSQL or DBeaver to access database
- Docker and Docker-Compose
- Eclipse / Intellij
- Postman 

## Steps to execute

First, run "mvn clean install" in the project root and then execute "docker-compose up" to create containers.

Then open the project in Eclipse, Intellij, etc. Build and update project and run [Application file](src/main/java/com/example/transaction/authorizer/Application.java). 

## Database

To access database, create a PostgreSQL connection using the following:

- url: jdbc:postgresql://localhost:5432/authorize
- user: admin
- password: admin


Table and data are being created automatically through Flyway scripts, located at [migration folder](src/main/resources/db/migration/V001__CREATE_TABLE.sql).

- Use them to execute the [postman collection](Authorizers.postman_collection.json) requests.

## Tests

Unit tests are developed using JUnit at: [tests](src/test/java/com/example/transaction/authorizer/service/AuthorizerServiceTest.java)


## API Documentation

Check the APIs docs by accessing the following addresses:

- [OpenAPI](http://localhost:8080/api-docs)
- [Swagger](http://localhost:8080/swagger-ui/index.html)

## Other considerations

This project uses lombok dependency.

Users might experience some issues depending on the IDE they use.

In case you face any issues, consider doing one of the following:

- Download the IDE Lombok plugin;
- Download Lombok JAR from [ProjectLombok](https://projectlombok.org/download), execute the JAR and locate your IDE to install it.
