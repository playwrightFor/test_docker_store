package tests;

import base.BaseTest;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки функционала авторизации.
 */
public class LoginTest extends BaseTest {

    @Attachment
    @Test
    void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));
        assertTrue(
                page.url().contains(ConfigReader.getProperty("inventory.page.url")),
                "Не удалось авторизоваться");
    }
}
