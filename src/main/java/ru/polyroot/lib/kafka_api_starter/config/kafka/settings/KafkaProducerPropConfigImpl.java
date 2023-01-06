package ru.polyroot.lib.kafka_api_starter.config.kafka.settings;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация-обёртка над {@link HashMap} служит для удобного подтягивания бинов
 * настроек для {@link org.springframework.kafka.core.ProducerFactory} через DI.
 * DI на Map &lt; String, Object &gt; подтягивает бин как K-имя бина, V-объект бина,
 * * поэтому если так объявить настройки, то инжект делается не корректно
 * 
 * <p>
 * Value в мапе должно быть из {@link ProducerConfig}.
 *
 * @author Александр Резов
 */
public class KafkaProducerPropConfigImpl extends HashMap<String, Object> implements KafkaProducerPropConfig {

    public KafkaProducerPropConfigImpl() {
    }

    public KafkaProducerPropConfigImpl(final Map<? extends String, ?> m) {
        super(m);
    }
}
