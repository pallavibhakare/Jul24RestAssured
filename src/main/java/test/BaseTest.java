package test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.DataUtils;
import utils.ReportManager;
import utils.RestUtils;

public class BaseTest{
	
	static String token;
//	protected String token;
	public static Logger logger = LogManager.getLogger("BaseTest");
	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	
	@BeforeMethod
	public void setupBeforeMethod(Method name) throws IOException {
		test.set(extent.createTest(name.getName()));
		token = generateToken();
	}
	
	@BeforeSuite(alwaysRun = true)
	public void getReport() {		
		extent = ReportManager.getInstance();
	    if (extent == null) {
	        logger.error("Failed to initialize ExtentReports instance.");
	    } else {
	        logger.info("ExtentReports instance initialized successfully.");
	    }
	}
	@AfterSuite(alwaysRun = true)
	public void writeReport() {
		
		 extent.flush();
	}
	
	
	@BeforeTest
	public void setup() throws IOException {
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.uri").toString();
	}
	
	public static String generateToken() throws IOException {
		
//		Object payloadStr = DataUtils.getTestData(FileConstants.USERACCOUNTS_TEST_DATA_FILE_PATH, "$.prod.valid");
//		System.out.print(payload);   //{username=july2024.pallavi@tekarch.com, password=Admin123}
	
//		String payload = DataUtils.getTestData(FileConstants.USERACCOUNTS_TEST_DATA_FILE_PATH, "$.prod.valid").toString();
//		System.out.print(payload);	//{username=july2024.pallavi@tekarch.com, password=Admin123}

		String payload = "{\"username\": \"july2024.pallavi@tekarch.com\", \"password\": \"Admin123\"}";		
		
		String uri = RestAssured.baseURI;
		
		String endpoint= DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.login");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		
		if(token == null) {
			Response loginResponse= RestUtils.resPost(uri+endpoint, headers, payload);
			//System.out.println("Generated Response:"+loginResponse.asPrettyString());
			token = loginResponse.jsonPath().get("[0].token");
		}else {
			System.out.println("Token is already generated.");
		}					
		return token;
	}
}
