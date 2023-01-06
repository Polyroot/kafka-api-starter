package ru.polyroot.lib.kafka_api_starter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Настройки producer.
 *
 * @author Александр Резов
 */
@Validated
@Getter
@Setter
public class KafkaProducerApiLibProperties {

    /**
     * BootstrapAddress для kafka.
     * 
     * Пример: localhost:9092.
     */
    @NotBlank
    private String bootstrapAddress;
}
