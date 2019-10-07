package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import entities.User;
import org.json.JSONObject;

import static com.jayway.restassured.RestAssured.given;

public class WaesHeroesAPIs {

    public String baserUrl;

    public WaesHeroesAPIs(String baseUrl){
         this.baserUrl = baseUrl;
    }

    public Response getUser(String username){
        RestAssured.baseURI = this.baserUrl;
        Response res = given()
                            .queryParam("username", username)
                       .when()
                       .get("/waesheroes/api/v1/users/details")
                       .then()
                       .extract()
                       .response();

        return res;
    }

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

        return res;
    }

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

        return res;
    }

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

        return res;
    }

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

        return res;
    }

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

        return res;
    }

}
