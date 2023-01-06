package ru.polyroot.lib.kafka_api_starter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.polyroot.lib.kafka_api_starter.test_model.KafkaTestDto;
import ru.polyroot.lib.kafka_api_starter.test_util.test_categories.TestCategoryUnitDocker;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static ru.polyroot.lib.kafka_api_starter.test_config.ConfigEmbeddedKafkaDocker.TEST_TOPIC;


@Log4j2
@Category(TestCategoryUnitDocker.class)
class KafkaListenerTestsUIT extends TestUIT {
    @Autowired
    KafkaConsumer kafkaConsumer;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void givenKafkaBroker_whenSendingStringToTemplate_thenMessageReceived() {
        final String message = "Sending with default template";
        kafkaTemplate.send(TEST_TOPIC, UUID.randomUUID().toString(), message);
        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> assertThat(kafkaConsumer.getCopyMessages())
                        .as("Проверка содержимого сообщения ").contains("\"" + message + "\""));
        log.info("");
    }


    @Test
    void givenKafkaBroker_whenSendingDtoToTemplate_thenMessageReceived() {
        final KafkaTestDto testDto = KafkaTestDto.builder().value("1").value2("2").build();
        final KafkaTestDto testDto2 = KafkaTestDto.builder().value("2").value2("3").build();
        final List<KafkaTestDto> list = List.of(testDto, testDto2);
        list.forEach(it -> kafkaTemplate.send(TEST_TOPIC, UUID.randomUUID().toString(), it));
        final List<String> strings = list.stream().map(it -> {
            try {
                return objectMapper.writeValueAsString(it);
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> assertThat(kafkaConsumer.getCopyMessages())
                        .as("Проверка прихода нескольких сообщений").containsAll(strings));
    }
}
