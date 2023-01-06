package ru.polyroot.lib.kafka_api_starter.test_config;

import lombok.NonNull;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polyroot.lib.kafka_api_starter.config.kafka.KafkaAdminConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.KafkaConsumerConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.KafkaProducerConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaAdminPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaConsumerPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaProducerPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaConsumerApiLibProperties;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaProducerApiLibProperties;

import java.time.Duration;

/**
 * Конфигурация для поднятия базы данных kafka в docker контейнере
 *
 * @author Александр Резов
 */
@Configuration
@Testcontainers
@Profile("docker-kafka-test")
public class ConfigEmbeddedKafkaDocker {

    public static final String TEST_TOPIC = "test_topic";

    @NonNull
    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaContainer kafkaContainer(final DockerKafkaImageProperties kafkaImageProperties) {
        return new KafkaContainer(kafkaImageProperties.getUrlByPriority())
                .withStartupTimeout(Duration.ofSeconds(120));
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(TEST_TOPIC)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    @NonNull
    public KafkaProducerPropConfig defaultKafkaProducerFactoryConfig(
            @NonNull final KafkaContainer kafkaContainer,
            @NonNull final KafkaProducerApiLibProperties kafkaApiLibProperties,
            @NonNull final KafkaProperties kafkaProperties) {
        kafkaApiLibProperties.setBootstrapAddress(kafkaContainer.getBootstrapServers());
        return new KafkaProducerConfig().defaultKafkaProducerFactoryConfig(kafkaApiLibProperties, kafkaProperties);
    }

    @Bean
    @NonNull
    public KafkaConsumerPropConfig defaultKafkaConsumerConfig(
            @NonNull final KafkaConsumerApiLibProperties kafkaApiLibProperties,
            @NonNull final KafkaProperties kafkaProperties,
            @NonNull final KafkaContainer kafkaContainer) {
        kafkaApiLibProperties.setBootstrapAddress(kafkaContainer.getBootstrapServers());
        return new KafkaConsumerConfig().defaultKafkaConsumerConfig(kafkaApiLibProperties, kafkaProperties);
    }

    @Bean
    @NonNull
    public KafkaAdminPropConfig defaultKafkaAdminConfig(
            @NonNull final KafkaContainer kafkaContainer,
            @NonNull final KafkaProducerApiLibProperties kafkaApiLibProperties,
            @NonNull final KafkaProperties kafkaProperties) {
        kafkaApiLibProperties.setBootstrapAddress(kafkaContainer.getBootstrapServers());
        return new KafkaAdminConfig().defaultKafkaAdminConfig(kafkaApiLibProperties, kafkaProperties);
    }

}