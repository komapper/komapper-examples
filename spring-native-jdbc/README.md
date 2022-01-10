# spring-native-jdbc

This project is a Spring Boot web application that supports
[Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
and uses JDBC to access the database.

## Build

You can build the native application with the following command:

```sh
$ ./gradlew :spring-native-jdbc:bootBuildImage
```

## Running

To run the application, start Docker as follows:

```sh
$ docker run --rm -p 8080:8080 docker.io/library/spring-native-jdbc:0.0.1
````

Once the application is running, open `http://localhost:8080` in your browser.
The message returned from the database will be displayed in your browser.

To add a message to the database, pass it as a query parameter, like `http://localhost:8080/?text=Hi`.
If you open `http://localhost:8080` again, you will see the list with the added data.
