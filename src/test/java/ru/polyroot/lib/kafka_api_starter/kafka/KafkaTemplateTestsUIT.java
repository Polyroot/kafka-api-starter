package ru.polyroot.lib.kafka_api_starter.kafka;

import lombok.extern.log4j.Log4j2;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.polyroot.lib.kafka_api_starter.test_model.KafkaTestDto;
import ru.polyroot.lib.kafka_api_starter.test_util.test_categories.TestCategoryUnitDocker;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.polyroot.lib.kafka_api_starter.test_config.ConfigEmbeddedKafkaDocker.TEST_TOPIC;

@Log4j2
@Category(TestCategoryUnitDocker.class)
class KafkaTemplateTestsUIT extends TestUIT {

    @Test
    void givenStringMessage_whenSendToKafka_thenReturnSuccess() throws ExecutionException, InterruptedException {

        final ListenableFutureCallback<SendResult<String, Object>> successCallback =
                mock(ListenableFutureCallback.class);

        final ListenableFuture<SendResult<String, Object>> responce =
                kafkaTemplate.send(TEST_TOPIC, UUID.randomUUID().toString(), "String");
        responce.addCallback(successCallback);
        final SendResult<String, Object> result = responce.get();
        assertNotNull(result, "При успешной отправке результат не может быть пуст");
        assertTrue(result.getProducerRecord().value().toString().contains("String"),
                "Сообщение в строке должно быть равно отправленному сообщению");
        log.info("Send message ok: {}", result);
    }

    @Test
    void givenDtoMessage_whenSendToKafka_thenReturnSuccess() throws ExecutionException, InterruptedException {

        final KafkaTestDto testDto = KafkaTestDto.builder().value("param1").value2("param2").build();

        final ListenableFutureCallback<SendResult<String, Object>> successCallback =
                mock(ListenableFutureCallback.class);

        final ListenableFuture<SendResult<String, Object>> responce =
                kafkaTemplate.send(TEST_TOPIC, UUID.randomUUID().toString(), testDto);

        responce.addCallback(successCallback);
        responce.get();

        verify(successCallback, times(1)).onSuccess(any());
        verify(successCallback, times(0)).onFailure(any());
    }
}
