package ru.polyroot.lib.kafka_api_starter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import ru.polyroot.lib.kafka_api_starter.test_util.YmlPropertySourceFactory;

import static org.springframework.boot.SpringApplication.run;

/**
 * Раннер, для запуска ApplicationContext который подхватит @SpringBootTest.
 * <p>
 * Необходим, чтобы конфигурации и бины из библиотеки поднялись в контексте и их можно было протестировать
 * Не приезжает в jar сборку приложения. Не мешает основному раннеру приложения
 *
 * @author Александр Резов
 */
@PropertySource(
        value = "classpath:application-docker-kafka-test.yml",
        factory = YmlPropertySourceFactory.class)
@SpringBootApplication
public class MainTestContextSpring {
    public static void main(final String[] args) {
        run(MainTestContextSpring.class);
    }
}
