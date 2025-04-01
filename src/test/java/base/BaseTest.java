package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import utils.ConfigReader;
import utils.ScreenshotWatcher;

public class BaseTest {
    protected static ExtentReports extent;
    /**
     * Текущий тест в отчете
     */
    protected ExtentTest test;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    /**
     * Авто-скриншоты при падении тестов
     */
    @RegisterExtension
    ScreenshotWatcher screenshotWatcher = new ScreenshotWatcher();

    static {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        ScreenshotWatcher.setPage(page);
        page.navigate(ConfigReader.getProperty("login.page.url"));
    }

    /**
     * После теста: закрытие ресурсов и сохранение скриншотов при ошибках
     */
    @AfterEach
    void tearDown() {
        if (ScreenshotWatcher.getLastScreenshotPath() != null) {
            test.addScreenCaptureFromPath(ScreenshotWatcher.getLastScreenshotPath());
        }
        page.close();
        browser.close();
        playwright.close();
        extent.flush();
    }

    /**
     * Логирование в отчет
     */
    public void logInfo(String message) {
        test.log(Status.INFO, message);
    }
}

