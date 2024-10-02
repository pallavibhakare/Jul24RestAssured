package constants;

import utils.ScreenshotUtils;

public class FileConstants {
	
	public static String ROOT_PATH = System.getProperty("user.dir");
	
	public static final String ENV_CONFIG_FILE_PATH = ROOT_PATH +
			"/src/main/java/config/envConfig.json";
	
	public static final String LOGIN_TEST_DATA_FILE_PATH = ROOT_PATH +
			"/src/main/java/testdata/logintestdata.json";
	public static final String LOGINCREDS_TEST_DATA_FILE_PATH = ROOT_PATH +
			"/src/main/java/testdata/loginCreds.json";
	public static final String USERS_TEST_DATA_FILE_PATH = ROOT_PATH +
			"/src/main/java/testdata/users.json";
	
	public static final String LOGIN_RESPONSE_SCHEMA_FILE_PATH = ROOT_PATH +
			"/src/main/java/schema/loginResponseSchema.json";

	public static final String REPORTS_FILE_PATH = ROOT_PATH +
			"/src/main/resources/reports/Report"+ScreenshotUtils.getTimeStamp()+".html";
	public static final String SCREENSHOT_FOLDER_PATH = ROOT_PATH +
			"/src/main/resources/reports/SS"+ScreenshotUtils.getTimeStamp()+".png";
	
}
