package com.bellintegrator.service;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLog;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Класс CustomAllureSelenide расширяет функциональность AllureSelenide для кастомизации логирования и сохранения данных в отчетах Allure.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class CustomAllureSelenide extends AllureSelenide {

    /**
     * Логгер для записи информации о ходе выполнения и ошибках.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenide.class);

    /**
     * Флаг для определения, сохранять ли скриншоты.
     */
    private final boolean saveScreenshots;

    /**
     * Флаг для определения, сохранять ли HTML исходный код страницы.
     */
    private final boolean savePageHtml;

    /**
     * Флаг для включения шагов Selenide Locators в логирование.
     */
    private final boolean includeSelenideLocatorsSteps;

    /**
     * Карта для хранения типов логов и уровней логирования.
     */
    private final Map<LogType, Level> logTypesToSave;

    /**
     * Объект AllureLifecycle для управления жизненным циклом тестов в Allure.
     */
    private final AllureLifecycle lifecycle;

    /**
     * Конструктор по умолчанию, который инициализирует необходимые параметры.
     */
    public CustomAllureSelenide() {
        this(Allure.getLifecycle());
    }

    /**
     * Конструктор, принимающий AllureLifecycle для инициализации.
     *
     * @param lifecycle объект AllureLifecycle
     */
    public CustomAllureSelenide(AllureLifecycle lifecycle) {
        this.saveScreenshots = true;
        this.savePageHtml = true;
        this.includeSelenideLocatorsSteps = true;
        this.logTypesToSave = new HashMap<>();
        this.lifecycle = lifecycle;
    }

    /**
     * Получает байты скриншота текущей страницы, если WebDriver был запущен.
     *
     * @return Optional, содержащий байты скриншота, или пустой Optional, если скриншот не может быть получен
     */
    private static Optional<byte[]> getScreenshotBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ?
                    Optional.of(((TakesScreenshot)WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)) :
                    Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Не удалось получить скриншот", var1);
            return Optional.empty();
        }
    }

    /**
     * Получает байты исходного кода текущей страницы, если WebDriver был запущен.
     *
     * @return Optional, содержащий байты исходного кода страницы, или пустой Optional, если исходный код не может быть получен
     */
    private static Optional<byte[]> getPageSourceBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ?
                    Optional.of(WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8)) :
                    Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Не удалось получить исходный код страницы", var1);
            return Optional.empty();
        }
    }

    /**
     * Получает логи браузера для указанного типа логов и уровня логирования.
     *
     * @param logType тип логов браузера
     * @param level уровень логирования
     * @return строка, содержащая логи браузера
     */
    private static String getBrowserLogs(LogType logType, Level level) {
        return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
    }

    /**
     * Выполняется после события Selenide и добавляет соответствующие вложения и логи в отчет Allure.
     *
     * @param event событие Selenide
     */
    @Override
    public void afterEvent(LogEvent event) {
        this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
            if (this.saveScreenshots) {
                getScreenshotBytes().ifPresent((bytes) -> {
                    this.lifecycle.addAttachment("Screenshot", "image/png", "png", bytes);
                });
            }

            if (this.savePageHtml) {
                getPageSourceBytes().ifPresent((bytes) -> {
                    this.lifecycle.addAttachment("Page source", "text/html", "html", bytes);
                });
            }

            if (!this.logTypesToSave.isEmpty()) {
                this.logTypesToSave.forEach((logType, level) -> {
                    byte[] content = getBrowserLogs(logType, level).getBytes(StandardCharsets.UTF_8);
                    this.lifecycle.addAttachment("Logs from: " + logType, "application/json", ".txt", content);
                });
            }
        });

        if (this.stepsShouldBeLogged(event)) {
            this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
                switch(event.getStatus()) {
                    case PASS:
                        this.lifecycle.updateStep((step) -> {
                            step.setStatus(Status.PASSED);
                        });
                        break;
                    case FAIL:
                        this.lifecycle.updateStep((stepResult) -> {
                            stepResult.setStatus((Status) ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
                            stepResult.setStatusDetails((StatusDetails)ResultsUtils.getStatusDetails(event.getError()).orElse(new StatusDetails()));
                        });
                        break;
                    default:
                        LOGGER.warn("Шаг завершен с неподдерживаемым статусом {}", event.getStatus());
                }

                this.lifecycle.stopStep();
            });
        }
    }

    /**
     * Определяет, должны ли шаги Selenide быть залогированы.
     *
     * @param event событие Selenide
     * @return true, если шаги должны быть залогированы; false в противном случае
     */
    private boolean stepsShouldBeLogged(LogEvent event) {
        return this.includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
    }
}
