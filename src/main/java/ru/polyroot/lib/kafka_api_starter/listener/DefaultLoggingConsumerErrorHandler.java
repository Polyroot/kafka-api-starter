package ru.polyroot.lib.kafka_api_starter.listener;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * Handler для отлавливания ошибок у producer kafka и записи их в лог.
 *
 * @author Александр Резов
 */
@Log4j2
public class DefaultLoggingConsumerErrorHandler implements CommonErrorHandler {

    @Override
    public void handleRecord(final Exception thrownException,
                             final ConsumerRecord<?, ?> record,
                             final Consumer<?, ?> consumer,
                             final MessageListenerContainer container) {
        log.error("Kafka listener error: " + thrownException.getMessage());
    }
}
