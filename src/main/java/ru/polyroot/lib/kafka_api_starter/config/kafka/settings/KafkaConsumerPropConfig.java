package ru.polyroot.lib.kafka_api_starter.config.kafka.settings;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;

/**
 * Интерфейс-обёртка {@link Map} служит для удобного подтягивания бинов
 * настроек для {@link org.springframework.kafka.core.ConsumerFactory} через DI.
 * 
 * Value в мапе должно быть из {@link ConsumerConfig}.
 *
 * @author Александр Резов
 */
public interface KafkaConsumerPropConfig extends Map<String, Object> {

    /**
     * Данной настройки нет в {@link ConsumerConfig}, но её можно использовать, чтобы разрешить JsonDeserializer
     * десериализацию из данных пакетов
     */
    String SPRING_JSON_TRUSTED_PACKAGES = "spring.json.trusted.packages";
}
