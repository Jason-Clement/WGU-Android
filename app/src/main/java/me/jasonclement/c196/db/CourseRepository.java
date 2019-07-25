package me.jasonclement.c196.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.entities.Course;

public class CourseRepository {

    private CourseDao dao;
    private AppAsyncTask asyncTask;
    private LiveData<List<Course>> allItems;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.courseDao();
        asyncTask = new AppAsyncTask(dao);
        allItems = dao.getAll();
    }

    public LiveData<Course> get(int id) {
        return dao.get(id);
    }

    public LiveData<List<Course>> getAll() {
        return allItems;
    }

    public LiveData<List<Course>> getUnenrolled() {
        return dao.getUnenrolled();
    }

    public LiveData<List<Course>> getAvailableForTerm(int termId) {
        return dao.getAvailableForTerm(termId);
    }

    public void insert(Course t) {
        asyncTask.insert(t);
    }

    public void delete(Course t) {
        asyncTask.delete(t);
    }
}
