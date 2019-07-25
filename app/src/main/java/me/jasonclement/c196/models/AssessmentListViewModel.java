package me.jasonclement.c196.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.AssessmentRepository;
import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;

public class AssessmentListViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allItems;

    public AssessmentListViewModel(Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allItems = repository.getAll();
    }

    public LiveData<List<Assessment>> getAll() { return allItems; }
}
