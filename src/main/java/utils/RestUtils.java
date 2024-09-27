package utils;

import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestUtils {

	public static Response resPost(String baseUri, HashMap<String, String> header, HashMap<String, String> payload) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(header)
						.when().body(payload)
						.post();		
		return res;		
	}
	
	public static Response resPost(String baseUri, HashMap<String, String> header, String payload) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(header)
						.when().body(payload)
						.post();		
		return res;		
	}

	public static Response resGet(String baseUri, HashMap<String, String> header) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(header).when().get();		
		return res;		
	}
	public static Response resPut(String baseUri, HashMap<String, String> header,HashMap<String, String> payload ) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(header)
				.when().body(payload)
				.put();		
		return res;		
	}
	public static Response resDelete(String baseUri, HashMap<String, String> header,HashMap<String, String> payload ) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(header)
				.when().body(payload)
				.delete();		
		return res;		
	}
}
