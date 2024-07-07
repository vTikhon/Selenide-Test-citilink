package com.bellintegrator.tests;

import com.bellintegrator.pages.BasePage;
import com.bellintegrator.pages.citilink.CitilinkCatalogPage;
import com.bellintegrator.pages.citilink.CitilinkStartPage;
import com.bellintegrator.webdriver.WebDriverInitialization;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Класс Task_2_1_Test содержит тесты для проверки функционала сайта Citilink.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class Task_2_1_Test extends WebDriverInitialization {

    /**
     * Тест проверяет поиск смартфонов на сайте Citilink.
     *
     * @param buttonName             имя кнопки для первоначального клика
     * @param menuName               имя пункта меню, на который необходимо навести курсор
     * @param innerMenuName          имя подменю, на которое необходимо кликнуть
     * @param checkURL               ожидаемый фрагмент URL для проверки
     * @param filterBlock            блок фильтров, в котором находится кнопка "Показать все"
     * @param buttonNameInFilterBlock имя кнопки "Показать все" в блоке фильтров
     * @param element                элемент в блоке фильтров, который необходимо выбрать
     * @param productName            имя продукта, которое должно быть найдено на странице результатов
     * @param showMoreButton         текст кнопки "Показать еще" в блоке результатов
     */
    @Feature("Проверка сайта sitilink")
    @DisplayName("Проверка сайта sitilink")
    @ParameterizedTest(name = "{displayName} {arguments}")
    @MethodSource("com.bellintegrator.data.DataProvider#dataForTestFive")
    public void testSearchingSmartPhones(String url,
                                         String buttonName,
                                         String menuName,
                                         String innerMenuName,
                                         String checkURL,
                                         String filterBlock,
                                         String buttonNameInFilterBlock,
                                         String element,
                                         String productName,
                                         String showMoreButton) {

        BasePage.open(url, CitilinkStartPage.class)
                .clickButton(buttonName)
                .moveToMenu(menuName)
                .clickToInnerMenu(innerMenuName, CitilinkCatalogPage.class)
                .shouldHaveUrlFragment(checkURL)
                .clickButtonInFilterBlock(filterBlock, buttonNameInFilterBlock)
                .chooseElementInFilterBlock(filterBlock, element)
                .shouldHaveProductNameOnAllAvailablePages(showMoreButton, productName);
    }
}
