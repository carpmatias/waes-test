package waesqa;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import entities.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testutils.TestBase;

public class GetUserTest extends TestBase {

    private User existingUser;

    @BeforeClass
    public void beforeClass(){
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
     * TC: getUserPositive
     *
     * step 1: Perform GET User call for an existing user.
     * step 2: Validate user is retrieved successfully.
     *
     */
    @Test
    public void getUserPositive(){
        reportLog("step 1: Perform GET User call for an existing user.");
        Response res = waesHeroesAPIs.getUser("dev");

        reportLog("step 2: Validate user is retrieved successfully.");
        Assert.assertEquals(res.getStatusCode(),200);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("id"), existingUser.getId());
        Assert.assertEquals(returnedBody.get("username"), existingUser.getUserName());
        Assert.assertEquals(returnedBody.get("name"), existingUser.getName());
        Assert.assertEquals(returnedBody.get("email"), existingUser.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), existingUser.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), existingUser.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), existingUser.getSuperpower());
    }

    /**
     * TC: getNonExistingUser
     *
     * step 1: Attempt to perform GET User call passing a non-existing username.
     * step 2: Validate user can't be retrieved and response code and messages are the expected.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void getNonExistingUser(){
        String userName = "non-existing-user";

        reportLog("step 1: Attempt to perform GET User call passing a non-existing username.");
        Response res = waesHeroesAPIs.getUser(userName);


        reportLog("step 2: Validate user can't be retrieved and response code and messages are the expected.");
        Assert.assertEquals(res.getStatusCode(),404);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("status"), 404);
        Assert.assertEquals(returnedBody.get("message"), "Username " + userName + " does not exist.");
    }

    /**
     * TC: getEmptyUser
     *
     * step 1: Attempt to perform GET User call passing a non-existing username.
     * step 2: Validate user can't be retrieved and response code and messages are the expected.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void getEmptyUser(){
        reportLog("step 1: Attempt to perform GET User call passing an empty username.");
        Response res = waesHeroesAPIs.getUser("");

        reportLog("step 2: Validate user can't be retrieved and response code and messages are the expected.");
        Assert.assertEquals(res.getStatusCode(),404);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("status"), 404);
        Assert.assertEquals(returnedBody.get("message"), "Username  does not exist.");
    }
}
