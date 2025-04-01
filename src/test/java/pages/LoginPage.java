package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import utils.ConfigReader;


/**
 * Page Object для страницы авторизации
 */
public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
        page.navigate(ConfigReader.getProperty("login.page.url"));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void login(String username, String password) {
        String usernameSelector = ConfigReader.getProperty("username.selector");
        String passwordSelector = ConfigReader.getProperty("password.selector");
        String loginButtonSelector = ConfigReader.getProperty("login.button.selector");

        page.locator(usernameSelector).waitFor();
        page.locator(passwordSelector).waitFor();

        page.fill(usernameSelector, username);
        page.fill(passwordSelector, password);
        page.click(loginButtonSelector);

        page.waitForURL(
                url -> url.contains(ConfigReader.getProperty("inventory.page.url")),
                new Page.WaitForURLOptions().setTimeout(10000)
        );
    }
}
