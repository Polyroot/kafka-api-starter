package ru.polyroot.lib.kafka_api_starter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * Общие настройки для AdminKafka.
 *
 * @author Александр Резов
 */
@Validated
@Getter
@Setter
public class KafkaAdminApiLibProperties {

    /**
     * Выключить создание топика в кафка, если его нет.
     * Внимание: в самой kafka есть настройка auto.create.topics.enable,
     * которая сама делает топики (если их нет), поэтому выключать нужно и в самой kafka.
     */
    boolean autoCreateEnable = false;
}
