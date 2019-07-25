package me.jasonclement.c196.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.entities.Course;

public class CourseListViewModel extends AndroidViewModel {

    private CourseRepository repository;
    private LiveData<List<Course>> allItems;

    public CourseListViewModel(Application application) {
        super(application);
        repository = new CourseRepository(application);
        allItems = repository.getAll();
    }

    public LiveData<List<Course>> getAll() { return allItems; }
}
