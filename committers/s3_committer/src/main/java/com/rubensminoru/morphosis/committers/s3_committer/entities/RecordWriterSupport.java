package com.rubensminoru.morphosis.committers.s3_committer.entities;

import com.rubensminoru.morphosis.consumers.shared.entities.Record;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

import java.util.Map;

public class RecordWriterSupport extends WriteSupport<Record> {
    private final MessageType messageType;
    private Schema schema;
    private RecordConsumer recordConsumer;

    public RecordWriterSupport(Schema schema) {
        this.schema = schema;
        this.messageType = parseSchema(schema);
    }

    @Override
    public WriteSupport.WriteContext init(Configuration configuration) {
        return new WriteSupport.WriteContext(messageType, new java.util.HashMap<String, String>());
    }

    @Override
    public void prepareForWrite(RecordConsumer recordConsumer) {
        this.recordConsumer = recordConsumer;
    }

    @Override
    public void write(Record record) {
        int i = 0;

        recordConsumer.startMessage();

        for (Map.Entry<String, String> attributeEntry : record.getAttributes().entrySet()) {
            String type = schema.getType(attributeEntry.getKey());

            if (type.equals("string")) {
                recordConsumer.startField(attributeEntry.getKey(), i);
                recordConsumer.addBinary(Binary.fromString(attributeEntry.getValue()));
                recordConsumer.endField(attributeEntry.getKey(), i);
            } else {
                throw new RuntimeException("Not supported type: " + type);
            }

            i += 1;
        }

        recordConsumer.endMessage();
    }

    private MessageType parseSchema(Schema schema) {
        Types.GroupBuilder<MessageType> messageTypeBuilder = Types.buildMessage();

        for (Map.Entry<String, String> fieldEntry : schema.getFields().entrySet()) {
            if (fieldEntry.getValue().equals("string")) {
                messageTypeBuilder = messageTypeBuilder
                        .optional(PrimitiveType.PrimitiveTypeName.BINARY)
                        .as(OriginalType.UTF8)
                        .named(fieldEntry.getKey());
            }
        }

        return messageTypeBuilder.named("schema");
    }
}
