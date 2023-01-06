package ru.polyroot.lib.kafka_api_starter.config.kafka.settings;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация-обёртка над {@link HashMap} служит для удобного подтягивания бинов
 * настроек для {@link org.springframework.kafka.core.ConsumerFactory} через DI.
 * DI на Map &lt; String, Object &gt; подтягивает бин как K-имя бина, V-объект бина,
 * * поэтому если так объявить настройки, то инжект делается не корректно
 * 
 * Value в мапе должно быть из {@link ConsumerConfig}.
 *
 * @author Александр Резов
 */
public class KafkaConsumerPropConfigImpl extends HashMap<String, Object> implements KafkaConsumerPropConfig {

    public KafkaConsumerPropConfigImpl() {
    }

    public KafkaConsumerPropConfigImpl(final Map<? extends String, ?> m) {
        super(m);
    }
}
