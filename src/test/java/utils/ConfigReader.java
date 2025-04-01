package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс ConfigReader предназначен для загрузки и чтения свойств из файла
 * конфигурации config.properties.
 * Содержит статический блок инициализации, который загружает свойства при
 * загрузке класса
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Извините, не удалось найти config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
