package com.rubensminoru.morphosis.consumers.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;

import java.util.List;

public interface Consumer {
    public void initialize();

    public List<ConsumerRecord> consume();
}
