import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class ApiFetcher {

  public static void main(String[] args) {
    // Fetch and print user data from API
    fetchAndPrintUsers();
    // Fetch and print post data from API
    fetchAndPrintPosts();
  }

  // Helper function to send HTTP GET request and return response body as a string
  private static String fetchData(String url) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
    // Ensure response status is OK, otherwise throw an exception
    if (response.statusCode() == 200){
      return response.body();
    } else {
      throw new RuntimeException("Http error: " + response.statusCode());
    }
  }

  // Fetch user data and print relevant information to the console
  private static void fetchAndPrintUsers() {
    String url = "https://fake-json-api.mock.beeceptor.com/users";
    
    try {
      String response = fetchData(url);
      JsonArray users = JsonParser.parseString(response).getAsJsonArray();
      System.out.println("=== Users Json Parsing ===");

      // Iterating through the user JSON array
      for (var user : users) {
        JsonObject userObject = user.getAsJsonObject();
        System.out.printf("ID: %s, Name: %s, Email: %s%n",
          userObject.get("id").getAsString(),
          userObject.get("name").getAsString(),
          userObject.get("email").getAsString()
        );
      }
    } catch (Exception e) {
      // Handle any errors that occurred during the data fetch or JSON parsing
      System.out.println("Error fetching users: " + e.getMessage());
    }
  }


  // Fetch post data and print relevant information to the console
  private static void fetchAndPrintPosts() {
    String url = "https://dummy-json.mock.beeceptor.com/posts";
    
    try {
      String response = fetchData(url);
      JsonArray posts = JsonParser.parseString(response).getAsJsonArray();
      System.out.println("\n=== Posts ===");

      // Iterating through the post JSON array
      for (var post : posts) {
        JsonObject postObject = post.getAsJsonObject();
        System.out.printf("ID: %s, Title: %s, Body: %s%n",
          postObject.get("id").getAsString(),
          postObject.get("title").getAsString(),
          // Limiting body content output to the first 50 characters
          postObject.get("body").getAsString().substring(0, 50)
        );
      }
    } catch (Exception e) {
      // Handle any errors that occurred during the data fetch or JSON parsing
      System.out.println("Error fetching posts: " + e.getMessage());
    }
  }
}
