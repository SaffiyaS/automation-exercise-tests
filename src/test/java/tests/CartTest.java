package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Verifies asynchronous add-to-cart behaviour and the empty-state banner.
 */
public class CartTest extends BaseTest {

    @Test
    public void addToCartTest() {
        driver.get(BASE_URL + "/products");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.add-to-cart[data-product-id]")));
        String productId = driver.findElements(By.cssSelector("a.add-to-cart[data-product-id]")).get(0).getAttribute("data-product-id");

        // add_to_cart is invoked through AJAX; the modal confirms the round trip
        driver.findElements(By.cssSelector("a.add-to-cart[data-product-id]")).get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#cartModal h4.modal-title")));

        driver.get(BASE_URL + "/view_cart");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cart_info_table")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#product-" + productId)));
        Assert.assertTrue(driver.findElement(By.cssSelector("#product-" + productId)).isDisplayed(), "Product row should exist in the cart table.");
    }

    @Test
    public void removeFromCartTest() {
        driver.get(BASE_URL + "/products");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.add-to-cart[data-product-id]")));
        driver.findElements(By.cssSelector("a.add-to-cart[data-product-id]")).get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#cartModal")));

        driver.get(BASE_URL + "/view_cart");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cart_quantity_delete")));

        // Removing the lone line item should collapse the table and reveal the helper copy
        driver.findElement(By.cssSelector("a.cart_quantity_delete")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span#empty_cart")));
        String emptyBanner = driver.findElement(By.cssSelector("span#empty_cart")).getText();
        Assert.assertTrue(emptyBanner.contains("Cart is empty!"), "Cart strip should announce the empty basket state.");
    }
}
