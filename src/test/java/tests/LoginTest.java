package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Covers the combined signup/login page and the happy-path session header.
 */
public class LoginTest extends BaseTest {

    private static final String VALID_EMAIL = "test@saffiya.com";
    private static final String VALID_PASSWORD = "Test1234!";

    @Test
    public void validLoginTest() {
        // Land on the dedicated login route so we know we are not on a cached page
        driver.get(BASE_URL + "/login");

        // Wait until the login card is ready, then type into the left-hand form only (there is also an email field on signup)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.login-form")));
        driver.findElement(By.cssSelector("div.login-form input[name='email']")).clear();
        driver.findElement(By.cssSelector("div.login-form input[name='email']")).sendKeys(VALID_EMAIL);
        driver.findElement(By.cssSelector("div.login-form input[name='password']")).clear();
        driver.findElement(By.cssSelector("div.login-form input[name='password']")).sendKeys(VALID_PASSWORD);

        // Submit the credentials
        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        // Successful auth swaps the header link for Logout on this demo site
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='/logout']")));
        Assert.assertTrue(driver.findElement(By.cssSelector("a[href='/logout']")).isDisplayed(), "Logout should appear after a valid login.");
    }

    @Test
    public void invalidLoginTest() {
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.login-form")));
        driver.findElement(By.cssSelector("div.login-form input[name='email']")).sendKeys("wrong@example.com");
        driver.findElement(By.cssSelector("div.login-form input[name='password']")).sendKeys("WrongPassword!");

        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        // The site surfaces a red inline paragraph when credentials do not match any account
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.login-form p[style*='color: red']")));
        String message = driver.findElement(By.cssSelector("div.login-form p[style*='color: red']")).getText();
        Assert.assertEquals(message, "Your email or password is incorrect!");
    }
}
