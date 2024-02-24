package com.rubensminoru.morphosis.committers.s3_committer;

import com.rubensminoru.morphosis.committers.s3_committer.entities.FileWriter;
import com.rubensminoru.morphosis.committers.s3_committer.entities.ParquetFileWriter;
import com.rubensminoru.morphosis.committers.s3_committer.entities.Schema;
import com.rubensminoru.morphosis.committers.shared.Committer;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.consumers.shared.entities.Record;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommitterImpl extends GenericEntity implements Committer {
    private final FileWriter fileWriter;
    List<Record> inMemoryRecords = new ArrayList<>();

    public CommitterImpl() throws IOException {
        super();

        this.fileWriter = new ParquetFileWriter("/home/rubensminoru/Development/morphosis/tmp/1.parquet", new Schema());
    }

    public void write(ConsumerRecord consumerRecord) {
        Record record = consumerRecord.getRecord();

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
