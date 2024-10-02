package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import listeners.TestListener;
import testdata.AddUserPojo;
import testdata.DeserializeExample;
import testdata.UpdateUserDataPojo;
import utils.DataUtils;
import utils.RestUtils;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

@Listeners(TestListener.class)
public class LoginTest123 extends BaseTest{
	
	private String token;
	private String userid;
	private String id;
	
	@Test
	public void loginRequestTest_TC01() throws IOException {
		logger.info(" loginRequestTest_TC01 ");
		
		// 1. getting uri fron BeforeTest from BasetTest as RestAssured.baseURI
		// 2.getiing endpoint fron json file
		String env = DataUtils.readJsonFileToString(FileConstants.ENV_CONFIG_FILE_PATH);		
		String endpoint = JsonPath.read(env, "$.prod.endpoints.login");
//		String endpoint= DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.login");
		
		// 3.Setting headers
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		
		// 4. body as login credentials
		String creds = DataUtils.readJsonFileToString(FileConstants.LOGINCREDS_TEST_DATA_FILE_PATH);
		HashMap<String, String> credentials = new HashMap<String, String>();
		credentials.put("username", JsonPath.read(creds, "$.prod.valid.username"));
		credentials.put("password", JsonPath.read(creds, "$.prod.valid.password"));
				
		// 5. posting request for login which will give response as token n userid
		Response loginRes = RestUtils.resPost(RestAssured.baseURI + endpoint, headers, credentials);
		System.out.println(loginRes.prettyPrint());	
		
		// 6. validate response we receiving is containing valid data
		RestUtils.validateSchema(loginRes, FileConstants.LOGIN_RESPONSE_SCHEMA_FILE_PATH);
		
		//7. validation using hamcrest, note to add two static imports
		int statusCode = loginRes.statusCode();
//		System.out.println("code"+statusCode);  //201
		assertThat("Expecting 201", 201 == loginRes.getStatusCode());
		logger.info("loginRequestTest_TC01 : "+loginRes.getStatusLine());	
		test.get().info("Logged into the Application.");	
		
		// Store the token from the response
        token = loginRes.jsonPath().getString("token[0]");
        userid = loginRes.jsonPath().getString("userid[0]");
//        System.out.println("token"+token);
//        System.out.println("userid"+userid);
	}
	
	@Test(dependsOnMethods = "loginRequestTest_TC01")
	public void addData_TC02() throws IOException {
		
		// 1. To create user data 
		AddUserPojo userInfo = new AddUserPojo("TA-1212333", "4", "123456", "678901");
		
		// 2.object instance 'om' is created to convert java objects to JSON string
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		// writeValueAsString converts useInfo object into JSON string(payload)
		String payload = om.writeValueAsString(userInfo);
		//System.out.println(payload);
		
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.addData");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", token);
		
		Response getDataReq = RestUtils.resPost(RestAssured.baseURI + endPoint, headers, payload);
//		System.out.println(getDataReq.prettyPrint());
//		System.out.println(getDataReq.getStatusLine());
		getDataReq.then().assertThat().statusCode(201);
		logger.info("addData_TC02 : "+getDataReq.getStatusLine());	
		test.get().info("Data is added.");		
	}
	
	@Test(dependsOnMethods = "loginRequestTest_TC01")
	public void getUsersData_TC03() throws IOException {
		// 1. endpoint
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.getdata");

		// 2.headers with token
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", token);
		
		// 3. get request (endpoint and headers with token)	
		Response getDataReq = RestUtils.resGet(RestAssured.baseURI + endPoint, headers);
		
		ObjectMapper om = new ObjectMapper();	
		String getUserInfo = om.writeValueAsString(getDataReq.jsonPath().get("[0]"));
		//System.out.println(getUserInfo);
		//{"accountno":"TA-1212333","departmentno":"4","salary":"123456","pincode":"678901","userid":"GUYtpneFqys7YrpVWDVS","id":"1k2Ph5MIIKVyuJlTBG6G"}
		
		DeserializeExample ds = om.readValue(getUserInfo, DeserializeExample.class);		
		
//		Assert.assertEquals(getDataReq.statusCode(), 200); //assertion using testNG
		getDataReq.then().assertThat().statusCode(200);   //assertion using hamcrest
//		assertThat("Expecting 200", 200 == getDataReq.getStatusCode());
		logger.info("getUsersData_TC03 : "+getDataReq.getStatusLine());	
		test.get().info("Data is dispayed with status "+getDataReq.getStatusLine());	
		id = JsonPath.read(getUserInfo, "$.id");
		System.out.println("Extracted ID: " + id);
	}
	
	@Test(dependsOnMethods = "loginRequestTest_TC01")
	public void updateData_TC04() throws IOException {
		
		// 1. extratced userid and record id to update record
		String userId = userid;
		String recordId=id;
		
		
		// 2. endpoint
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.updatedata");
		
		// 3.headers with token
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", token);
		
		// 4. update payload : Create an instance of DeserializeExample for the update
		UpdateUserDataPojo updateUserInfo = new UpdateUserDataPojo("TA-1212121", "4", "6543217", "678901", userid, recordId);
		//UpdateUserDataPojo is java object that I want serialize to JSON
		//convert the updated object to json
		ObjectMapper om = new ObjectMapper();
		String updatedPayload = om.writeValueAsString(updateUserInfo);		
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.uri").toString();
		
		// 5. get request (endpoint and headers with token, payload to update)	
		Response updateResponse = RestUtils.resPut(RestAssured.baseURI + endPoint, headers, updatedPayload);

		
		//log the response
//		System.out.println(updateResponse.asPrettyString());
		
		//assert
//		System.out.println("Status line: "+updateResponse.getStatusLine());
		updateResponse.then().assertThat().statusCode(200);
		
		//Retrieve updated data
		String endPointForGet = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.getdata");
		Response getUpdatedDataReq = RestUtils.resGet(RestAssured.baseURI + endPointForGet, headers);
//		System.out.println(getUpdatedDataReq.asPrettyString());
		
		//Deserialize the response to check the updates fields
		String updatedUserInfoJson = getUpdatedDataReq.asString();
		//System.out.println("updatedUserInfoJson"+updatedUserInfoJson);
		//updatedUserInfoJson[{"accountno":"TA-1212121","departmentno":"4","salary":"6543217","pincode":"678901","userid":"GUYtpneFqys7YrpVWDVS","id":"RbLp7x9zd46wnAlVFQ4l"},...]
		List<UpdateUserDataPojo> updatedUserInfoResponseList = om.readValue(updatedUserInfoJson, new TypeReference<List<UpdateUserDataPojo>>() {});

        // Assuming you want to verify the first item in the list
        UpdateUserDataPojo updatedUserInfoResponse = updatedUserInfoResponseList.get(0);
        
        // Verify the updated fields
        assertEquals("TA-1212121", updatedUserInfoResponse.getAccountno());
        assertEquals("6543217", updatedUserInfoResponse.getSalary());
		logger.info("updateData_TC04 : "+updateResponse.getStatusCode());	
		test.get().info("Data is updates with status "+updateResponse.getStatusLine());	
	}

	@Test(dependsOnMethods = "loginRequestTest_TC01")
	public void deleteData_TC05() throws IOException {
		
		// 1. extratced userid and record id to update record
		String userId = userid;
		String recordId=id;
		
		
		// 2. endpoint
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.deletedata");
		
		// 3.headers with token
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", token);
		
		// 4.payload with userid and recordId
				HashMap<String, String> payload = new HashMap<String, String>();
				payload.put("userid", userId);
				payload.put("id", id);
		
		// 5. delete req
		Response deleteDataReq = RestUtils.resDelete(RestAssured.baseURI + endPoint, headers, payload);
		System.out.println("Status line for delete req: "+deleteDataReq.getStatusLine());
		deleteDataReq.then().assertThat().statusCode(200);
		
	}
//	@Test(dependsOnMethods = "loginRequestTest_TC01")
	public void logout_TC06() throws IOException {
				
		// 1. endpoint
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.logout");
		
		// 2.headers with token
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", token);
				
		// 3. logout req
		Response logoutReq = RestUtils.resPost(RestAssured.baseURI + endPoint, headers);
		System.out.println("logiut res "+logoutReq.asPrettyString());
		System.out.println("Status line for logout: "+logoutReq.getStatusLine());
		logoutReq.then().assertThat().statusCode(200);
		
	}
	
}
