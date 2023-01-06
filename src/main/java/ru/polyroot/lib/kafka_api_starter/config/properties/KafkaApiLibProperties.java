package ru.polyroot.lib.kafka_api_starter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Настройки включения и выключение создания бинов producer/consumer.
 *
 * @author Александр Резов
 */
@Validated
@Getter
@Setter
public class KafkaApiLibProperties {

    /**
     * Настройки включения/выключения создания бинов PRODUCER. Если true, тогда бины создаются. По умолчанию false.
     */
    @NotNull
    private Boolean producerEnable;
    /**
     * Настройки включения/выключения создания бинов CONSUMER. Если true, тогда бины создаются. По умолчанию false.
     */
    @NotNull
    private Boolean consumerEnable;
}
