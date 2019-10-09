# waes-test
test-solution repository for waes-heroes website apis

*Refer to WaesAutomationSolution.pdf file in root folder of repository for more details and documentation of the assignment.


REQUIREMENTS

•	Java 8 + JDK
•	Maven
•	backend-for-testers  application provided by Waes, must be running.


INSTRUCTIONS

1)	GET SOLUTION
Clone repository


2)	EXECUTE TESTS
Tests can be executed grouping 3 different sets of tests. 
In command line go to project root folder and execute the following, depending on the type of test you want to run:

*Smoke: Execute the main positive test case for each provided API.

                 mvn -PSmoke test

Execution time: ~10 seconds.*

*Regression: Execute the main positive test case for each provided API, plus some other functional and integration scenarios.
                 
                 mvn -PRegression test

Execution time: ~15 seconds.*

*Full Regression: Execute the main positive test case for each provided API, plus some other functional and integration scenarios, and different combination of positive/negative scenarios for APIs with payloads in their request (Sing Up and Update user tests).

                 mvn -PFullRegression test

Execution time: ~1 min.*

3)	RESULTS
Once executed, results are logged in runtime in the console. Besides a report is generated with the details for each test and charts at \target\surfire-reports\ExtentReport.html

 

 
 
