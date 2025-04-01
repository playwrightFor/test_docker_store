package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.ConfigReader;

/**
 * Page Object для страницы каталога товаров
 */
public class InventoryPage {
    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
        page.waitForSelector(ConfigReader.getProperty("inventory.list.selector"),
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000));
    }

    public void addProductToCart(String productName) {
        page.locator(String.format(
                        "div.inventory_item:has-text('%s') " + ConfigReader.getProperty("add.to.cart.selector"),
                        productName))
                .click();
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(page.textContent(ConfigReader.getProperty("inventory.list")));
        } catch (Exception e) {
            return 0;
        }
    }
}
