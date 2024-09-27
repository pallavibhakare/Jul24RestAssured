package test;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BasicRequestTest {

	@Test
	public void login() {
		RestAssured.baseURI = "https://us-central1-qa01-tekarch-accmanager.cloudfunctions.net";
		
		HashMap<String, String> loginPayload = new HashMap<String, String>();
		loginPayload.put("username", "july2024.pallavi@tekarch.com");
		loginPayload.put("password", "Admin123");
		
		//or can be done with jsonObject		
//		JsonObject jsonObjLogin = new JsonObject();
//		jsonObjLogin.addProperty("username", "july2024.pallavi@tekarch.com");
//		jsonObjLogin.addProperty("password", "Admin123");
				
		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Content-Type", "application/json");
		
		//login
		Response res = RestAssured.given().headers(reqHeaders)
						.when().body(loginPayload)
						.post("/login").then().statusCode(201).extract().response();	
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 201);
		
		
		//extracting token
		String token = res.jsonPath().getString("token").replace("[", "").replace("]", "");
		System.out.println("Token:"+token);
		
		//get data
		System.out.println("Get Data Request");
		Response data = RestAssured.given()
				.header("token", token)
				.when().get("/getdata");
		System.out.println(data.asPrettyString());
		
		//add Data GUYtpneFqys7YrpVWDVS
		System.out.println("Add Data Request");
		Response addData = RestAssured.given().contentType(ContentType.JSON)
				.header("token", token).when()
				.body("{\"accountno\": \"TA-1224331\", \"departmentno\": \"1\", \"salary\": \"3\", \"pincode\": \"323232\"}")
				.post("/addData");
		System.out.println(addData.asPrettyString());
		
		//update Data GUYtpneFqys7YrpVWDVS
		System.out.println("Update Request");
		Response updateData = RestAssured.given().contentType(ContentType.JSON)
						.header("token", token).when()
						.body("{\"accountno\":\"TA-2065192\",\r\n"
								+ " \"departmentno\":\"1\", \r\n"
								+ " \"salary\":\"7654321\", \r\n"
								+ " \"id\":\"50uOxNG6ds5udmqb1Fi2\",\r\n"
								+ " \"pincode\":\"654321\",\r\n"
								+ " \"salary\":\"7654321\",\r\n"
								+ "\"userid\":\"GUYtpneFqys7YrpVWDVS\"}")
						.put("/updateData");
		System.out.println(updateData.asPrettyString());
		
		//Delete Data Request
		System.out.println("Delete Data Request");
		Response deleteData = RestAssured.given().contentType(ContentType.JSON)
				.header("token", token).when()
				.body("{\"userid\": \"GUYtpneFqys7YrpVWDVS\",\r\n"
						+ "\"id\": \"QP33KlA12HFgxs23pcm2\"}")
				.delete("/deleteData");
System.out.println(deleteData.asPrettyString());

//logout Request
		System.out.println("Logout Request");
		Response logoutReq = RestAssured.given().contentType(ContentType.JSON)
				.header("token", token).when()
				.post("/logout");
System.out.println(logoutReq.asPrettyString());
	}
}
