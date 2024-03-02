package com.rubensminoru.morphosis.shared.entities;

import com.rubensminoru.morphosis.shared.entities.fields.Field;
import com.rubensminoru.morphosis.shared.entities.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Pair<T1, T2> {
    private T1 t1;
    private T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getLeft() { return t1; };

    public T2 getRight() { return t2; };

    public static <T, U> List<Pair<T, U>> zip(Set<T> set1, Set<U> set2) {
        List<T> list1 = set1 != null ? new ArrayList<>(set1) : new ArrayList<>();
        List<U> list2 = set2 != null ? new ArrayList<>(set2) : new ArrayList<>();

        List<Pair<T, U>> result = new ArrayList<>();
        int size = Math.max(list1.size(), list2.size());
        for (int i = 0; i < size; i++) {
            T value1 = i < list1.size() ? list1.get(i) : null;
            U value2 = i < list2.size() ? list2.get(i) : null;
            result.add(new Pair<>(value1, value2));
        }
        return result;
    }
}
