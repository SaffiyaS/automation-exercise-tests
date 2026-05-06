package tests;

import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Exercises the two-step registration journey (inline signup + account details form).
 */
public class RegisterTest extends BaseTest {

    @Test
    public void registerNewUserTest() {
        driver.get(BASE_URL + "/login");

        // Start on the right-hand "New User Signup" column
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.signup-form")));
        String randomName = "User-" + UUID.randomUUID().toString().substring(0, 8);
        String randomEmail = "ae." + UUID.randomUUID() + "@mailinator.com";

        driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(randomName);
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(randomEmail);
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        // Account information step should expose title radio + address fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_gender1")));
        driver.findElement(By.id("id_gender1")).click();

        driver.findElement(By.cssSelector("input[data-qa='password']")).sendKeys("RegPassw0rd!");

        new Select(driver.findElement(By.cssSelector("select[data-qa='days']"))).selectByValue("10");
        new Select(driver.findElement(By.cssSelector("select[data-qa='months']"))).selectByValue("5");
        new Select(driver.findElement(By.cssSelector("select[data-qa='years']"))).selectByValue("1994");

        driver.findElement(By.cssSelector("input[data-qa='first_name']")).sendKeys("Saffiya");
        driver.findElement(By.cssSelector("input[data-qa='last_name']")).sendKeys("Sanhar");
        driver.findElement(By.cssSelector("input[data-qa='company']")).sendKeys("QA Automation");
        driver.findElement(By.cssSelector("input[data-qa='address']")).sendKeys("221B Baker Street");
        driver.findElement(By.cssSelector("input[data-qa='address2']")).sendKeys("Floor 2");

        new Select(driver.findElement(By.cssSelector("select[data-qa='country']"))).selectByVisibleText("United States");

        driver.findElement(By.cssSelector("input[data-qa='state']")).sendKeys("New York");
        driver.findElement(By.cssSelector("input[data-qa='city']")).sendKeys("New York");
        driver.findElement(By.cssSelector("input[data-qa='zipcode']")).sendKeys("10001");
        driver.findElement(By.cssSelector("input[data-qa='mobile_number']")).sendKeys("2125550199");

        WebElement createAccount = driver.findElement(By.cssSelector("button[data-qa='create-account']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", createAccount);
        createAccount.click();

        // Success screen includes a green banner with the account confirmation copy
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2[data-qa='account-created']")));
        Assert.assertTrue(driver.findElement(By.cssSelector("h2[data-qa='account-created']")).getText().contains("ACCOUNT CREATED!"));
    }

    @Test
    public void registerExistingEmailTest() {
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.signup-form")));
        driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys("Duplicate probe");
        // Reuse the same mailbox the login suite expects so the address is guaranteed to exist server-side
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys("test@saffiya.com");
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.signup-form p[style*='color: red']")));
        String error = driver.findElement(By.cssSelector("div.signup-form p[style*='color: red']")).getText();
        Assert.assertEquals(error, "Email Address already exist!");
    }
}
