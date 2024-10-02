package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.jayway.jsonpath.JsonPath;



public class DataUtils {
	public static String readJsonFileToString(String filePath) throws IOException {
		byte[] data = Files.readAllBytes(Paths.get(filePath));
		return new String(data, StandardCharsets.UTF_8);		
	}
	public static String getTestData(String filePath, String jsonPath) throws IOException {
		String testData = DataUtils.readJsonFileToString(filePath);
		return JsonPath.read(testData, jsonPath).toString();
	}
	
	
}
