package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки процесса оформления заказа.
 */
public class CheckoutTest extends BaseTest {
    @Test
    void testCompleteCheckout() {
        new LoginPage(page).login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password")
        );

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addProductToCart(ConfigReader.getProperty("product.backpack"));

        CartPage cartPage = new CartPage(page);
        cartPage.navigateToCart();
        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        checkoutPage.completePurchase();

        assertTrue(
                page.url().contains(ConfigReader.getProperty("checkout.complete.url")),
                "Не завершился checkout"
        );
    }
}
