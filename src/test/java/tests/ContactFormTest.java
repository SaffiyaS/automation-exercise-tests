package tests;

import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Contact us flow validates both the happy path and the browser-enforced constraints.
 */
public class ContactFormTest extends BaseTest {

    @Test
    public void submitContactFormTest() throws Exception {
        driver.get(BASE_URL + "/contact_us");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#contact-us-form")));

        Path attachment = Files.createTempFile("ae-upload-", ".txt");
        Files.writeString(attachment, "Automation upload sample");

        // Populate every visible field plus the optional file picker (name attribute is stable here)
        driver.findElement(By.cssSelector("input[name='name'][data-qa='name']")).sendKeys("Saffiya Sanhar");
        driver.findElement(By.cssSelector("input[name='email'][data-qa='email']")).sendKeys("hello@saffiya.com");
        driver.findElement(By.cssSelector("input[name='subject'][data-qa='subject']")).sendKeys("Automated message");
        driver.findElement(By.cssSelector("textarea[name='message'][data-qa='message']")).sendKeys("This note was sent from the Selenium suite.");
        driver.findElement(By.cssSelector("input[name='upload_file']")).sendKeys(attachment.toAbsolutePath().toString());

        driver.findElement(By.cssSelector("input[data-qa='submit-button'][name='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.contact-form div.status.alert-success")));
        String banner = driver.findElement(By.cssSelector("div.contact-form div.status.alert-success")).getText();
        Assert.assertTrue(banner.contains("Success! Your details have been submitted successfully."));
    }

    @Test
    public void contactFormEmptySubmitTest() {
        driver.get(BASE_URL + "/contact_us");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#contact-us-form")));
        String urlBefore = driver.getCurrentUrl();

        // Attempting to submit without satisfying HTML5 required fields should keep us on the contact route
        driver.findElement(By.cssSelector("input[data-qa='submit-button'][name='submit']")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("contact_us"), "Empty submission should not navigate away from the contact page.");
        Assert.assertEquals(driver.getCurrentUrl(), urlBefore, "The browser should block the POST until mandatory fields are filled.");
    }
}
