# quarkus-jdbc

This project is a Quarkus web application that uses JDBC to access PostgreSQL database.

## Live coding with Quarkus

Execute the following command:

```sh
$ ./gradlew :quarkus-jdbc:quarkusDev
```

## Run Quarkus as a native application

First compile it:

```sh
$ ./gradlew :quarkus-jdbc:build -Dquarkus.package.type=native
```

Next we need to make sure you have a PostgreSQL instance running:

```sh
$ docker run --rm=true --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3
```

Then run it:

```sh
 ./quarkus-jdbc/build/quarkus-jdbc-0.0.1-runner 
```

## See the demo in your browser

Once the application is running, open `http://localhost:8080` in your browser.
The message returned from the database will be displayed in your browser.

To add a message to the database, pass it as a query parameter, like `http://localhost:8080/add?text=Hi`.
If you open `http://localhost:8080` again, you will see the list with the added data.
