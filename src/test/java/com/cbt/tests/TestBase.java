package com.cbt.tests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.cbt.utilities.BrowserUtils;
import com.cbt.utilities.ConfigurationReader;
import com.cbt.utilities.Driver;

public abstract class TestBase {

	protected WebDriver driver;
	protected Actions action;

	protected ExtentReports report;
	protected ExtentHtmlReporter htmlReporter;
	protected ExtentTest extentLogger;

	@BeforeTest
	public void setUpTest() {
		report = new ExtentReports();

		String filePath = System.getProperty("user.dir") + "/test-output/report.html";

		htmlReporter = new ExtentHtmlReporter(filePath);

		report.attachReporter(htmlReporter);

		report.setSystemInfo("ENV", "staging");

		report.setSystemInfo("browser", ConfigurationReader.getProperty("browser"));

		report.setSystemInfo("OS", System.getProperty("os.name"));

		htmlReporter.config().setReportName("WebOrders Automation Test Reports");

	}

	@Parameters("browser")
	@BeforeMethod
	public void setUp(@Optional String browser) throws InterruptedException {
		driver = Driver.getDriver(browser);
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(ConfigurationReader.getProperty("url"));
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws IOException, InterruptedException{

		if (result.getStatus() == ITestResult.FAILURE) {
			String screenShotLocation = BrowserUtils.getScreenshot(result.getName());
			extentLogger.fail(result.getName());
			extentLogger.addScreenCaptureFromPath(screenShotLocation);
			extentLogger.fail(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentLogger.skip("Test case is skipped" + result.getName());
		}

		Driver.closeDriver();
	}

	@AfterTest
	public void tearDownTest() {
		report.flush();
	}

}
