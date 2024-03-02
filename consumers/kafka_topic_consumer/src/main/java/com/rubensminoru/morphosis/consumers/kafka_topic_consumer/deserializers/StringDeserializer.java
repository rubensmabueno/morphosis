package com.rubensminoru.morphosis.consumers.kafka_topic_consumer.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import com.rubensminoru.morphosis.shared.entities.fields.StringField;
import com.rubensminoru.morphosis.shared.entities.values.StringValue;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class StringDeserializer implements Deserializer<RecordMessage> {
    private final ObjectMapper objectMapper;

    public StringDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    public RecordMessage deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        String value = new String(data, StandardCharsets.UTF_8);

        return new RecordMessage(
                new Schema(new StringField("value")),
                new StringValue(value)
        );
    }

    public RecordMessage deserialize(String topic, Headers headers, ByteBuffer data) {
        if (data == null) {
            return null;
        }

        String value = data.hasArray() ? new String(data.array(), data.position() + data.arrayOffset(), data.remaining(), StandardCharsets.UTF_8) : new String(Utils.toArray(data), StandardCharsets.UTF_8);

        return new RecordMessage(
                new Schema(new StringField("value")),
                new StringValue(value)
        );
    }
}
