package me.jasonclement.c196.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ListRepository<T> {

    private AppDao<T> dao;
    private LiveData<List<T>> allItems;

    public ListRepository(Application application, AppDao<T> dao) {
        this.dao = dao;
        allItems = dao.getAll();
    }

    public LiveData<List<T>> getAll() {
        return allItems;
    }

}
