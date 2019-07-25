package me.jasonclement.c196.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Term;
import me.jasonclement.c196.db.TermRepository;

public class TermViewModel extends AndroidViewModel {

    private TermRepository termRepository;
    private CourseRepository courseRepository;

    public TermViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
        courseRepository = new CourseRepository(application);
    }

    public LiveData<Term> getTerm(int termId) {
        return termRepository.get(termId);
    }

    public LiveData<List<Course>> getCourses() {
        return courseRepository.getAll();
    }

    public void save(Term term, List<Course> courses) {
        termRepository.insert(term);
        for (Course course : courses)
            courseRepository.insert(course);
    }

    public void delete(Term term, List<Course> courses) {
        for (Course course : courses) {
            if (course.getTermId() == term.getId()) {
                course.setTermId(null);
                courseRepository.insert(course);
            }
        }
        termRepository.delete(term);
    }
}
