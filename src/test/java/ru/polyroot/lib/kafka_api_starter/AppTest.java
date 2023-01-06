package ru.polyroot.lib.kafka_api_starter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты основного класса приложения.
 *
 * @author Александр Резов
 */
@SpringJUnitConfig
class AppTest {

    @Autowired
    ApplicationContext context;

    @Test
    void givenAppContext_whenAppStart_thenContextNotNull() {
        assertNotNull(context, "Не был загружен контекст приложения");
    }

}
