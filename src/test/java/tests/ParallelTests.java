package tests;

import base.BaseTest;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import utils.ConfigReader;

/**
 * Класс для тестирования параллельного выполнения операций с корзиной и оформлением заказа.
 * Содержит комплексные тесты, проверяющие:
 * - Полный процесс оформления заказа
 * - Параллельное управление содержимым корзины
 */
public class ParallelTests extends BaseTest {

    @Test
    @DisplayName("Полный цикл оформления заказа")
    void testFullCheckoutProcess() {

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));

        assertEquals(page.url(), ConfigReader.getProperty("inventory.page.url"), "Не удалось авторизоваться");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addProductToCart(ConfigReader.getProperty("product.backpack"));

        page.waitForCondition(
                () -> inventoryPage.getCartItemCount() == 1,
                new Page.WaitForConditionOptions().setTimeout(10000)
        );

        CartPage cartPage = new CartPage(page);
        cartPage.navigateToCart();
        page.waitForURL(ConfigReader.getProperty("cart.page.url"));

        assertTrue(cartPage.isProductInCart(ConfigReader.getProperty("product.backpack")), "Товар не найден в корзине");

        cartPage.proceedToCheckout();
        page.waitForURL(ConfigReader.getProperty("checkout.step1.url"));

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        page.waitForURL(ConfigReader.getProperty("checkout.page.url"));

        assertTrue(checkoutPage.isOrderSummaryVisible(), "Итоговая сумма не отображается");

        checkoutPage.completePurchase();
        page.waitForURL(ConfigReader.getProperty("checkout.complete.url"));

        assertTrue(
                page.locator(".complete-header").innerText()
                        .contains(ConfigReader.getProperty("checkout.complete.message")),
                "Нет сообщения об успешном заказе"
        );
    }


    @Test
    @DisplayName("Управление корзиной")
    void testCartManagement() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addProductToCart(ConfigReader.getProperty("product.bike.light"));
        inventoryPage.addProductToCart(ConfigReader.getProperty("product.bolt.tshirt"));
        assertEquals(2, inventoryPage.getCartItemCount(), "Неверное количество товаров");

        CartPage cartPage = new CartPage(page);
        cartPage.navigateToCart();
        cartPage.removeItem(ConfigReader.getProperty("product.bike.light"));

        assertEquals(1, cartPage.getCartItems().size(), "Неверное количество после удаления");
        assertTrue(
                cartPage.getCartItems().contains(ConfigReader.getProperty("product.bolt.tshirt")),
                "Не найден оставшийся товар"
        );
    }
}