package ru.polyroot.lib.kafka_api_starter.test_config;

import lombok.Getter;
import lombok.Setter;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.utility.DockerImageName;

/**
 * Конфигурация для kafka docker контейнера
 *
 * @author Александр Резов
 */
@Configuration
@ConfigurationProperties(prefix = "test.kafka.docker.image")
@Profile("docker-kafka-test")
@Getter
@Setter
public class DockerKafkaImageProperties {
    private String dockerHub;
    private String url;

    /**
     * Получение пути к image. Если относитльный url {@link url} пуст,
     * тогда возьмёт image из DockerHub {@link dockerHub}
     */
    public DockerImageName getUrlByPriority() {
        if (StringUtils.isBlank(url)) {
            return DockerImageName.parse(dockerHub);
        }

        return DockerImageName.parse(url).asCompatibleSubstituteFor("confluentinc/cp-kafka:6.2.0");
    }
}
