package waesqa;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import entities.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testutils.TestBase;

import java.util.Random;

public class LoginTest extends TestBase {

    private User loginUser;

    @BeforeClass
    public void beforeClass(){
        //existing user to use in login tests
        loginUser = new User()
                .withUserName("tester")
                .withName("Al Skept-Cal Tester")
                .withEmail("as.tester@wearewaes.com")
                .withSuperpower("Voltage AND Current.")
                .withAdmin(false)
                .withDateOfBirth("2014-07-15")
                .withId(3);
    }

    /**
     * TC: loginPositive
     *
     * step 1: Perform GET login call with existing username and password.
     * step 2: Validate user logged in succesffully, and response codes and messages are the expected.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void loginPositive(){
        //step 1: Perform GET login call with existing username and password.
        Response res = waesHeroesAPIs.getLogin(waesProperties.loginUsername, waesProperties.loginPassword);

        //step 1: Validate user logged in succesffully, and response codes and messages are the expected.
        Assert.assertEquals(res.getStatusCode(),200);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("id"), loginUser.getId());
        Assert.assertEquals(returnedBody.get("username"), loginUser.getUserName());
        Assert.assertEquals(returnedBody.get("name"), loginUser.getName());
        Assert.assertEquals(returnedBody.get("email"), loginUser.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), loginUser.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), loginUser.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), loginUser.getSuperpower());
    }

    /**
     * TC: createUserAndLogin
     *
     * step 1: Create new user.
     * step 2: Validate user was created successfully
     * step 3: Perform GET login call with the new user credentials.
     * step 4: Validate user logged in succesffully, and response codes and messages are the expected.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void createUserAndLogin(){
        // step 1: Create new user.
        int userNumber = new Random().nextInt(1000);
        User userToCreate = new User();
        userToCreate.withUserName("test-user-" + userNumber)
                    .withName("New test user " + userNumber)
                    .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                    .withSuperpower("Henki dama")
                    .withAdmin(false)
                    .withDateOfBirth("2019-10-06")
                    .withPassword("waestestpass");
        Response res = waesHeroesAPIs.postSignUp(userToCreate.toJson());

        // step 2: Validate user was created successfully
        Assert.assertEquals(res.getStatusCode(),201);

        // step 3: Perform GET login call with the new user credentials.
        res = waesHeroesAPIs.getLogin(userToCreate.getUserName(), userToCreate.getPassword());

        // step 4: Validate user logged in successfully, and response codes and messages are the expected.
        Assert.assertEquals(res.getStatusCode(),200);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("username"), userToCreate.getUserName());
        Assert.assertEquals(returnedBody.get("name"), userToCreate.getName());
        Assert.assertEquals(returnedBody.get("email"), userToCreate.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), userToCreate.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), userToCreate.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), userToCreate.getSuperpower());
    }

    /**
     * TC: getLoginInvalidAuth
     *
     * step 1: Attempt to login with invalid credentials
     * step 2: Validate login fails, and response code and message received is the expected.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void getLoginInvalidAuth(){
        // step 1: Attempt to login with invalid credentials
        Response res = waesHeroesAPIs.getLogin("invalid-user", "invalid-pass");

        // step 2: Validate login fails, and response code and message received is the expected.
        Assert.assertEquals(res.getStatusCode(),401);

        JsonPath returnedBody = new JsonPath(res.asString());

        Assert.assertEquals(returnedBody.get("status"), 401);
        Assert.assertEquals(returnedBody.get("error"), "Unauthorized");
        Assert.assertEquals(returnedBody.get("message"), "Bad credentials");
    }


}
