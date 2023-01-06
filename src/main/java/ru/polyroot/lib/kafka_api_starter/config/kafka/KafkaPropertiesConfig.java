package ru.polyroot.lib.kafka_api_starter.config.kafka;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaAdminApiLibProperties;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaApiLibProperties;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaConsumerApiLibProperties;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaProducerApiLibProperties;

/**
 * Конфигурации, связанные с общими настройками kafka.
 *
 * @author Александр Резов
 */
@Configuration
public class KafkaPropertiesConfig {

    @Bean
    @NonNull
    @ConfigurationProperties(prefix = "kafka")
    @Validated
    public KafkaApiLibProperties kafkaAliLibProperties() {
        return new KafkaApiLibProperties();
    }

    @Bean
    @NonNull
    @ConfigurationProperties(prefix = "kafka.admin")
    @Validated
    public KafkaAdminApiLibProperties kafkaTopicsApiLibProperties() {
        return new KafkaAdminApiLibProperties();
    }

    @Bean
    @NonNull
    @ConfigurationProperties(prefix = "kafka.producer")
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    @Validated
    public KafkaProducerApiLibProperties kafkaProducerApiLibProperties() {
        return new KafkaProducerApiLibProperties();
    }

    @Bean
    @NonNull
    @ConfigurationProperties(prefix = "kafka.consumer")
    @ConditionalOnProperty(name = "kafka.consumer-enable", havingValue = "true")
    @Validated
    public KafkaConsumerApiLibProperties kafkaConsumerApiLibProperties() {
        return new KafkaConsumerApiLibProperties();
    }
}
