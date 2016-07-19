# Live Order Board

Service that accepts and cancels orders and can produce order board summary.

## Functionality

Order Service supports the following functionality:

* Register an order. Order must contain these fields:
    * user id
    * order quantity (e.g.: 3.5 kg)
    * price per kg (e.g.: £303)
    * order type: BUY or SELL

* Cancel a registered order - this will remove the order from board

* Get summary information of live orders:
    * orders for the same price are be merged together producing summary with appropriate price and overall quantity 
    * the lowest price of SELL orders summary is displayed first
    * the highest price of BUY orders summary is displayed first

## Environment

Program supports:
* Java 8
* Maven 3.3.9

## Management

* Build and install application:
```
mvn clean install
```