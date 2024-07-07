package com.bellintegrator.pages.citilink;

import com.bellintegrator.pages.BasePage;
import io.qameta.allure.Step;
import com.codeborne.selenide.*;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/**
 * Класс CitilinkCatalogPage представляет страницу каталога сайта Citilink и включает методы
 * для взаимодействия с элементами страницы.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class CitilinkCatalogPage extends BasePage {

    /**
     * Коллекция элементов Selenide всех продуктов в результирующем блоке.
     */
    private final ElementsCollection allProductTitlesInResultBlock =
            $$x("//div[@data-meta-name='ProductVerticalSnippet']" +
                              "//a[@data-meta-name='Snippet__title']");


    /**
     * Проверяет, что текущий URL содержит указанный фрагмент.
     *
     * @param urlFragment ожидаемый фрагмент URL для проверки
     * @return текущий объект CitilinkCatalogPage
     */
    @Step("Проверка, что текущий URL содержит {urlFragment}")
    public CitilinkCatalogPage shouldHaveUrlFragment(String urlFragment) {
        webdriver().shouldHave(urlContaining(urlFragment));
        return this;
    }

    /**
     * Кликает на кнопку в указанном блоке фильтров.
     *
     * @param filterBlockName имя блока фильтров, в котором находится кнопка
     * @param buttonNameInFilterBlock имя кнопки, на которую необходимо кликнуть
     * @return текущий объект CitilinkCatalogPage
     */
    @Step("Кликаем {buttonNameInFilterBlock} у категории {filterBlockName} ")
    public CitilinkCatalogPage clickButtonInFilterBlock(String filterBlockName, String buttonNameInFilterBlock) {
        SelenideElement filterBlockElement = getFilterBlockByName(filterBlockName)
                .$x(".//button[text()='" + buttonNameInFilterBlock + "']");
        filterBlockElement.scrollTo();
        filterBlockElement.click(ClickOptions.usingJavaScript());
        return this;
    }

    /**
     * Выбирает указанный элемент в заданном блоке фильтров на странице каталога Citilink.
     *
     * @param filterBlockName имя блока фильтров, в котором находится элемент
     * @param elementName имя элемента, который необходимо выбрать в блоке фильтров
     * @return текущий объект CitilinkCatalogPage
     */
    @Step("Выбираем {elementName} в разделе {filterBlockName}")
    public CitilinkCatalogPage chooseElementInFilterBlock(String filterBlockName, String elementName) {
        SelenideElement element = getAllElementsInFilterBlock(filterBlockName).findBy(text(elementName));
        element.scrollTo();
        element.click(ClickOptions.usingJavaScript());
        waitForResultsLoad();
        return this;
    }

    /**
     * Проверяет, что результирующие продукты на текущей странице содержат указанное имя продукта.
     *
     * @param productName имя продукта, которое должно быть найдено
     */
    @Step("Проверка, что результирующие продукты содержат {productName} на просматриваемой странице")
    public void shouldHaveProductName(String productName) {
        allProductTitlesInResultBlock.shouldHave(CollectionCondition.allMatch("All elements should contain text",
                element -> element.getText().contains(productName))
        );
    }

    /**
     * Проверяет, что результирующие продукты содержат указанное имя продукта на всех доступных страницах.
     *
     * @param showMoreButtonName текст кнопки "Показать еще"
     * @param productName    имя продукта, которое должно быть найдено
     */
    @Step("Проверка, что результирующие продукты содержат {productName}")
    public void shouldHaveProductNameOnAllAvailablePages(String showMoreButtonName, String productName) {
        do {
            shouldHaveProductName(productName);
            SelenideElement button = getButtonShowMoreInResultBlock(showMoreButtonName);
            if (button != null) {
                button.scrollTo();
                button.click(ClickOptions.usingJavaScript());
                waitForResultsLoad();
            } else {
                break;
            }
        } while (true);
    }

    /**
     * Ожидает загрузки элемента с результатами.
     */
    private void waitForResultsLoad() {
        String fullXPath = "//div[contains(@class, '')]" +
                           "[descendant::section[@data-meta-name='ProductListLayout__banners']]" +
                           "[ancestor::div[@data-meta-name='ProductListLayout']]";
        $$x(fullXPath).shouldHave(sizeGreaterThan(0));
    }

    /**
     * Возвращает элемент блока фильтров по заданному имени блока фильтров.
     *
     * @param filterBlockName имя блока фильтров, по которому необходимо найти элемент
     * @return элемент блока фильтров с заданным именем
     */
    private SelenideElement getFilterBlockByName(String filterBlockName) {
        return $x("//div[@data-meta-value='" + filterBlockName + "']");
    }

    /**
     * Собирает все элементы из указанного блока фильтров.
     *
     * @param filterBlockName блок фильтров, из которого необходимо собрать элементы
     * @return коллекция элементов из блока фильтров
     */
    private ElementsCollection getAllElementsInFilterBlock(String filterBlockName) {
        String xPath = ".//span[contains(text(),'')][preceding-sibling::span[input[@type='checkbox']]]" +
                       "//span[contains(text(),'')]";
        return getFilterBlockByName(filterBlockName).$$x(xPath).filter(visible);
    }

    /**
     * Возвращает элемент кнопки "Показать еще" в результирующем блоке.
     *
     * @param showMoreButton текст кнопки "Показать еще"
     * @return элемент кнопки "Показать еще", если она существует и видима, иначе null
     */
    private SelenideElement getButtonShowMoreInResultBlock(String showMoreButton) {
        SelenideElement button = $$("button").findBy(exactText(showMoreButton));
        return button.exists() ? button : null;
    }

}
