package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

;public class RestUtils {

	public static Response resPost(String endpoint, HashMap<String, String> header, Object payload) {
	
		Response res = RestAssured.given().headers(header)
						.when().body(payload)
						.post(endpoint);		
		return res;		
	}
	
	public static Response resPost(String endpoint, HashMap<String, String> header, String payload) {
//		System.out.println("resPost: URI :" + RestAssured.baseURI+endpoint);
		RestAssured.useRelaxedHTTPSValidation();
		Response res = RestAssured.given().headers(header).when().body(payload).post(endpoint);
		return res;	
	}

	public static Response resGet(String endpoint, HashMap<String, String> header) {
//		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(header).when().get(endpoint);		
		return res;		
	}
	public static Response resPut(String endpoint, HashMap<String, String> header, String payload) throws IOException {
		
		Response res = RestAssured.given().headers(header).when().body(payload).put(endpoint);
		return res;
	}
	public static Response resDelete(String endpoint, HashMap<String, String> header, HashMap<String, String> payload) {
//		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(header).when().body(payload).delete(endpoint);
		return res;
	}
	public static void validateSchema(Response actualResponse, String schemaFilePath) {
		actualResponse.then().assertThat().body(matchesJsonSchema(new File(schemaFilePath)));
	}

	public static Response resPost(String endpoint, HashMap<String, String> header) {
		Response res = RestAssured.given().headers(header)
				.when()	.post(endpoint);		
		return res;	
	}
	
}
