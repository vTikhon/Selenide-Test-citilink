package com.bellintegrator.pages.citilink;

import com.bellintegrator.pages.BasePage;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

/**
 * Класс CitilinkStartPage представляет стартовую страницу сайта Citilink и включает методы для взаимодействия
 * с элементами страницы.
 *
 * @version 1.0
 * @since 2024-06-22
 * author Vergentev Tikhon
 */
public class CitilinkStartPage extends BasePage {

    /**
     * Кликает на кнопку с заданным именем.
     *
     * @param buttonName имя кнопки, на которую необходимо кликнуть
     * @return текущий объект CitilinkStartPage
     */
    @Step("Кликаем на кнопку {buttonName}")
    public CitilinkStartPage clickButton(String buttonName) {
        $$("span").findBy(exactText(buttonName)).click();
        return this;
    }

    /**
     * Наводит курсор на указанный раздел меню.
     *
     * @param menuName имя раздела меню, на который необходимо навести курсор
     * @return текущий объект CitilinkStartPage
     */
    @Step("Наводим курсор на раздел {menuName}")
    public CitilinkStartPage moveToMenu(String menuName) {
        SelenideElement menuElement = $$("span").findBy(exactText(menuName));
        String xPath = ".//ancestor::div[@data-meta-name='CatalogMenuDesktopLayout__menu']";
        menuElement.$x(xPath).hover();
        return this;
    }

    /**
     * Кликает на указанный внутренний раздел меню.
     *
     * @param innerMenuName имя внутреннего раздела меню, на который необходимо кликнуть
     * @param typeNextPage  класс следующей страницы, которая должна быть загружена после клика
     * @param <T>           тип следующей страницы, наследуемый от BasePage
     * @return объект следующей страницы типа T
     */
    @Step("Кликаем на раздел {innerMenuName}")
    public <T extends BasePage> T clickToInnerMenu(String innerMenuName, Class<T> typeNextPage) {
        String fullXPath =
                "//span[text()='" + innerMenuName + "'][ancestor::div[@data-meta-name='CatalogMenuDesktopLayout__body']]";
        $x(fullXPath).click();
        return typeNextPage.cast(page(typeNextPage));
    }

}
