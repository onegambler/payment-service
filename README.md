# PaymentService

### Assumptions
* All transfers will be in the same currency, for simplicity.
* Transactions are successful only if accounts have enough balance to cover the transaction.
* Account balances must be 0 or more. It is assumed overdrafts are not allowed.

### Language and Frameworks
* Java 8
* Dropwizard (http://www.dropwizard.io/) for the RESTful API. It makes use of
    * Jersey - As REST library
    * Jackson - As JSON parser
    * Guice - For dependency injection
    * JDBI - For database access
    * Lombok - Used to reduce Java verbosity
    * JUnit - for testing
    * AssertJ - for testing
    * Mockito - for testing
* H2 database
* Swagger - for API documentation

### Implementation details
Used H2 as database. Easy to set up and run within Dropwizard.

### Resources
Documentation can be found at [http://localhost:8080/swagger](http://localhost:8080/swagger), once started the application.

The application contains two different resources
* Account - CRUD operations
    * Add account - POST `/account`
    * Update account - PUT `/account/{:id}`
    * Get account - GET `/account/{:id}`
    * Delete account - DELETE `/account/{:id}`
    
    Where the message for add and update is a json message in this form
    ```json
    {
        "holderName": "Roberto",
        "balance": 1230000
    }
    ```
    
* Transaction
    * Create new transaction - POST `/transaction`
    
    Where the message looks like the following
    ```json
    {
      "sourceAccountId": 1,
      "destinationAccountId": 2,
      "amount": 23000
    }
    ```
    
    In case `sourceAccountId` or `destinationAccountId` don't correspond to existing accounts then a http `404` error is returned.
    In case the `amount` is greater than the `sourceAccount` then an http `400` is returned as the transfer is not possible: the user does not have enough funding.

    * Get an existing transactions - GET `/transaction/{:id}`
    
### Concurrency
In order to be safe in case of high concurrency, the database is using `SERIALIZABLE` transaction isolation level: both read locks and write locks are kept until the transaction commits. 
So, I'm using pessimistic locking, rather than optimistic. The assumption here is that there is no requirement about reading resources: with pessimistic locking locks are applied in a fail-safe way, without impacting performances.
In case there is a requirement for frequent reads as well, then an optimistic lock is a better approach.


### Improvements

* Tests: Currently code coverage is not great. I wrote most of the unit tests, but not enough integration tests. So, tests should definitely be improved.
* Introduce HATEOS on resources. If required. It depends on which entities will interact with the webservice.

How to start the PaymentService application
---
1. Run `mvn clean install` to build your application
1. Create and initialise the database with  `java -jar target/payment-service-1.0-SNAPSHOT.jar db migrate config.yml`
1. Start application with `java -jar target/payment-service-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

where `config.yml` is the configuration yaml file contained in the `root` folder.



Health Check
---

To see the applications health enter url `http://localhost:8081/healthcheck`