## Revature Bank API & React Frontend
A json API simulating a bank (as in investment bank) server built during [Revature Online Certification](https://revature.com/) (ROC). A browser frontend for the API built with ReactJS.

## Technologies used
* Apache tomcat
* Nginx
* Java Servlets
* Java Database Connectivity (JDBC)
* PostgreSQL
* Maven
* React (with JSX compiled using babel)

## Set up
* Set up the database: In PostgreSQL, create two databases, production and testing. Run the databases.sql file (in src/main/resources) in both databases. Run initial_data.sql in just the production database.
* Set up JDBC. Create 4 environmental variables: postgres_user, postgres_pass, postgres_URL, postgres_URL_test, for your PostgreSQL username, password, production database URL and testing database URL, respectively.
* Create war. Run `mvn package`. This will run a bunch of integration tests to make sure java can access PostgreSQL.
* Start API. Deploy the war file created in the last step to tomcat. Make it is deployed to `/BankAPI`. The servlets parse the URIs, and expect them to start with `/BankAPI`.
* Set up html server. Configure nginx to serve static files from the frontend/dist directory. Also, configure nginx as a reverse proxy, passing all requests with a URI beginning with /BankAPI/ to the tomcat server.

## Note on git
The project was built while working in a shared git repository containing unrelated code. Thus, this git repository has no commit history.