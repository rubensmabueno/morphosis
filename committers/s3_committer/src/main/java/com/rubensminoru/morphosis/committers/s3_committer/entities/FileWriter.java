package com.rubensminoru.morphosis.committers.s3_committer.entities;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;

import java.util.List;

public interface FileWriter {
    void write(List<RecordMessage> records);

    void commit();
}
