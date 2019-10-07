package waesqa;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import entities.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import testutils.TestBase;
import utils.WaesUtils;

import java.io.IOException;
import java.util.Random;

public class DeleteUserTest extends TestBase {

    private User userToDelete;

    @BeforeClass
    public void beforeClass(){
        // Create user for remove in positive test case
        int userNumber = new Random().nextInt(1000);
        userToDelete = new User()
                .withUserName("test-user-" + userNumber)
                .withName("New test user " + userNumber)
                .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                .withSuperpower("Superpower " + userNumber)
                .withAdmin(false)
                .withDateOfBirth("2019-10-06")
                .withPassword("waestestpass");

        Response res = waesHeroesAPIs.postSignUp(userToDelete.toJson());

        // Validate it was created properly and update 'id' field.
        Assert.assertEquals(res.getStatusCode(), 201);

        JsonPath returnedBody = new JsonPath(res.asString());
        userToDelete.withId(Integer.parseInt(returnedBody.get("id").toString()));
    }

    /**
     * TC: deletePositive
     *
     * step 1: Create new user for removing (performed in BeforeClass).
     * step 2: Perform delete user call
     * step 3: Validate user was removed successfully.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void deletePositive(){
        //step 1: Create new user for removing (performed in BeforeClass).

        //step 2: Perform delete user call
        Response res = waesHeroesAPIs.deleteUser(userToDelete.getUserName(), userToDelete.getPassword(), userToDelete.toJson());

        //step 3: Validate user was removed successfully.
        Assert.assertEquals(res.getStatusCode(), 200);

        Assert.assertEquals(res.asString(), "User '" + userToDelete.getUserName() + "' removed from database.");
    }

    /**
     * TC: deleteNonExistingUser
     *
     * step 1: Build not created user.
     * step 2: Perform delete user call with GOOD Credentials but NON-EXISTING-USER
     * step 3: Validate proper code and error messages are received.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void deleteNonExistingUser(){
        //step 1: Build not created user.
        int userNumber = new Random().nextInt(1000);
        User nonExistingUser = new User()
                .withUserName("test-user-" + userNumber)
                .withName("New test user " + userNumber)
                .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                .withSuperpower("Superpower " + userNumber)
                .withAdmin(false)
                .withDateOfBirth("2019-10-06")
                .withPassword("waestestpass");


        //step 2: Perform delete user call with GOOD Credentials but NON-EXISTING-USER
        Response res = waesHeroesAPIs.deleteUser(userToDelete.getUserName(), userToDelete.getPassword(), nonExistingUser.toJson());

        //step 3: Validate proper code and error messages are received.
        Assert.assertEquals(res.getStatusCode(), 404);

        JsonPath returnedBody = new JsonPath(res.asString());

        Assert.assertEquals(returnedBody.get("status"), 404);
        Assert.assertEquals(returnedBody.get("error"), "Not Found");
        Assert.assertEquals(returnedBody.get("message"), "Username " + nonExistingUser.getUserName() + " does not exist.");
    }

    /**
     * TC: deleteInvalidAuth
     *
     * step 1: Build not created user.
     * step 2: Perform delete user call with BAD Credentials from NON-EXISTING-USER
     * step 3: Validate proper code and error messages are received.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void deleteInvalidAuth(){
        //step 1: Build not created user.
        int userNumber = new Random().nextInt(1000);
        User nonExistingUser = new User()
                .withUserName("test-user-" + userNumber)
                .withName("New test user " + userNumber)
                .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                .withSuperpower("Superpower " + userNumber)
                .withAdmin(false)
                .withDateOfBirth("2019-10-06")
                .withPassword("waestestpass");


        //step 2: Perform delete user call with BAD Credentials from NON-EXISTING-USER
        Response res = waesHeroesAPIs.deleteUser(nonExistingUser.getUserName(), nonExistingUser.getPassword(), userToDelete.toJson());

        //step 3: Validate proper code and error messages are received.
        Assert.assertEquals(res.getStatusCode(), 401);

        JsonPath returnedBody = new JsonPath(res.asString());

        Assert.assertEquals(returnedBody.get("status"), 401);
        Assert.assertEquals(returnedBody.get("error"), "Unauthorized");
        Assert.assertEquals(returnedBody.get("message"), "Bad credentials");
    }
}
