package com.rubensminoru.morphosis.consumers.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;

import java.util.List;

public interface Consumer {
    void initialize();

    List<ConsumerRecord> consume();
}
