# API Fetcher

This Java application retrieves and displays user and post data from specified APIs using Java's `HttpClient` and Google's `Gson` library.

## Features

- **Fetch User Data**: Retrieves user information, including ID, name, and email, from the API at `https://fake-json-api.mock.beeceptor.com/users`.
- **Fetch Post Data**: Retrieves post details, including ID, title, and a snippet of the body, from the API at `https://dummy-json.mock.beeceptor.com/posts`.

## Prerequisites

- **Java 11 or higher**: Utilizes the `HttpClient` introduced in Java 11.
- **Gson Library**: Required for parsing JSON responses.

  To include Gson in your project, you can add the following dependency to your `pom.xml` if you're using Maven:

  ```xml
  <dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.9</version>
  </dependency>
  ```

  Alternatively, download the Gson JAR from the [Maven Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson) and add it to your project's classpath.

## Usage

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/goringich/java__pet-projects.git
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd java__pet-projects/api-fetcher
   ```

3. **Compile the Application**:

   ```bash
   javac -cp ".:path/to/gson.jar" ApiFetcher.java
   ```

   Replace `path/to/gson.jar` with the actual path to the Gson JAR file on your system.

4. **Run the Application**:

   ```bash
   java -cp ".:path/to/gson.jar" ApiFetcher
   ```

   The application will output the fetched user and post data to the console.

## Error Handling

The application includes basic error handling to manage exceptions that may occur during HTTP requests or JSON parsing. Error messages will be displayed in the console if issues arise during execution.

## Notes

- **API Endpoints**: The application fetches data from mock API endpoints provided by Beeceptor. Ensure these endpoints are accessible and returning the expected JSON structure.

- **JSON Parsing**: The application uses Gson's `JsonParser` to parse JSON arrays and objects. Ensure the JSON responses match the expected format to avoid parsing errors.

## References

- [Java 11 HttpClient & Gson Tutorial](https://www.techiediaries.com/java/java-11-httpclient-gson-send-http-get-parse-json-example/)
- [Gson - Read and Parse JSON from URL](https://www.javacodeexamples.com/gson-read-and-parse-json-from-url/14635)

These resources provide additional insights into using `HttpClient` and `Gson` for HTTP requests and JSON parsing in Java.
