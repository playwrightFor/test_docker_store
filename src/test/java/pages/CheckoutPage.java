package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.ConfigReader;

/**
 * Page Object для страницы оформления заказа
 */
public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
        page.waitForSelector(ConfigReader.getProperty("first.name.selector"),
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void fillShippingInfo(String firstName, String lastName, String postalCode) {
        page.fill(ConfigReader.getProperty("first.name.selector"), firstName);
        page.fill(ConfigReader.getProperty("last.name.selector"), lastName);
        page.fill(ConfigReader.getProperty("postal.code.selector"), postalCode);

        if (lastName.isEmpty() || page.inputValue(ConfigReader.getProperty("last.name.selector")).isEmpty()) {
            throw new AssertionError("Фамилия обязательна для заполнения");
        }

        page.click(ConfigReader.getProperty("continue.button.selector"));
    }

    public void completePurchase() {
        page.click(ConfigReader.getProperty("finish.button.selector"));
        page.waitForURL(
                url -> url.contains("checkout-complete.html"),
                new Page.WaitForURLOptions().setTimeout(10000)
        );
    }
    public boolean isOrderSummaryVisible() {
        return page.locator(".summary_info").count() > 0;
    }
}
