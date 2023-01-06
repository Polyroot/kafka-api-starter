package ru.polyroot.lib.kafka_api_starter.test_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaTestDto {
    private String value;
    private String value2;
}
