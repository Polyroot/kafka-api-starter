package ru.polyroot.lib.kafka_api_starter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * Настройки consumer.
 *
 * @author Александр Резов
 */
@Validated
@Getter
@Setter
public class KafkaConsumerApiLibProperties {

    /**
     * BootstrapAddress для kafka.
     * 
     * Пример: localhost:9092.
     */
    @NotBlank
    private String bootstrapAddress;

    /**
     * Id группы вашего сервиса в кафке.
     * 
     * Необходим для того, чтобы кафка знала с
     * какого сервиса в неё ходят и правильно распределяла нагрузку между микросервиссами.
     */
    @NotBlank
    private String groupId;

    /**
     * Фиксация смещения
     * 
     * По умолчанию {@link AutoOffsetResetType#LATEST}
     */
    private AutoOffsetResetType autoOffsetReset = AutoOffsetResetType.LATEST;

    /**
     * Настройки включения/выключения автоматического коммита.
     * 
     * Если включена, то потребитель
     * будет периодически автоматически фиксировать смещения с интервалом,
     * установленным параметром auto.commit.interval.ms. По умолчанию 5 секунд.
     *
     * По умолчанию true
     */
    private Boolean enableAutoCommit = Boolean.TRUE;

    /**
     * Настройки интервала автоматического коммита.
     * 
     * Работает, только если включен авто-коммит.
     * @see {@link KafkaConsumerApiLibProperties#enableAutoCommit}
     * 
     * По умолчанию 5 секунд
     */
    private Duration autoCommitIntervalMs = Duration.ofSeconds(5);

    public enum AutoOffsetResetType {
        EARLIEST("earliest"),
        EXCEPTION("exception"),
        LATEST("latest"),
        NONE("none");

        private final String value;

        AutoOffsetResetType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
