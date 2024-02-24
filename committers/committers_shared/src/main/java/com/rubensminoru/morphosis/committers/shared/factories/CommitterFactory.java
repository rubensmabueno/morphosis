package com.rubensminoru.morphosis.committers.shared.factories;

import com.rubensminoru.morphosis.committers.shared.Committer;
import com.rubensminoru.morphosis.shared.entities.Entity;

public interface CommitterFactory extends Entity<Long> {
    public Committer create();
}
