package com.rubensminoru.morphosis.committers.s3_committer;

import com.rubensminoru.morphosis.committers.s3_committer.entities.FileWriter;
import com.rubensminoru.morphosis.committers.s3_committer.entities.ParquetFileWriter;
import com.rubensminoru.morphosis.committers.shared.Committer;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;
import com.rubensminoru.morphosis.shared.entities.RecordMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommitterImpl extends GenericEntity implements Committer {
    private final FileWriter fileWriter;
    List<RecordMessage> inMemoryRecords = new ArrayList<>();

    public CommitterImpl() throws IOException {
        super();

        this.fileWriter = new ParquetFileWriter("/home/rubensminoru/Development/morphosis/tmp/" + UUID.randomUUID() + ".parquet");
    }

    public void write(ConsumerRecord consumerRecord) {
        RecordMessage record = consumerRecord.getMessage();

        inMemoryRecords.add(record);

        if (inMemoryRecords.size() >= 1000) {
            flush();
        }
    }

    private void flush() {
        this.fileWriter.write(inMemoryRecords);

        inMemoryRecords.clear();
    }

    public void commit() {
        flush();

        this.fileWriter.commit();
    }
}
