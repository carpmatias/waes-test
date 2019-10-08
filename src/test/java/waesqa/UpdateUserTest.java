package waesqa;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import entities.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testutils.TestBase;
import utils.WaesUtils;

import java.io.IOException;
import java.util.Random;

public class UpdateUserTest extends TestBase {

    private User userToUpdate;

    @BeforeClass
    public void beforeClass(){
        int userNumber = new Random().nextInt(1000);
        userToUpdate = new User()
                .withUserName("test-user-" + userNumber)
                .withName("New test user " + userNumber)
                .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                .withSuperpower("Henki dama")
                .withAdmin(false)
                .withDateOfBirth("2019-10-06")
                .withPassword("waestestpass");

        Response res = waesHeroesAPIs.postSignUp(userToUpdate.toJson());

        Assert.assertEquals(res.getStatusCode(), 201);

        // Retrieve user
        res = waesHeroesAPIs.getUser(userToUpdate.getUserName());

        // Validate user is retrieved successfully.
        Assert.assertEquals(res.getStatusCode(),200);

        // Set id to the user entity.
        JsonPath returnedBody = new JsonPath(res.asString());
        userToUpdate.withId(Integer.parseInt(returnedBody.get("id").toString()));
    }

    /**
     * TC: updateUserPositive
     *
     * step 1: Set new fields values to update user
     * step 2: Perform PUT user call.
     * step 3: Validate user was updated successfully.
     * step 4: Perform GET User call for created user.
     * step 5: Validate user is retrieved successfully with updated fields
     *
     * @author Matías Cárdenas
     */
    @Test
    public void updateUserPositive(){
        reportLog("step 1: Set new fields values to update user");
        userToUpdate.withDateOfBirth("2018-10-06")
                    .withName("New name positive")
                    .withSuperpower("Kaioken");

        reportLog("step 2: Perform PUT user call.");
        Response res = waesHeroesAPIs.putUser(userToUpdate.getUserName(), userToUpdate.getPassword(), userToUpdate.toJson());

        reportLog("step 3: Validate user was updated successfully.");
        Assert.assertEquals(res.getStatusCode(),200);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("username"), userToUpdate.getUserName());
        Assert.assertEquals(returnedBody.get("name"), userToUpdate.getName());
        Assert.assertEquals(returnedBody.get("email"), userToUpdate.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), userToUpdate.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), userToUpdate.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), userToUpdate.getSuperpower());

        reportLog("step 4: Perform GET User call for created user.");
        res = waesHeroesAPIs.getUser(userToUpdate.getUserName());

        reportLog("step 5: Validate user is retrieved successfully with updated fields");
        Assert.assertEquals(res.getStatusCode(),200);

        returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("username"), userToUpdate.getUserName());
        Assert.assertEquals(returnedBody.get("name"), userToUpdate.getName());
        Assert.assertEquals(returnedBody.get("email"), userToUpdate.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), userToUpdate.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), userToUpdate.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), userToUpdate.getSuperpower());
    }

    /**
     * TC: updateUserRegressionCases
     *
     * This test covers many (and expandable) different scenarios from an configured Excel file
     * where payload is formed with filled, empty or missing fields according to the scenario we
     * want to test.
     *
     * step 1: Perform PUT user call with parametrized payload
     * step 2: Validate response code and message is the expected (if necessary).
     *
     * @author Matías Cárdenas
     */
    @Test(dataProvider = "updateUserData")
    public void updateUserRegressionCases(String scenario, JSONObject requestPayload, int expectedCode) throws InterruptedException {
        reportLog("step 1: Perform PUT user call with parametrized payload");
        Response res = waesHeroesAPIs.putUser(userToUpdate.getUserName(),userToUpdate.getPassword(), requestPayload);

        reportLog("step 2: Validate response code and message is the expected (if necessary).");
        Assert.assertEquals(res.getStatusCode(),expectedCode);

        if(expectedCode==200) {
            JsonPath returnedBody = res.jsonPath();
            Assert.assertEquals(returnedBody.get("username"), requestPayload.get("username"));
            if(requestPayload.has("name"))
                Assert.assertEquals(returnedBody.get("name"), requestPayload.getString("name"));
            if(requestPayload.has("email"))
                Assert.assertEquals(returnedBody.get("email"), requestPayload.get("email"));
            if(requestPayload.has("dateOfBirth"))
                Assert.assertEquals(returnedBody.get("dateOfBirth"), requestPayload.get("dateOfBirth").toString().equals("") ? null : requestPayload.get("dateOfBirth"));
            if(requestPayload.has("isAdmin"))
                Assert.assertEquals(returnedBody.get("isAdmin"), requestPayload.get("isAdmin").toString().equals("") ? null : requestPayload.get("isAdmin"));
            if(requestPayload.has("superpower"))
                Assert.assertEquals(returnedBody.get("superpower"), requestPayload.get("superpower"));
        }
        Thread.sleep(1000);
    }

    @DataProvider(name = "updateUserData")
    public Object[][] testData() throws IOException, InvalidFormatException {

        Object[][] retrievedData = dataManager.getTestData("UpdateUserTest");

        Object[][] testData = new Object[retrievedData.length][3];

        for (int row = 0; row < retrievedData.length; row++){
            JSONObject payload = new JSONObject();
            for(int col = 0; col < retrievedData[row].length; col++){
                if(col == 0)
                    testData[row][col] = retrievedData[row][col];

                if(col > 0 && col < 9){
                    if(col == 3)
                        payload.put("username", userToUpdate.getUserName());

                    if(col == 8)
                        payload.put("password", userToUpdate.getPassword());

                    if(!retrievedData[row][col].toString().equals("MISSING"))
                        payload.put(WaesUtils.getKeyForIndex(col), retrievedData[row][col].toString().equals("EMPTY") ? "" : getUserToUpdateKeyValue(WaesUtils.getKeyForIndex(col)));
                }

                if(col == 9) {
                    testData[row][1] = payload;
                    testData[row][2] = Integer.parseInt(retrievedData[row][col].toString());
                }
            }
        }

        return testData;
    }

    public Object getUserToUpdateKeyValue(String key){
        switch(key){
            case "id": return userToUpdate.getId();
            case "name": return userToUpdate.getName();
            case "username": return userToUpdate.getUserName();
            case "superpower": return userToUpdate.getSuperpower();
            case "email": return userToUpdate.getEmail();
            case "dateOfBirth": return userToUpdate.getDateOfBirth();
            case "isAdmin": return userToUpdate.isAdmin();
            case "password": return userToUpdate.getPassword();
            default: return "";
        }
    }

}
