package waesqa;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import entities.User;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testutils.TestBase;

import java.util.Random;

public class GetAllUsersTest extends TestBase {

    private User existingUser;

    @BeforeClass
    public void beforeClass(){
        // Create existing user to validate with one received in the collection.
        existingUser = new User()
                .withUserName("dev")
                .withName("Zuper Dooper Dev")
                .withEmail("zd.dev@wearewaes.com")
                .withSuperpower("Debug a repellent factory storage.")
                .withAdmin(false)
                .withDateOfBirth("1999-10-10")
                .withId(2);
    }

    /**
     *
     * TC: getAllUsersPositive
     *
     * step 1: Perform call to retrieve data from all users (using existing admin user).
     * step 2: Validate users are retrieved successfully.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void getAllUsersPositive(){
        reportLog("step 1: Perform call to retrieve data from all users (using existing admin user).");
        Response res = waesHeroesAPIs.getAllUsers(waesProperties.getUsersUsername, waesProperties.getUsersPassword);

        reportLog("step 2: Validate users are retrieved successfully.");
        Assert.assertEquals(res.getStatusCode(),200);

        JsonPath returnedBody = new JsonPath(res.asString());

        reportLog("Validate one user JsonObject and its structure.");
        Assert.assertEquals(returnedBody.get("id[1]"), existingUser.getId());
        Assert.assertEquals(returnedBody.get("username[1]"), existingUser.getUserName());
        Assert.assertEquals(returnedBody.get("name[1]"), existingUser.getName());
        Assert.assertEquals(returnedBody.get("email[1]"), existingUser.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth[1]"), existingUser.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin[1]"), existingUser.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower[1]"), existingUser.getSuperpower());
    }

    /**
     *
     * TC: getAllUsersInvalidAuth
     *
     * step 1: Attempt to retrieve all users with invalid (non-existing) credentials.
     * step 2: Validate proper response code and message is received.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void getAllUsersInvalidAuth(){
        reportLog("step 1: Attempt to retrieve all users with invalid (non-existing) credentials.");
        Response res = waesHeroesAPIs.getAllUsers("invalid-user", "invalid-pass");

        reportLog("step 2: Validate proper response code and message is received.");
        Assert.assertEquals(res.getStatusCode(),401);

        JsonPath returnedBody = new JsonPath(res.asString());

        Assert.assertEquals(returnedBody.get("status"), 401);
        Assert.assertEquals(returnedBody.get("error"), "Unauthorized");
        Assert.assertEquals(returnedBody.get("message"), "Bad credentials");
    }

    /**
     *
     * TC: createNewAdminUserAndGetAllUsers
     *
     * step 1: Attempt to retrieve all users with invalid (non-existing) credentials.
     * step 2: Attempt to retrieve all users with the new user credentials.
     * step 3: Validate proper response code and message is received.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void createNewAdminUserAndGetAllUsers(){
        reportLog("step 1: Attempt to retrieve all users with invalid (non-existing) credentials.");
        int userNumber = new Random().nextInt(1000);
        User userToCreate = new User();
        userToCreate.withUserName("test-user-" + userNumber)
                    .withName("New test user " + userNumber)
                    .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                    .withSuperpower("Henki dama")
                    .withAdmin(true)
                    .withDateOfBirth("2019-10-06")
                    .withPassword("waestestpass");

        Response res = waesHeroesAPIs.postSignUp(userToCreate.toJson());

        Assert.assertEquals(res.getStatusCode(),201);

        reportLog("step 2: Attempt to retrieve all users with the new user credentials.");
        res = waesHeroesAPIs.getAllUsers(userToCreate.getUserName(), userToCreate.getPassword());

        reportLog("step 3: Validate proper response code and message is received.");
        Assert.assertEquals(res.getStatusCode(),403);

        //No quite sure if it would be the expected behavior, considering the user is admin?
        JsonPath returnedBody = new JsonPath(res.asString());

        Assert.assertEquals(returnedBody.get("status"), 403);
        Assert.assertEquals(returnedBody.get("error"), "Forbidden");
        Assert.assertEquals(returnedBody.get("message"), "Forbidden");
    }
}
