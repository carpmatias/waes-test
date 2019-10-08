# waes-test
test-solution repository for waes-heroes website apis

INSTRUCTIONS

1)	GET SOLUTION
Clone repository


2)	EXECUTE TESTS
Tests can be executed grouping 3 different sets of tests. 
 In project root folder, execute the following:

*Smoke: Execute the main positive test case for each provided API.

                 mvn -PSmoke test


*Regression: Execute the main positive test case for each provided API, plus some other functional and integration scenarios.
                 
                 mvn -PRegression test

*Full Regression: Execute the main positive test case for each provided API, plus some other functional and integration scenarios, and different combination of positive/negative scenarios for APIs with payloads in their request (Sing Up and Update user tests).

                 mvn -PFullRegression test



3)	RESULTS
Once executed, results are logged in runtime in the console. Besides a report is generated with the details for each test and charts at \target\surfire-reports\ExtentReport.html

 

 
 
