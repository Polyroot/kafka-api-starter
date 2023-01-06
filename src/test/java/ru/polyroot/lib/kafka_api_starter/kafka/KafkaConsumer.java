package ru.polyroot.lib.kafka_api_starter.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static ru.polyroot.lib.kafka_api_starter.test_config.ConfigEmbeddedKafkaDocker.TEST_TOPIC;

@Slf4j
@Component
public class KafkaConsumer {

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = TEST_TOPIC)
    public void receive(final ConsumerRecord<?, ?> consumerRecord) {
        messages.add(consumerRecord.value().toString());
    }

    public List<String> getCopyMessages() {
        final ArrayList<String> copyMessages = new ArrayList<>(messages);
        messages.clear();
        return copyMessages;
    }
}