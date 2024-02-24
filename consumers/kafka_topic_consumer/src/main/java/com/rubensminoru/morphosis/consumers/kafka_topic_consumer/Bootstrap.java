package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


public class Bootstrap {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: Bootstrap <bootstrap_servers> <topic>");
            System.exit(1);
        }

        String bootstrapServers = args[0];
        String topic = args[1];

        // Set properties for the Kafka producer
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Create a Kafka producer
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            // Produce some sample data to Kafka
            for (int i = 0; i < 10; i++) {
                String key = "key" + i;
                String value = "value" + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record);
                System.out.println("Produced: " + record);
            }

            producer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
