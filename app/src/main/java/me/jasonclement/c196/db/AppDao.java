package me.jasonclement.c196.db;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface AppDao<T> {
    LiveData<T> get(int id);
    LiveData<List<T>> getAll();
    void insert(T t);
    void delete(T t);
    void deleteAll();
}
