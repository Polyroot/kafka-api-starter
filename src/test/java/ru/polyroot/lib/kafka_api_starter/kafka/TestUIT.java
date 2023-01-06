package ru.polyroot.lib.kafka_api_starter.kafka;


import lombok.extern.log4j.Log4j2;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import ru.polyroot.lib.kafka_api_starter.test_util.test_categories.TestCategoryUnitDocker;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@SpringBootTest
@ActiveProfiles("docker-kafka-test")
@Category(TestCategoryUnitDocker.class)
class TestUIT {

    @Autowired
    protected KafkaAdmin kafkaAdmin;
    @Autowired
    protected KafkaContainer kafkaContainer;
    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testContainer() {
        assertTrue(kafkaContainer.isRunning(), "Контейнер Kafka должен быть запущен");
    }
}
