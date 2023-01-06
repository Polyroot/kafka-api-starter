package ru.polyroot.lib.kafka_api_starter.config.kafka.settings;

import java.util.Map;

/**
 * Интерфейс-обёртка {@link Map} служит для удобного подтягивания бинов
 * настроек для {@link org.springframework.kafka.core.KafkaAdmin} через DI.
 *
 * @author Александр Резов
 */
public interface KafkaAdminPropConfig extends Map<String, Object> {
}
