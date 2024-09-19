To launch the application, use command

``docker compose up -d --no-deps --build --force-recreate``

This will run a detached docker instance, rebuild dependencies, and force recreation of the container.

Design:
* Java JDK 21
* Spring Boot

Database:
* H2 in memory database

Port:
* 8080

Endpoints:

``POST /receipts/process`` 

Posts the receipt for processing and calculating points. Points are stored in database. I did not elect for editing receipts as that would be too complicated. 

We are assuming data *can be* wrong, but is not outright wrong. IE, the vision data did not pick up a proper format, but not that the vision data picked up 30 instead of 38. We are assuming the data is correct, but the format is wrong upon an error.

The points are calculated and stored in the database.


``GET /receipts/{id}/points``

Gets the points from the database for a specific receipt ID

