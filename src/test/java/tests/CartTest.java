package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Тестовый класс для проверки функционала корзины покупок.
 * Содержит тест добавления товара в корзину.
 */
public class CartTest extends BaseTest {

    @Test
    void testAddToCart() {
        new LoginPage(page).login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password")
        );

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addProductToCart(ConfigReader.getProperty("product.backpack"));

        CartPage cartPage = new CartPage(page);
        cartPage.navigateToCart();

        assertTrue(
                cartPage.getCartItems().contains(ConfigReader.getProperty("product.backpack")),
                "Товар не найден в корзине"
        );
    }
}
