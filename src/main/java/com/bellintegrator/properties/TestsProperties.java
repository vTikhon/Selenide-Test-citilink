package com.bellintegrator.properties;

import org.aeonbits.owner.Config;

/**
 * Интерфейс TestsProperties для загрузки конфигурационных свойств из различных источников.
 *
 * @version 1.0
 * @since 2024-06-22
 * @author Vergentev Tikhon
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "file:src/main/resources/tests.properties"
})
public interface TestsProperties extends Config {

    /**
     * Возвращает URL для сайта Citilink.
     *
     * @return URL для сайта Citilink
     */
    @Config.Key("citilink.url")
    String citilinkUrl();

}
