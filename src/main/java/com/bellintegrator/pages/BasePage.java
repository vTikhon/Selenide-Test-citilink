package com.bellintegrator.pages;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.page;

/**
 * Абстрактный класс BasePage служит базовым классом для всех страниц приложения.
 * Этот класс может содержать общие методы и свойства, которые будут унаследованы всеми страницами.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public abstract class BasePage {

    /**
     * Открывает указанную URL-адрес и возвращает экземпляр страницы, представляющей этот URL.
     *
     * @param url URL-адрес страницы, которую необходимо открыть
     * @param typeNextPage класс страницы, который необходимо создать после открытия URL
     * @param <T> тип страницы, наследуемый от BasePage
     * @return экземпляр страницы типа T
     */
    @Step("Открываем сайт {url}")
    public static <T extends BasePage> T open(String url, Class<T> typeNextPage) {
        com.codeborne.selenide.Selenide.open(url, typeNextPage);
        return typeNextPage.cast(page(typeNextPage));
    }
}
