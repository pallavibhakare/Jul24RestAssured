package test;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import listeners.TestListener;
import testdata.AddUserPojo;
import testdata.DeserializeExample;
import utils.DataUtils;
import utils.RestUtils;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Listeners(TestListener.class)
public class LoginTest extends BaseTest{
	
	private String userid;
	private String id;
//	@Test
	public void login() throws IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		String env = DataUtils.readJsonFileToString(FileConstants.ENV_CONFIG_FILE_PATH);
		String endpoint = JsonPath.read(env, "$.prod.endpoints.login");
		String creds = DataUtils.readJsonFileToString(FileConstants.LOGINCREDS_TEST_DATA_FILE_PATH);
		HashMap<String, String> payload = new HashMap<String, String>();
		payload.put("username", JsonPath.read(creds, "$.prod.valid.username"));
		payload.put("password", JsonPath.read(creds, "$.prod.valid.password"));
		Response res = RestUtils.resPost(RestAssured.baseURI + endpoint, headers, payload);
		assertThat("Expecting 201", 201 == res.getStatusCode());
		RestUtils.validateSchema(res, FileConstants.LOGIN_RESPONSE_SCHEMA_FILE_PATH);
		System.out.println(res.prettyPrint());						
	}

	@Test
	public void addData_TC02() throws IOException {
		
		//Serialization :Converting an object(userInfo) into a format (like JSON) for storage or transmission.
		AddUserPojo userInfo = new AddUserPojo("TA-1212333", "1", "123456", "678901");		
		ObjectMapper om = new ObjectMapper();    //instance of ObjectMapper is responsible for converting between Java objects and JSON. 
		om.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		//Below convert userInfo 'java object' -> JSON string
		//writeValueAsString() method takes the userInfo object and produces a JSON representation of it.
		String payload = om.writeValueAsString(userInfo);
//		System.out.println("payload "+payload);	//{"accountno":"TA-1212333","departmentno":"4","salary":"123456","pincode":"678901"}
		
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.addData");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", generateToken());
		
		Response res = RestUtils.resPost(RestAssured.baseURI + endPoint, headers, payload);
	
		res.then().assertThat().statusCode(201);
	}

	@Test
	public void getUsers_TC03() throws IOException {
	 
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.getdata");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", generateToken());
		
		Response res = RestUtils.resGet(RestAssured.baseURI + endPoint, headers);
		
		ObjectMapper om = new ObjectMapper();   //who is used for conversion between java objects and json
		String getUserInfo = om.writeValueAsString(res.jsonPath().get("[0]"));  //fetches 1st element from a JSON responce-> reprtived JSON object is  serialized into JSON string
//		below line converts the JSON string (getUserInfo) back into a Java object of type DeserializeExample.
		DeserializeExample ds = om.readValue(getUserInfo, DeserializeExample.class);
//		System.out.println("getUserInfo"+getUserInfo); 
		
		res.then().assertThat().statusCode(200);
		
		userid = res.jsonPath().getString("userid[0]");
		id = JsonPath.read(getUserInfo, "$.id");
		System.out.println("Extracted userId: " + userid);
		System.out.println("Extracted recordID: " + id);
	}
	@Test
	public void getData_TC04() throws IOException {
	 
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.getdata");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", generateToken());
		
		Response getDataResponse = RestUtils.resGet(RestAssured.baseURI + endPoint, headers);		 
//		System.out.println(getDataResponse.asPrettyString());
		getDataResponse.then().assertThat().statusCode(200);
		
	}
	@Test
	public void updateData_TC05() throws IOException {
	 
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.updatedata");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", generateToken());
		
		DeserializeExample updateUserInfo = new DeserializeExample("TA-2121212", "4", "6543217", "678901", userid, id);
		ObjectMapper om = new ObjectMapper();
		String updatedPayload = om.writeValueAsString(updateUserInfo);
		
		
		Response updateRes = RestUtils.resPut(RestAssured.baseURI + endPoint, headers, updatedPayload);		 
//		System.out.println(res.asPrettyString());
		updateRes.then().assertThat().statusCode(200);
	}
	
	@Test(dependsOnMethods = "getUsers_TC03")
	public void deleteData_TC06() throws IOException {
	 
		// extratced userid and record id for update record
		String userId = userid;
		String recordId=id;
//		System.out.println("userId "+userId);	
//		System.out.println("recordId "+recordId);	
		
		String endPoint = DataUtils.getTestData(FileConstants.ENV_CONFIG_FILE_PATH, "$.prod.endpoints.deletedata");
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", generateToken());
		
		HashMap<String, String> payload = new HashMap<String, String>();
		payload.put("userid", userId);
		payload.put("id", recordId);
						
		Response deleteDataRes = RestUtils.resDelete(RestAssured.baseURI + endPoint, headers, payload);		 
		deleteDataRes.then().assertThat().statusCode(200);
	}
	
}
