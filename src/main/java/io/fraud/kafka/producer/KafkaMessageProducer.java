package io.fraud.kafka.producer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaMessageProducer {
    public static KafkaProducer<String, String> kafkaProducer;
    public final String bootStrapServer;

    public KafkaMessageProducer(String bootStrapServer) {
        this.bootStrapServer = bootStrapServer;
        createProducer();
    }

    private KafkaMessageProducer createProducer() {
        Properties properties = createProducerProperties();
        if (kafkaProducer == null) {
            kafkaProducer = new KafkaProducer<>(properties);
        }
        return this;
    }

    private Properties createProducerProperties() {
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                this.bootStrapServer);
        prop.put(ProducerConfig.CLIENT_ID_CONFIG, "TkKafkaProducer");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return prop;
    }

    @SneakyThrows
    public RecordMetadata send(String topicName, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topicName, message);
        return kafkaProducer.send(record).get();
    }
}
