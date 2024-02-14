To build the docker image run `docker build -t dkramer-fetch-test .` in the root project directory to build the docker image.

To deploy the project run `docker run -p 8080:8080 -t dkramer-fetch-test` which will allow connections on port 8080 for testing.

Also did it in Go as a challenge to myself. See that [here](https://github.com/dkramer/fetch-go).


The project is a Spring Boot application.\
On startup an in memory h2 DB is started and populated by the commands in `\src\main\resources\schema.sql`.\
The handler for the endpoints are in `ReceiptResource.java`.\
The `/receipts/process` endpoint populates the points total and saves the receipt and items to the DB. Note log/console output when this is done to see calculated points (in addition to the `/receipts/{id}/points` endpoint).\


There are also tests in `src/test/java/dev/dkramer/fetch/challenge/ReceiptResourceTests.java`.
