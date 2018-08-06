package com.cbt.tests.login;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.cbt.pages.AllOrdersPage;
import com.cbt.pages.LoginPage;
import com.cbt.tests.TestBase;
import com.cbt.utilities.BrowserUtils;
import com.cbt.utilities.ConfigurationReader;

public class LoginTests extends TestBase {

	@Test()
	public void positiveloginTest() throws InterruptedException {
		
		extentLogger = report.createTest("positiveLoginTest");
		
		extentLogger.info("entering user credentials");

		driver.findElement(By.id("ctl00_MainContent_username")).sendKeys("Tester");
		driver.findElement(By.id("ctl00_MainContent_password")).sendKeys("test");
		
		extentLogger.info("click login");
		driver.findElement(By.id("ctl00_MainContent_login_button")).click();
		BrowserUtils.waitFor(2);
		
		extentLogger.info("verifying the logout link");
		
		assertTrue(new AllOrdersPage().logoutLink.isDisplayed());
		
		extentLogger.pass("Verifies log out link displayed");
		
	}

	@Test(priority = 2)
	public void positiveLoginUsingPOM() throws InterruptedException {
		extentLogger = report.createTest("positiveLoginTestUsingPom");
		LoginPage loginPage = new LoginPage();
		loginPage.userName.sendKeys(ConfigurationReader.getProperty("username"));
		loginPage.password.sendKeys(ConfigurationReader.getProperty("password"));
		loginPage.loginButton.click();
		fail("");
	}

	@Ignore
	@Test(priority = 1, groups = "functest")
	public void invalidUsernameTest() throws InterruptedException {
		extentLogger = report.createTest("invalidUsernameTest");
		LoginPage loginPage = new LoginPage();
		loginPage.userName.sendKeys("invalid");
		loginPage.password.sendKeys("test");
		loginPage.loginButton.click();
		String errMsg = loginPage.invalidUserNameErrMsg.getText();
		throw new SkipException("");
		
//		assertEquals(errMsg, "Invalid Login or Password.");
		
	}

}
