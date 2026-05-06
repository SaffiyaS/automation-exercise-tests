package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Product discovery and product detail page smoke checks.
 */
public class ProductTest extends BaseTest {

    @Test
    public void searchProductTest() {
        // Catalog lives under /products even when we start on the home page
        driver.get(BASE_URL + "/products");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#search_product[name='search']")));
        driver.findElement(By.cssSelector("input#search_product[name='search']")).clear();
        driver.findElement(By.cssSelector("input#search_product[name='search']")).sendKeys("T-Shirt");

        // The search icon triggers a location change with a query string (handled in main.js)
        driver.findElement(By.cssSelector("button#submit_search")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.title.text-center")));
        String heading = driver.findElement(By.cssSelector("h2.title.text-center")).getText();
        Assert.assertTrue(heading.toLowerCase().contains("searched products"), "Search results heading should be shown.");

        // At least one product tile should render after the filter
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.productinfo p")));
        Assert.assertTrue(driver.findElements(By.cssSelector("div.productinfo p")).size() > 0, "Expected product rows after searching.");
    }

    @Test
    public void viewProductDetailsTest() {
        driver.get(BASE_URL + "/products");

        // Use the first "View Product" deep link in the grid
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href^='/product_details/']")));
        driver.findElements(By.cssSelector("a[href^='/product_details/']")).get(0).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-information h2")));
        String productName = driver.findElement(By.cssSelector("div.product-information h2")).getText();
        Assert.assertFalse(productName.isBlank(), "Product title should be visible on the PDP.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-information span span")));
        String price = driver.findElement(By.cssSelector("div.product-information span span")).getText();
        Assert.assertTrue(price.toLowerCase().contains("rs"), "Price label should be displayed next to the title.");
    }
}
