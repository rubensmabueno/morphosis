package com.rubensminoru.morphosis.shared.repositories;

import com.rubensminoru.morphosis.shared.entities.Entity;

import java.util.*;


public class InMemoryRepository<U, T extends Entity<U>> {
    private final Map<U, T> entities;

    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }

    public InMemoryRepository(Map<U, T> entities) {
        this.entities = entities;
    }

    public InMemoryRepository(List<T> entities) {
        this.entities = new HashMap<>();

        for (T entity : entities) {
            this.entities.put(entity.getId(), entity);
        }
    }

    public List<T> all() { return entities.values().stream().toList(); }

    public T add(T entity) {
        this.entities.put(entity.getId(), entity);
        return entity;
    }

    public T add(U id, T entity) {
        this.entities.put(id, entity);
        return entity;
    }

    public T find(U id) {
        return this.entities.get(id);
    }

    public T findOrAdd(T entity) {
        if (this.entities.containsKey(entity.getId())) {
            return find(entity.getId());
        } else {
            return add(entity);
        }
    }

    public T findOrAdd(U id, T entity) {
        if (this.entities.containsKey(id)) {
            return find(id);
        } else {
            return add(id, entity);
        }
    }
}
