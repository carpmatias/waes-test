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

public class SignUpTest extends TestBase {

    private User userToCreate;

    @BeforeClass
    public void beforeClass(){
        int userNumber = new Random().nextInt(1000);
        userToCreate = new User();
        userToCreate.withUserName("test-user-" + userNumber)
                    .withName("New test user " + userNumber)
                    .withEmail("newtestuser" + userNumber + "@wearewaes.com")
                    .withSuperpower("Henki dama")
                    .withAdmin(false)
                    .withDateOfBirth("2019-10-06")
                    .withPassword("waestestpass");
    }

    /**
     * TC: signUpPositive
     *
     * step 1: Perform POST signup user with valid new-user payload.
     * step 2: Validate user is created successfully, and response code and message is the expected.
     * step 3: Perform GET User call for created user.
     * step 4: Validate user is retrieved successfully.
     *
     * @author Matías Cárdenas
     */
    @Test
    public void signUpPositive(){
        reportLog("step 1: Perform POST signup user with valid new-user payload.");
        Response res = waesHeroesAPIs.postSignUp(userToCreate.toJson());

        reportLog("step 2: Validate user is created successfully, and response code and message is the expected.");
        Assert.assertEquals(res.getStatusCode(),201);

        JsonPath returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("username"), userToCreate.getUserName());
        Assert.assertEquals(returnedBody.get("name"), userToCreate.getName());
        Assert.assertEquals(returnedBody.get("email"), userToCreate.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), userToCreate.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), userToCreate.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), userToCreate.getSuperpower());

        reportLog("step 3: Perform GET User call for created user.");
        res = waesHeroesAPIs.getUser(userToCreate.getUserName());

        reportLog("step 4: Validate user is retrieved successfully.");
        Assert.assertEquals(res.getStatusCode(),200);

        returnedBody = res.jsonPath();
        Assert.assertEquals(returnedBody.get("username"), userToCreate.getUserName());
        Assert.assertEquals(returnedBody.get("name"), userToCreate.getName());
        Assert.assertEquals(returnedBody.get("email"), userToCreate.getEmail());
        Assert.assertEquals(returnedBody.get("dateOfBirth"), userToCreate.getDateOfBirth());
        Assert.assertEquals(returnedBody.get("isAdmin"), userToCreate.isAdmin());
        Assert.assertEquals(returnedBody.get("superpower"), userToCreate.getSuperpower());
    }

    /**
     * TC: signUpRegressionCases
     *
     * This test covers many (and expandable) different scenarios from an configured Excel file
     * where payload is formed with filled, empty or missing fields according to the scenario we
     * want to test.
     *
     * step 1: Perform POST sign up call with parametrized payload
     * step 2: Validate response code and message is the expected (if necessary).
     *
     * @author Matías Cárdenas
     */
    @Test(dataProvider = "signUpData")
    public void signUpRegressionCases(String scenario, JSONObject requestPayload, int expectedCode) throws InterruptedException {
        reportLog("step 1: Perform POST sign up call with parametrized payload");
        Response res = waesHeroesAPIs.postSignUp(requestPayload);

        reportLog("step 2: Validate response code and message is the expected (if necessary).");
        Assert.assertEquals(res.getStatusCode(),expectedCode);

        //If we expected to success, validate response payload.
        if(expectedCode==201) {
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
        } else if(expectedCode==400) {
            JsonPath returnedBody = res.jsonPath();

            Assert.assertEquals(returnedBody.get("status"), 400);
            Assert.assertEquals(returnedBody.get("error"), "Bad Request");
        }

        // Realized some calls return 403 very often is there's not at least one second between attempts.
        Thread.sleep(1500);
    }

    @DataProvider(name = "signUpData")
    public Object[][] testData() throws IOException, InvalidFormatException {

        Object[][] retrievedData = dataManager.getTestData("SignUpTest");

        Object[][] testData = new Object[retrievedData.length][3];

        for (int row = 0; row < retrievedData.length; row++){
            JSONObject payload = new JSONObject();
            for(int col = 0; col < retrievedData[row].length; col++){
                  if(col == 0)
                      testData[row][col] = retrievedData[row][col];

                  if(col > 0 && col < 9){
                      if(!retrievedData[row][col].toString().equals("MISSING"))
                          payload.put(WaesUtils.getKeyForIndex(col), retrievedData[row][col].toString().equals("EMPTY") ? "" : WaesUtils.getDefaultKeyValue(WaesUtils.getKeyForIndex(col)));
                  }

                  if(col == 9) {
                      testData[row][1] = payload;
                      testData[row][2] = Integer.parseInt(retrievedData[row][col].toString());
                  }
            }
        }

        return testData;
    }

}
