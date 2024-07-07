package com.bellintegrator.data;

import com.bellintegrator.properties.Properties;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

/**
 * Класс DataProvider предоставляет данные для параметризованных тестов.
 * Содержит методы, которые возвращают потоки аргументов для тестов.
 * Каждый метод в этом классе используется для предоставления данных для конкретного теста.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class DataProvider {


    /**
     * Предоставляет данные для пятого теста.
     *
     * @return Stream из аргументов, содержащих URL и текст для поиска на странице.
     */
    public static Stream<Arguments> dataForTestFive() {
        return Stream.of(
                Arguments.of(
                        Properties.testsProperties.citilinkUrl(),
                        "Каталог товаров",
                        "Смартфоны и планшеты",
                        "Смартфоны",
                        "https://www.citilink.ru/catalog/smartfony/",
                        "Бренд",
                        "Показать все",
                        "APPLE",
                        "iPhone",
                        "Показать ещё"
                )
        );
    }
}
