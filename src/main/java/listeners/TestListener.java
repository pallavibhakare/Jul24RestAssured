package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;
import test.BaseTest;


public class TestListener implements ITestListener{
	public void onTestStart(ITestResult result) {
		BaseTest.test.get().log(Status.INFO, result.getName()+" STARTED.");
	}
	
	public void onTestSuccess(ITestResult result) {		
		BaseTest.test.get().log(Status.PASS, result.getName()+" test PASSED.");	
	}
	
	public void onTestFailure(ITestResult result) {		
		BaseTest.test.get().log(Status.FAIL, result.getName()+" FAILED.");	
	}
}
