package com.rubensminoru.morphosis.committers.s3_committer.entities;

import com.rubensminoru.morphosis.consumers.shared.entities.Record;

import java.util.List;

public interface FileWriter {
    public void write(List<Record> records);

    public void commit();
}
