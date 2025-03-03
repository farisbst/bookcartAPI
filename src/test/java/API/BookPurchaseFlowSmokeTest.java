package API;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.APIHelper;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class BookPurchaseFlowSmokeTest {

    private static String authToken;
    private static int userId;
    private final int bookId = 3; // Example book ID
    private static String username;

    // Verification of registration process
    @Test(priority = 1)
    public void registerUser() {
        // Create a unique username to avoid conflicts
        username = "TestUser" + UUID.randomUUID().toString().substring(0, 5);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(APIHelper.registerPayload)
                .post("https://bookcart.azurewebsites.net/api/User")
                .then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "User registration failed");
    }

    // Login verification
    @Test(priority = 2, dependsOnMethods = "registerUser")
    public void loginUser() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(APIHelper.loginPayload)
                .post("https://bookcart.azurewebsites.net/api/Login")
                .then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Login failed");

        // Extract auth token & userId
        authToken = response.jsonPath().getString("token");
        userId = response.jsonPath().getInt("userDetails.userId");
    }

    // Verification of adding items to cart
    @Test(priority = 3, dependsOnMethods = "loginUser")
    public void addBookToCart() {
        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .post("https://bookcart.azurewebsites.net/api/ShoppingCart/AddToCart/" + userId + "/" + bookId)
                .then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to add book to cart");
    }

    // Completing the purchase verification
    @Test(priority = 4, dependsOnMethods = "addBookToCart")
    public void checkout() {
        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(APIHelper.checkoutPayload)
                .post("https://bookcart.azurewebsites.net/api/CheckOut/" + userId)
                .then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Checkout failed");
    }
}