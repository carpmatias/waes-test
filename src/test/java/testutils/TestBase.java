package testutils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import utils.WaesProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.ITest;
import org.testng.ITestContext;
import utils.WaesHeroesAPIs;

import java.io.IOException;
import java.lang.reflect.Method;

//@Listeners(testutils.ExtentReporterNG.class)

public class TestBase implements ITest {

    protected WaesProperties waesProperties;
    protected WaesHeroesAPIs waesHeroesAPIs;
    protected TestCasesData dataManager;

    public static ExtentTest test;
    public static ExtentReports extent;

    protected static Logger logger = LogManager.getLogger(TestBase.class.getName());

    private ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeSuite
    public void before() {
        extent = new ExtentReports("target\\surefire-reports\\ExtentReport.html", true);
    }

    @BeforeClass
    public void setProperties() throws IOException, InvalidFormatException {
        // properties
        waesProperties = new WaesProperties();

        // Waes heroes apis
        waesHeroesAPIs = new WaesHeroesAPIs(waesProperties.baseUrl);

        //Data manager
        dataManager = new TestCasesData();
    }

    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData, ITestContext ctx){
        String testNameStr = "";

        if (testData.length > 0) {
            testNameStr = method.getName() + "_" + testData[0];
            testName.set(method.getName() + "_" + testData[0]);

        } else
            testNameStr = method.getName();

        ctx.setAttribute("testName", testNameStr);
        test = extent.startTest(testNameStr,testNameStr);

        logger.info("RUNNING TEST: *** " + testNameStr + " ***");
        reportLog("RUNNING TEST: *** " + testNameStr + " ***");
    }

    @AfterMethod
    public void endTest(ITestResult result){
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(LogStatus.FAIL, result.getName());
            test.log(LogStatus.FAIL,result.getThrowable());
            logger.info("TEST " + result.getName() + ": *** FAILED ***\n\n");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(LogStatus.PASS, result.getName());
            logger.info("TEST " + result.getName() + ": *** PASSED ***\n\n");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(LogStatus.SKIP,"Test Case : " + result.getName() + " has been skipped");
            logger.info("TEST " + result.getName() + ": *** SKIPPED ***\n\n");
        }
        extent.endTest(test);
    }

    @Override
    public String getTestName(){
        return testName.get();
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
        extent.close();
    }

    public void reportLog(String message) {
        test.log(LogStatus.INFO, message);
        logger.info("Message: " + message);
        Reporter.log(message);

    }

}

