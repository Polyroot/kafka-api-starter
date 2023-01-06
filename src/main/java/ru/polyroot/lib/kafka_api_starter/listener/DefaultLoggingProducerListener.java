package ru.polyroot.lib.kafka_api_starter.listener;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.LoggingProducerListener;

/*TODO: Сейчас реализация сделана при помощи расширении класса {@link LoggingProducerListener}
   потому что необходимо логировать ошибку и логировать успешное выполнение вызова.
   В дальнейшем может потребоваться обернуть ошибку в свою Dto
   или выбросить свой exception, который можно будет отловить.
   В таком случае нужно будет имплементировать интерфейса {@link ProducerListener}
   и реализовать метод {@link ProducerListener#onError(ProducerRecord, RecordMetadata, Exception)} вручную*/

/**
 * Listener - обрабатывающий ошибки для {@link org.springframework.kafka.core.KafkaTemplate}.
 *
 * @author Александр Резов
 */
@Log4j2
public class DefaultLoggingProducerListener extends LoggingProducerListener<String, Object> {

    @Override
    public void onSuccess(final ProducerRecord<String, Object> producerRecord,
                          final RecordMetadata recordMetadata) {
        log.info("{} {}", producerRecord.toString(), recordMetadata.toString());
    }

    @Override
    public void onError(final ProducerRecord<String, Object> record,
                        final RecordMetadata recordMetadata,
                        final Exception exception) {
        super.onError(record, recordMetadata, exception);
    }
}
