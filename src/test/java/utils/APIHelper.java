package utils;

import static io.restassured.RestAssured.given;

public class APIHelper {

    public static final String BASE_URL = "https://bookcart.azurewebsites.net";
    public static String username;
    private static final int bookId = 3; // Example book ID
    public static String registerPayload = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"username\": \"" + username + "\", " +
            "\"password\": \"Test@1234\", \"confirmPassword\": \"Test@1234\", \"gender\": \"Female\" }";
    public static String loginPayload = "{ \"username\": \"" + username + "\", \"password\": \"Test@1234\" }";
    public static String checkoutPayload = "{ \"orderDetails\": [{ \"book\": { \"bookId\": " + bookId + " }, \"quantity\": 1 }], \"cartTotal\": 1 }";
    }