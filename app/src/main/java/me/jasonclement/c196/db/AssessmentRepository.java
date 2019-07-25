package me.jasonclement.c196.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.entities.Assessment;

public class AssessmentRepository {

    private AssessmentDao dao;
    private AppAsyncTask asyncTask;
    private LiveData<List<Assessment>> allItems;

    public AssessmentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.assessmentDao();
        asyncTask = new AppAsyncTask(dao);
        allItems = dao.getAll();
    }

    public LiveData<List<Assessment>> getAll() {
        return allItems;
    }

    public LiveData<Assessment> get(int id) { return dao.get(id); }

    public void insert(Assessment t) {
        asyncTask.insert(t);
    }

    public void delete(Assessment t) {
        asyncTask.delete(t);
    }

}
