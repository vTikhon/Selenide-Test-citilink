package com.bellintegrator.webdriver;

import com.bellintegrator.service.CustomAllureSelenide;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

/**
 * Класс WebDriverInitialization выполняет настройку WebDriver перед выполнением тестов.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class WebDriverInitialization {

    /**
     * Метод setup() выполняется перед всеми тестами и настраивает Selenide Logger с Allure интеграцией.
     * Включает создание скриншотов и сохранение исходного кода страницы при ошибках.
     */
    @BeforeAll
    public static void setup(){
        SelenideLogger.addListener("AllureSelenide",
                new CustomAllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    /**
     * Метод, выполняющийся перед каждым тестом.
     * Настраивает Selenide для использования браузера Chrome и запускает его в максимизированном режиме.
     */
    @BeforeEach
    public void options() {
        Configuration.headless = false;
        Configuration.browser = "chrome";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        setWebDriver(driver);
    }

    /**
     * Метод, выполняющийся после каждого теста.
     * Закрывает текущий экземпляр WebDriver.
     */
    @AfterEach
    public void quit() {
        closeWebDriver();
    }

}
