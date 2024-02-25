package com.rubensminoru.morphosis.committers.s3_committer.entities;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;

import com.rubensminoru.morphosis.shared.entities.fields.Field;
import com.rubensminoru.morphosis.shared.entities.fields.IntegerField;
import com.rubensminoru.morphosis.shared.entities.fields.RecordField;
import com.rubensminoru.morphosis.shared.entities.fields.StringField;
import com.rubensminoru.morphosis.shared.entities.values.IntegerValue;
import com.rubensminoru.morphosis.shared.entities.values.RecordValue;
import com.rubensminoru.morphosis.shared.entities.values.StringValue;
import com.rubensminoru.morphosis.shared.entities.values.Value;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.*;

import java.util.Map;

public class RecordWriterSupport extends WriteSupport<RecordMessage> {
    private final MessageType messageType;
    private RecordConsumer recordConsumer;

    public RecordWriterSupport(Schema schema) {
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
    public void write(RecordMessage message) {
        recordConsumer.startMessage();

        writeFields((RecordValue) message.getValue(), 0);

        recordConsumer.endMessage();
    }

    private int writeFields(RecordValue recordValue, int position) {
        for (Map.Entry<Field, Value> fieldValue : recordValue.getFields().entrySet()) {
            Field field = fieldValue.getKey();
            Value value = fieldValue.getValue();
            String name = field.getName();

            recordConsumer.startField(name, position);

            if (value instanceof RecordValue) {
                recordConsumer.startGroup();
                position = writeFields((RecordValue) value, 0);
                recordConsumer.endGroup();
            } else if (value instanceof StringValue) {
                String stringValue = ((StringValue) value).getValue();
                recordConsumer.addBinary(Binary.fromString(stringValue));
                position++;
            } else if (value instanceof IntegerValue) {
                int intValue = ((IntegerValue) value).getValue();
                recordConsumer.addInteger(intValue);
                position++;
            }

            recordConsumer.endField(name, position);
        }
        return position;
    }

    private MessageType parseSchema(Schema schema) {
        Types.GroupBuilder<MessageType> messageTypeBuilder = Types.buildMessage();
        Field field = schema.getField();

        for (Field innerField : ((RecordField) field).getFields()) {
            messageTypeBuilder.addField(createType(innerField));
        }

        return messageTypeBuilder.named("root");
    }

    private Type createType(Field field) {
        if (field instanceof RecordField) {
            return new GroupType(
                    Type.Repetition.OPTIONAL,
                    field.getName(),
                    ((RecordField) field).getFields().stream().map(this::createType).toList()
            );
        } else if (field instanceof StringField) {
            return new PrimitiveType(
                    Type.Repetition.OPTIONAL,
                    PrimitiveType.PrimitiveTypeName.BINARY,
                    field.getName(),
                    OriginalType.UTF8
            );
        } else if (field instanceof IntegerField) {
            return new PrimitiveType(
                    Type.Repetition.OPTIONAL,
                    PrimitiveType.PrimitiveTypeName.INT32,
                    field.getName()
            );
        } else {
            throw new RuntimeException();
        }
    }
}
