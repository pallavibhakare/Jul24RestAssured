package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import constants.FileConstants;
public class ScreenshotUtils {

	public static String captureScreenshot(WebDriver driver) {
		String filePath = FileConstants.SCREENSHOT_FOLDER_PATH;
		TakesScreenshot page = (TakesScreenshot)driver;
		File screenshotFile = page.getScreenshotAs(OutputType.FILE);
		File dst = new File(filePath);
		screenshotFile.renameTo(dst);		
		return filePath;
	}
	public static String getTimeStamp() {
		return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	}
}
