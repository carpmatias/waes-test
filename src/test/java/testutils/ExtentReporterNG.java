package testutils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;
 
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterNG implements IReporter, ITestListener {
    private ExtentReports extent;
    private ExtentTest extentTest;

    private static Logger logger = LogManager.getLogger(TestBase.class.getName());
    ExtentTest test;

    public void onTestSuccess(ITestResult result) {
        logger.info("TEST " +  result.getMethod().getMethodName() + ":  PASSED ");
        extentTest.log(LogStatus.PASS, result.getMethod().getMethodName());
    }

    public void onTestFailure(ITestResult result) {
        logger.info("TEST " +  result.getMethod().getMethodName() + " : **** FAILED ***");
        extentTest.log(LogStatus.FAIL, result.getMethod().getMethodName());
    }


    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        extent = new ExtentReports(outputDirectory + File.separator + "ExtentReportsTestNG.html", true);
 
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
 
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
 
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
 
        extent.flush();
        extent.close();
    }
 
    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;
 
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.startTest(result.getMethod().getMethodName());
                //test = extent.startTest(result.getTestContext().getAttribute("testName").toString());
 
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
 
                String message = "Test " + status.toString().toLowerCase() + "ed";
 
                if (result.getThrowable() != null)
                    message = result.getThrowable().getMessage();

                test.log(status, result.getTestContext().getAttribute("testName").toString());
                test.log(status, message);
 
                extent.endTest(test);
            }
        }
    }
 
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();        
    }
}