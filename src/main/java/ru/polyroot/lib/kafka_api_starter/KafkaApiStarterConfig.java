package ru.polyroot.lib.kafka_api_starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Общая автоконфигурация для KafkaApiStarter.
 * 
 * Стартует строго после автоконфигурации {@link KafkaAutoConfiguration}
 * для исключения ошибок и конфликтов автоконфигураций
 *
 * @author Александр Резов
 */
@ComponentScan
@EnableKafka
@AutoConfigureBefore(KafkaAutoConfiguration.class)
@AutoConfigureAfter(KafkaProperties.class)
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaApiStarterConfig {
}
