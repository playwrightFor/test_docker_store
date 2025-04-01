package utils;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Класс ScreenshotWatcher реализует интерфейс и предназначен для
 * автоматического создания скриншотов при падении тестов. Он использует механизм
 * потокобезопасного хранения страницы для получения текущего состояния интерфейса в момент
 * неудачи теста.
 * Основные возможности класса:
 * -Создание скриншота при падении теста;
 * -Хранение пути к последнему созданному скриншоту;
 * -Потокобезопасное управление объектом.
 */
public class ScreenshotWatcher implements TestWatcher {
    /**
     * -- GETTER --
     * Возвращает путь к последнему скриншоту
     */
    @Getter
    private static String lastScreenshotPath;
    private static final ThreadLocal<Page> pageHolder = new ThreadLocal<>();

    /**
     * Устанавливает Page для текущего потока.
     */
    public static void setPage(Page page) {
        pageHolder.set(page);
    }

    /** Вызывается при падении теста */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Page page = pageHolder.get();
        if (page != null) {
            String testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");
            String screenshotDir = ConfigReader.getProperty("screenshot.directory");
            Path path = Paths.get(screenshotDir, testName + "_FAILED.png");
            try {
                page.screenshot(new Page.ScreenshotOptions().setPath(path));
                lastScreenshotPath = path.toString();
                System.out.println("Скриншот сохранен: " + lastScreenshotPath);
            } catch (Exception e) {
                System.err.println("Ошибка при создании скриншота: " + e.getMessage());
            }
        } else {
            System.err.println("Page не установлен для скриншота.");
        }
    }
}