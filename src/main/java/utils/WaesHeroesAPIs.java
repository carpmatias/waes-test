package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import entities.User;
import org.testng.Reporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import static com.jayway.restassured.RestAssured.given;

public class WaesHeroesAPIs {

    public String baserUrl;
    public static ExtentTest test;

    protected static Logger logger = LogManager.getLogger(WaesHeroesAPIs.class.getName());

    public WaesHeroesAPIs(String baseUrl){
         this.baserUrl = baseUrl;
    }

    /**
     *
     * GET User endpoint - /waesheroes/api/v1/users/details
     * Retrieves the specified user.
     *
     * @param username
     *
     * @author Matías Cárdenas
     */
    public Response getUser(String username){
        RestAssured.baseURI = this.baserUrl;
        Response res = given()
                            .queryParam("username", username)
                       .when()
                       .get("/waesheroes/api/v1/users/details")
                       .then()
                       .extract()
                       .response();

        reportLog("REQUEST: GET User - /waesheroes/api/v1/users/details");
        reportLog("Query params -> username: " + username);
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    /**
     *
     * GET All users endpoint - /waesheroes/api/v1/users/all
     * Retrieves all the users
     *
     * @param username
     * @param password
     *
     * @author Matías Cárdenas
     *
     */
    public Response getAllUsers(String username, String password){
        RestAssured.baseURI = this.baserUrl;
        String key = WaesUtils.base64Encode(username + ":" + password);

        Response res = given()
                .header("Authorization", "Basic " + key)
                .when()
                .get("/waesheroes/api/v1/users/all")
                .then()
                .extract()
                .response();

        reportLog("REQUEST: GET All Users - /waesheroes/api/v1/users/all");
        reportLog("Header -> Authorization: Basic " + key);
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    /**
     *
     * GET Login endpoint - /waesheroes/api/v1/users/access
     *
     * @param username
     * @param password
     *
     * @author Matías Cárdenas
     *
     */
    public Response getLogin(String username, String password){
        RestAssured.baseURI = this.baserUrl;
        String key = WaesUtils.base64Encode(username + ":" + password);

        Response res = given()
                .header("Authorization", "Basic " + key)
                .when()
                .get("/waesheroes/api/v1/users/access")
                .then()
                .extract()
                .response();

        reportLog("REQUEST: GET Login - /waesheroes/api/v1/users/access");
        reportLog("Header -> Authorization: Basic " + key);
        reportLog("Query params -> username: " + username);
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    /**
     *
     * POST Sign up - /waesheroes/api/v1/users/access
     *
     * @param body Payload with user to create.
     *
     * @author Matías Cárdenas
     *
     */
    public Response postSignUp(JSONObject body){
        RestAssured.baseURI = this.baserUrl;

        Response res = given()
                .header("Content-Type", "application/json")
                .body(body.toString())
                .when()
                .post("/waesheroes/api/v1/users")
                .then()
                .extract()
                .response();

        reportLog("REQUEST: POST Sign up - /waesheroes/api/v1/users");
        reportLog("Header -> Content-Type: application/json");
        reportLog("Body -> " + body.toString());
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    /**
     *
     * PUT User endpoint - /waesheroes/api/v1/users
     *
     * @param username
     * @param password
     * @param body Payload with user data to update.
     *
     * @author Matías Cárdenas
     *
     */
    public Response putUser(String username, String password, JSONObject body){
        RestAssured.baseURI = this.baserUrl;
        String key = WaesUtils.base64Encode(username + ":" + password);

        Response res = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + key)
                .body(body.toString())
                .when()
                .put("/waesheroes/api/v1/users")
                .then()
                .extract()
                .response();

        reportLog("REQUEST: PUT User - /waesheroes/api/v1/users");
        reportLog("Header -> Content-Type: application/json");
        reportLog("Header -> Authorization: Basic " + key);
        reportLog("Body -> " + body.toString());
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    /**
     *
     * DEL User endpoint - /waesheroes/api/v1/users
     *
     * @param username
     * @param password
     * @param body Payload with user to delete.
     *
     * @author Matías Cárdenas
     *
     */
    public Response deleteUser(String username, String password, JSONObject body){
        RestAssured.baseURI = this.baserUrl;
        String key = WaesUtils.base64Encode(username + ":" + password);

        Response res = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + key)
                .body(body.toString())
                .when()
                .delete("/waesheroes/api/v1/users")
                .then()
                .extract()
                .response();

        reportLog("REQUEST: DEL User - /waesheroes/api/v1/users");
        reportLog("Header -> Content-Type: application/json");
        reportLog("Header -> Authorization: Basic " + key);
        reportLog("Body -> " + body.toString());
        reportLog("RESPONSE: ");
        reportLog("Code -> " + res.getStatusCode());
        reportLog("Body -> " + res.asString());

        return res;
    }

    public void reportLog(String message) {
        if(test != null)
            test.log(LogStatus.INFO, message);
        logger.info("Message: " + message);
        Reporter.log(message);
    }


}
