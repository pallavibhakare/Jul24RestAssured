package test;

import java.io.IOException;
import java.util.HashMap;

import org.testng.annotations.Test;
import com.jayway.jsonpath.JsonPath;
import constants.FileConstant;
import io.opentelemetry.sdk.metrics.data.Data;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.DataUtils;
import utils.RestUtils;

public class LoginTest extends BaseTest{
	
	@Test
	public void login() throws IOException {
		RestAssured.baseURI = "https://us-central1-qa01-tekarch-accmanager.cloudfunctions.net";
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		
		
		//reading all the contents of the file into variable env
		String env = DataUtils.readJsonFileToString(FileConstant.ENV_CONFIG_FILE_PATH);
		String envURI = JsonPath.read(env, "$.prod.uri");
		System.out.println(envURI);
		String endPoint = JsonPath.read(env, "$.prod.endpoints.login");
		String creds = DataUtils.readJsonFileToString(FileConstant.USERACCOUNTS_TEST_DATA_FILE_PATH);
		
		HashMap<String, String> credentials = new HashMap<String, String>();
		credentials.put("username", JsonPath.read(creds,"$.prod.valid.username"));
		credentials.put("password", JsonPath.read(creds,"$.prod.valid.password"));
		
		Response res = RestUtils.resPost(envURI+endPoint, headers, credentials);
		System.out.println(res.asPrettyString());
	}
	
}
