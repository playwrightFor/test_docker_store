package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;

import java.util.List;


/**
 * Page Object для работы с корзиной покупок.
 */
public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public void navigateToCart() {
        page.click(ConfigReader.getProperty("cart.link.selector"));
    }

    public void removeItem(String productName) {
        page.locator(String.format("%s:has-text('%s') %s",
                        ConfigReader.getProperty("cart.item.selector"),
                        productName,
                        ConfigReader.getProperty("remove.button.selector")))
                .click();
    }

    public boolean isProductInCart(String productName) {
        Locator cartItems = page.locator(ConfigReader.getProperty("cart.item.selector"));
        return cartItems.locator("text=" + productName).count() > 0;
    }

    public List<String> getCartItems() {
        return page.locator(ConfigReader.getProperty("cart.items.selector")).allTextContents();
    }

    public void proceedToCheckout() {
        page.click(ConfigReader.getProperty("checkout.button.selector"));
    }
}
