package me.jasonclement.c196.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.entities.Mentor;

public class MentorRepository {

    private MentorDao dao;
    private AppAsyncTask<Mentor> asyncTask;
    private LiveData<List<Mentor>> allItems;

    public MentorRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.mentorDao();
        asyncTask = new AppAsyncTask<>(dao);
        allItems = dao.getAll();
    }

    public LiveData<List<Mentor>> getAll() {
        return allItems;
    }

    public LiveData<Mentor> get(int id) { return dao.get(id); }

    public void insert(Mentor t) {
        asyncTask.insert(t);
    }

    public void delete(Mentor t) {
        asyncTask.delete(t);
    }

}
