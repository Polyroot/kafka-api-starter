package ru.polyroot.lib.kafka_api_starter.config.kafka;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import ru.polyroot.lib.kafka_api_starter.listener.DefaultLoggingConsumerErrorHandler;
import ru.polyroot.lib.kafka_api_starter.listener.DefaultLoggingProducerListener;

/**
 * Конфигурации, связанные с Listeners kafka.
 *
 * @author Александр Резов
 */
@Configuration
public class KafkaListenerConfig {

    /**
     * @return бин перехватчика ошибок для {@link KafkaListenerContainerFactory}.
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultConsumerErrorHandler")
    public DefaultLoggingConsumerErrorHandler defaultConsumerErrorHandler() {
        return new DefaultLoggingConsumerErrorHandler();
    }

    /**
     * @return бин listener для {@link org.springframework.kafka.core.KafkaTemplate}.
     */
    @Bean
    @NonNull
    @ConditionalOnMissingBean(name = "defaultProducerErrorListener")
    public DefaultLoggingProducerListener defaultProducerErrorListener() {
        return new DefaultLoggingProducerListener();
    }

    /**
     * Бин ProducerListener для стартера {@link KafkaAutoConfiguration}.
     * 
     * Хотя данная автоконфигурация запускается строго перед KafkaAutoConfiguration,
     * та в свою очередь для своих настроек требует бина ProducerListener&lt;Object, Object&gt;,
     * но в условиях его создания стоит @ConditionalOnMissingBean(type = ProducerListener.class)
     * При данном раскладе у бина рассматривается только тип, не учитывая дженериков, поэтому,
     * чтобы не сломать KafkaAutoConfiguration этот бин делается здесь(полностью идентичен тому,
     * что создаётся в {@link KafkaAutoConfiguration}).
     *
     * @return бин listener ошибок для {@link org.springframework.kafka.core.KafkaTemplate}
     */
    @Bean
    @NonNull
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener();
    }
}
