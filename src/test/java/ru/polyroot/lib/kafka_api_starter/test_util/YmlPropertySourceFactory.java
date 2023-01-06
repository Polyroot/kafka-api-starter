package ru.polyroot.lib.kafka_api_starter.test_util;

import lombok.NonNull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.Properties;


/**
 * Фабрика ресурсов для yml файлов конфигурации, нужна для того, чтобы в аннотациях
 * {@link org.springframework.context.annotation.PropertySource} работала с ym файлами.
 * <br>
 * <br>
 * Пример:
 * {@code org.springframework.context.annotation.@PropertySource(
 * value = "classpath:property.yml",
 * factory = YmlPropertySourceFactory.class)}
 *
 * @author Даниил Староверов
 */
public class YmlPropertySourceFactory implements PropertySourceFactory {

    @Override
    @NonNull
    public PropertySource<?> createPropertySource(@Nullable final String name,
                                                  @NonNull final EncodedResource encodedResource) {
        final YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        final Resource resource = encodedResource.getResource();
        factory.setResources(resource);

        final Properties properties = factory.getObject();
        final String filename = resource.getFilename();

        return new PropertiesPropertySource(
                Objects.requireNonNull(filename),
                Objects.requireNonNull(properties));
    }
}