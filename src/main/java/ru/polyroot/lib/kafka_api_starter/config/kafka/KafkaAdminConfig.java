package ru.polyroot.lib.kafka_api_starter.config.kafka;

import lombok.NonNull;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaAdminPropConfig;
import ru.polyroot.lib.kafka_api_starter.config.kafka.settings.KafkaAdminPropConfigImpl;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaAdminApiLibProperties;
import ru.polyroot.lib.kafka_api_starter.config.properties.KafkaProducerApiLibProperties;

import java.util.Map;

/**
 * Конфигурации, связанные с KafkaAdmin.
 *
 * @author Александр Резов
 */
@Configuration
public class KafkaAdminConfig {

    /**
     * Бин настроек для {@link KafkaAdmin}.
     * Его можно переопределить (оставить то же название, тогда новый бин встанет в настройки стартера на место этого)
     * Бин получает дополнительные настройки из kafka.spring пропертей.
     * Если концептуально не нужен новый бин, то через эти настройки (kafka.spring) бин можно донастроить.
     *
     * @param apiLibProperties настройки стартера для KafkaProducer. Используется именно он из соображений,
     *                         что, если мы пишем в kafka, то работаем мы с producer
     * @param kafkaProperties  настройки приходящие из spring.kafka
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultKafkaAdminConfig")
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    public KafkaAdminPropConfig defaultKafkaAdminConfig(@NonNull final KafkaProducerApiLibProperties apiLibProperties,
                                                        @NonNull final KafkaProperties kafkaProperties) {

        final Map<String, Object> buildAdminProperties = kafkaProperties.buildAdminProperties();
        final KafkaAdminPropConfig props = new KafkaAdminPropConfigImpl(buildAdminProperties);
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, apiLibProperties.getBootstrapAddress());
        return props;
    }

    /**
     * KafkaAdmin служит утилитарным бином для работы с кафкой.
     * Например: создать топик, посмотреть данные о топике и т.п.
     *
     * @param kafkaAdminConfig           конфигурации kafkaAdmin
     * @param kafkaAdminApiLibProperties настройки стартера для KafkaAdmin
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(value = KafkaAdmin.class)
    @ConditionalOnProperty(name = "kafka.producer-enable", havingValue = "true")
    public KafkaAdmin defaultKafkaAdmin(
            @Qualifier("defaultKafkaAdminConfig") @NonNull final KafkaAdminPropConfig kafkaAdminConfig,
            @NonNull final KafkaAdminApiLibProperties kafkaAdminApiLibProperties) {

        final KafkaAdmin kafkaAdmin = new KafkaAdmin(kafkaAdminConfig);
        kafkaAdmin.setAutoCreate(kafkaAdminApiLibProperties.isAutoCreateEnable());
        return kafkaAdmin;
    }

}