### Ted Talk Crud Application

## Overview
This application provides CRUD operations for a TED TALK Application.

It provides the below operations:
- **Create a Ted Talk**: A new ted talk can be created.
- **Update a Ted Talk**: An existing ted talk can be updated.
- **Retrieve a Ted Talk**: An existing ted talk can be retrieved by supplying an id.
- **Search a Ted Talk**: Ted talks can be searched based on Author, Title, Minimum Likes and Views.
- **Delete a Ted Talk**: An existing Ted Talk can be deleted by supplying the id.
- **Retrieve all Ted Talks**: All the ted talks can be retrieved.

### How to run the Application

The below steps provide a step-by-step guide on how to run the Ted Talk CRUD application.

## Prerequisites
Before running the application, please ensure the below components are installed and available

- **Java 17**: This application is developed using Java 17. Please ensure that it is installed either directly into the system or via the IDE.
- **Maven**: This application is built using Maven. Please ensure that it is installed and available in the system.
- **PostgreSQL**: This application uses PostgreSQL as the database. Please ensure that it is installed and running on the system.

## Steps to run the application
- **Import Source Code**: Clone the repository from Git and import in your preferred IDE.
- **Setting Up Database**:
    - Create a new database in PostgreSQL with the name `ted_talk`.
    - Update the `application.properties` file with the database connection details.
- **CSV File**:
    - The `data.csv` file is already present in static folder of src/main/resources
- **Build the Application**: Build the application using the below command
    ```shell
    mvn clean install
- **Run the Application**: Run the application using the below command
    ```shell
    mvn spring-boot:run
-  You can also run the application by navigating to the `TedTalkCrudApplication` class and running it as a Java Application.
-  Another way to run the application is by navigating to the targets folder of the project and running the below command.
    ```shell
    java -jar tedtalk-0.0.1-SNAPSHOT.jar
- **Access the Application**: The application can be accessed using the below URL
    ```shell
    http://localhost:8083/swagger-ui.html
