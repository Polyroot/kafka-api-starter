package ru.polyroot.lib.kafka_api_starter.config.kafka;

import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaConsumerPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaConsumerPropConfigImpl;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaConsumerApiLibProperties;

import java.util.Map;

/**
 * Конфигурации, связанные с KafkaConsumer.
 *
 * @author Александр Резов
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * Бин настроек для {@link ConsumerFactory}.
     * 
     * Для значения используется JsonDeserializer.
     * 
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого).
     * 
     * Бин получает дополнительные настройки из kafka.spring пропертей.
     * Если концептуально не нужен новый бин, то через эти настройки (kafka.spring) бин можно донастроить.
     *
     * @param kafkaApiLibProperties настройки стартера для ConsumerFactory
     * @param kafkaProperties       настройки приходящие из spring.kafka
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaConsumerConfig")
    @ConditionalOnProperty(name = "kafka.consumer-enable", havingValue = "true")
    public KafkaConsumerPropConfig defaultKafkaConsumerConfig(
            @NonNull final KafkaConsumerApiLibProperties kafkaApiLibProperties,
            @NonNull final KafkaProperties kafkaProperties) {

        final Map<String, Object> buildConsumerProperties = kafkaProperties.buildConsumerProperties();
        final KafkaConsumerPropConfig props = new KafkaConsumerPropConfigImpl(buildConsumerProperties);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaApiLibProperties.getBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaApiLibProperties.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaApiLibProperties.getAutoOffsetReset().getValue());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
                (int) kafkaApiLibProperties.getAutoCommitIntervalMs().toMillis());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaApiLibProperties.getEnableAutoCommit());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * Бин настроек для {@link KafkaListenerContainerFactory}.
     * 
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого)
     *
     * @param kafkaConsumerConfig настройки стартера для KafkaAdmin
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaConsumerFactory")
    @ConditionalOnProperty(name = "kafka.consumer-enable", havingValue = "true")
    public ConsumerFactory<String, String> defaultKafkaConsumerFactory(
            @Qualifier("defaultKafkaConsumerConfig") @NonNull final KafkaConsumerPropConfig kafkaConsumerConfig) {

        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig);
    }

    /**
     * Бин настроек для использования {@link org.springframework.kafka.annotation.KafkaListener}.
     * 
     * По умолчанию аннотация {@link org.springframework.kafka.annotation.KafkaListener}
     * внедряет в себя бин с названием kafkaListenerContainerFactory
     * поэтому {@link org.springframework.kafka.annotation.KafkaListener} будет использовать именно этот бин
     * 
     * Бин является потокобезопасным, поэтому вместе с {@link org.springframework.kafka.annotation.KafkaListener} можно
     * использовать {@link org.springframework.scheduling.annotation.Async},
     * тогда чтение будет производиться в нескольких потоках
     *
     * @param consumerFactory - фабрика consumer
     * @param errorHandler    - перехватчик ошибок для {@link KafkaListenerContainerFactory}
     */
    @Bean("kafkaListenerContainerFactory")
    @NonNull
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    @ConditionalOnProperty(name = "kafka.consumer-enable", havingValue = "true")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            @Qualifier("defaultKafkaConsumerFactory") @NonNull final ConsumerFactory<String, String> consumerFactory,
            @Qualifier("defaultConsumerErrorHandler") @NonNull final CommonErrorHandler errorHandler,
            @Qualifier("defaultMessageConverter") @NonNull final MessageConverter messageConverter) {

        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    /**
     * Ковертер байтового сообщения, приходящего из kafka в объект, при помощи ObjectMapper
     */
    @Bean("defaultMessageConverter")
    @NonNull
    @ConditionalOnMissingBean(name = "defaultMessageConverter")
    @ConditionalOnProperty(name = "kafka.consumer-enable", havingValue = "true")
    public MessageConverter defaultJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }
}