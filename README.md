# Стартер для kafka

Общие классы, ускоряющее старт kafka для сервисов ([Вики проекта](https://wiki.domrf.ru/x/d5VfBw)).

Основной функционал:

1) Вводит зависимости и конфигурации приложения (pom / config)
2) Добавляется бины для работы с kafka.
    1) Producer.
    2) Consumer.
    3) KafkaAdmin.
    4) Handler ошибок в Consumer.
    5) Listener для обработки успешных/неудачных вызовов Producer.

## Замечания

1) Замечание: стартер стартует строго перед стартером KafkaAutoConfiguration.
2) Все бины стартера можно заменить своими бинами
3) Кроме настройки своих пропертей стартер учитывает стандартные проперти kafka (spring.kafka) для своих бинов, но
   заданные значения пропертей данного стартера в случае совпадения с пропертями из spring.kafka переопределяют их.

## Для запуска необходимо:

1) Подключить зависимость-стартер в сборщик проектов maven
2) На раннере приложения (Main.class) должна стоять аннотация @EnableAutoConfiguration или @SpringBootApplication,
   которая включает в себя @EnableAutoConfiguration
3) При старте в вашем приложении стартер потребует включить/выключить создание бинов для producer/consumer бинов
    1) kafka.producer-enable: true/false
    2) kafka.consumer-enable: true/false
4) Если включена kafka.producer-enable стартер потребует дополнительных настроек и создаст бины для работы kafka producer и KafkaAdmin
5) Если включена kafka.consumer-enable стартер потребует дополнительных настроек и создаст бины для работы kafka consumer
6) Если включены обе настройки, то стартер потребует дополнительных настроек и создаст бины для producer и consumer
7) Либо сделать spring-boot-starter-parent не ниже 2.6.4, поскольку версии ниже не могут работать с версией 
spring-kafka 2.8.3 и выше. Либо в pom явно указать зависимость spring-kafka 2.8.3 и выше. 

## Работа с библиотекой

1) Если включена настройка kafka.producer-enable, то можно использовать KafkaTemplate<String, Object>
   для отправки сообщений в kafka. А так же использовать KafkaAdmin.

```java
import org.springframework.kafka.core.KafkaTemplate;

class Sender {
    KafkaTemplate<String, Object> kafkaTemplate;
}
```

Настроенный бин KafkaTemplate<String, Object> имеет возможность отправлять данные в различные топики. Тип отправляемых
данных может быть как String, так и Object. При отправке String в kafka будет строка. При отправке любого составного
класса с полями в kafka будет JSON.

2) Если включена настройка kafka.consumer-enable. То можно использовать листенер сообщений из kafka.
Тип приходящего сообщения можно указывать любой, листенер при помощи ObjectMapper сам сериализует в нужный тип.
Так же можно использовать как тип приходящего String 

```java
import org.springframework.kafka.annotation.KafkaListener;


class Listener {
    @KafkaListener(topics = "YourTopic")
    void listener(final Dto dto) {
       
    }

   @KafkaListener(topics = "YourTopic2")
   void listener(final String dto) {

   }
}
```
@KafkaListener сразу подхватит бин KafkaListenerContainerFactory (по именованию бина "kafkaListenerContainerFactory") из данного стартера, 
поэтому не нужно указывать настройки containerFactory и groupId (уже приходит с настройками в бине "kafkaListenerContainerFactory")
~~@KafkaListener(containerFactory = "path.x", groupId = "id")~~, но возможность переназначить данные атрибуты всё равно остаётся.

Поскольку для KafkaListenerContainerFactory создана реализация ConcurrentKafkaListenerContainerFactory поэтому можно 
использовать асинхронное чтение добавив аннотацию @Async:

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;


class Listener {
   @Async
   @KafkaListener(topics = "YourTopic")
   void listener(final Dto dto) {

   }
}
```

Поскольку в ConsumerFactory используется StringDeserializer, а только в ConcurrentKafkaListenerContainerFactory 
массив байтов приводиться в объект при помощи MessageConvertor(который использует ObjectMapper). Нужно учитывать, что в обёртке
ConsumerRecord<KEY, VALUE>, которая может быть параметром в @KafkaListener VALUE всегда будет массивом байтов (String) независимо от того, какой объект туда передан.
Для десериализации разных объектов в одном топике, приходящих из кафка можно использовать два подхода:
   1) Внутри листенера руками парсить String при помощи ObjectMapper
   2) Принимать обобщенный объект и парсить логикой ObjectMapper при помощи @JsonTypeInfo, @JsonTypeIdResolver

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;


class Listener {
   @KafkaListener(topics = "YourTopic")
   public void listener(final ConsumerRecord<String, String> message) {

   }
}
```






