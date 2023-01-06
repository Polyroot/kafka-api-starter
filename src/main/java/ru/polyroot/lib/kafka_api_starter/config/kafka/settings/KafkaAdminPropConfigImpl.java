package ru.polyroot.lib.kafka_api_starter.config.kafka.settings;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация-обёртка над {@link HashMap} служит для удобного подтягивания бинов
 * настроек для {@link org.springframework.kafka.core.KafkaAdmin} через DI.
 * DI на Map&lt;String, Object&gt; подтягивает бин как K-имя бина, V-объект бина,
 * поэтому если так объявить настройки, то инжект делается не корректно
 *
 * Value в мапе должно быть из {@link AdminClientConfig}.
 *
 * @author Александр Резов
 */
public class KafkaAdminPropConfigImpl extends HashMap<String, Object> implements KafkaAdminPropConfig {

    public KafkaAdminPropConfigImpl() {
    }

    public KafkaAdminPropConfigImpl(final Map<? extends String, ?> m) {
        super(m);
    }
}
