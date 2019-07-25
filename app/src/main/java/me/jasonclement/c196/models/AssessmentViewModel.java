package me.jasonclement.c196.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.AssessmentRepository;
import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository assessmentRepository;
    private CourseRepository courseRepository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = new AssessmentRepository(application);
        courseRepository = new CourseRepository(application);
    }

    public LiveData<Assessment> get(int id) {
        return assessmentRepository.get(id);
    }

    public LiveData<List<Course>> getCourses() {
        return courseRepository.getAll();
    }

    public void save(Assessment assessment) {
        assessmentRepository.insert(assessment);
    }

    public void delete(Assessment assessment) { assessmentRepository.delete(assessment);}
}
