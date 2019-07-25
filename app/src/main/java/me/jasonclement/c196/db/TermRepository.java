package me.jasonclement.c196.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.entities.Term;

public class TermRepository {

    private TermDao dao;
    private AppAsyncTask<Term> asyncTask;
    private LiveData<List<Term>> allItems;

    public TermRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.termDao();
        asyncTask = new AppAsyncTask<>(dao);
        allItems = dao.getAll();
    }

    public LiveData<List<Term>> getAll() {
        return allItems;
    }

    public LiveData<Term> get(int id) { return dao.get(id); }

    public void insert(Term t) {
        asyncTask.insert(t);
    }

    public void delete(Term t) {
        asyncTask.delete(t);
    }

}
