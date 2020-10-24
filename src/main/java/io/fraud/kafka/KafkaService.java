package io.fraud.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.messages.GeneratorMessage;
import io.fraud.kafka.producer.KafkaMessageProducer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.apache.kafka.clients.producer.RecordMetadata;

@Getter
public class KafkaService {
    private final KafkaMessageProducer kafkaMessageProducer;
    private final KafkaMessageConsumer kafkaMessageConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    public KafkaService() {
        this.kafkaMessageProducer = new KafkaMessageProducer(projectConfig.kafkaBrokers());
        this.kafkaMessageConsumer = new KafkaMessageConsumer(projectConfig.kafkaBrokers());
    }

    @SneakyThrows
    public GeneratorMessage send() {
        String message = testDataGenerator.generate("data/message.twig");
        send(projectConfig.queuingTopic(), message);
        return objectMapper.readValue(message, GeneratorMessage.class);
    }

    public RecordMetadata send(String topic, String message) {
        return kafkaMessageProducer.send(topic, message);
    }

    @SneakyThrows
    public RecordMetadata send(String topic, Object message) {
        return send(topic,
                objectMapper.writeValueAsString(message));
    }

    @SneakyThrows
    public RecordMetadata send(Object message) {
        return send(projectConfig.queuingTopic(),
                objectMapper.writeValueAsString(message));
    }

    public void subscribe(String topic) {
        kafkaMessageConsumer.subscribe(topic);
        kafkaMessageConsumer.consume();
    }


    public void subscribeLegitTopic() {
        subscribe(projectConfig.legitTopic());
    }

    public void subscribeFraudTopic() {
        subscribe(projectConfig.fraudTopic());
    }

    public KafkaRecord waitForMessage(String message) {
        return kafkaMessageConsumer.waitForMessage(message);
    }
}
