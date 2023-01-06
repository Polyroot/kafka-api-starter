package ru.polyroot.lib.kafka_api_starter.config.kafka;

import lombok.NonNull;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaProducerPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaProducerPropConfigImpl;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaProducerApiLibProperties;

import java.util.Map;

/**
 * Конфигурации, связанные с Producer kafka.
 *
 * @author Александр Резов
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Бин настроек для {@link ProducerFactory}.
     * 
     * Для значения используется JsonSerializer
     * 
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого)
     * 
     * Бин получает дополнительные настройки из kafka.spring пропертей.
     * Если концептуально не нужен новый бин, то через эти настройки (kafka.spring) бин можно донастроить.
     *
     * @param kafkaProducerApiLibProperties настройки стартера для KafkaProducerPropConfig
     * @param kafkaProperties               настройки приходящие из spring.kafka
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaProducerFactoryConfig")
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    public KafkaProducerPropConfig defaultKafkaProducerFactoryConfig(
            @NonNull final KafkaProducerApiLibProperties kafkaProducerApiLibProperties,
            @NonNull final KafkaProperties kafkaProperties) {

        final Map<String, Object> buildProducerProperties = kafkaProperties.buildProducerProperties();
        final KafkaProducerPropConfig props = new KafkaProducerPropConfigImpl(buildProducerProperties);

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerApiLibProperties.getBootstrapAddress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Бин настроек для {@link KafkaTemplate}.
     * 
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого).
     *
     * @param factoryConfig настройки стартера для ProducerFactory
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaProducerFactory")
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    public ProducerFactory<String, Object> defaultKafkaProducerFactory(
            @Qualifier("defaultKafkaProducerFactoryConfig") @NonNull final KafkaProducerPropConfig factoryConfig) {

        return new DefaultKafkaProducerFactory<>(factoryConfig);
    }

    /**
     * Бин для отправки сообщений в kafka.
     * 
     * Как ключ принимает String, а значение может быть как String, так и Object.
     * 
     * Если отправить в kafka {@link String}, то она положиться как строка,
     * если положить составной объект, состоящий из полей, то он положиться как JSON
     * 
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого).
     *
     * @param producerFactory  настройки стартера для KafkaTemplate
     * @param producerListener листенер для обработки удачной/неудачной отправки в kafka
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaTemplate")
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    public KafkaTemplate<String, Object> defaultKafkaTemplate(
            @Qualifier("defaultKafkaProducerFactory") @NonNull final ProducerFactory<String, Object> producerFactory,
            @Qualifier("defaultProducerErrorListener") @NonNull final LoggingProducerListener<String, Object> producerListener) {

        final KafkaTemplate<String, Object> stringObjectKafkaTemplate = new KafkaTemplate<>(producerFactory);
        stringObjectKafkaTemplate.setProducerListener(producerListener);
        return stringObjectKafkaTemplate;
    }
}