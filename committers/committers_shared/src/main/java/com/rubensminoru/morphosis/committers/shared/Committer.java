package com.rubensminoru.morphosis.committers.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.entities.Entity;

public interface Committer extends Entity<Long> {
    public void write(ConsumerRecord consumerRecord);
    public void commit();
}
