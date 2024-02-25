package com.rubensminoru.morphosis.shared.entities;


import java.util.UUID;

public class GenericEntity implements Entity<Long> {
    private final Long id;

    public GenericEntity() {
        this.id = UUID.randomUUID().getMostSignificantBits();
    }

    public Long getId() {
        return id;
    }
}
