package com.bellintegrator.properties;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс Properties загружает конфигурационные свойства для тестов.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
public class Properties {

    /**
     * Объект testsProperties загружает свойства тестов из конфигурационного файла.
     */
    public static TestsProperties testsProperties = ConfigFactory.create(TestsProperties.class);
}
