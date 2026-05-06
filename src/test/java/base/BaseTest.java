package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Shared browser setup so each test opens a fresh session at the storefront.
 */
public abstract class BaseTest {

    protected static final String BASE_URL = "https://automationexercise.com";
    /** Default explicit wait timeout for flaky network or slow JS on the demo site */
    protected static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUpBrowser() {
        // Headless-compatible flags help automated runs behave more like a normal desktop Chrome window
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        // Rely on WebDriverWait in tests rather than implicit delays
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
