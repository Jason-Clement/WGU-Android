package me.jasonclement.c196.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.AssessmentRepository;
import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.db.MentorRepository;
import me.jasonclement.c196.db.TermRepository;
import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Mentor;
import me.jasonclement.c196.entities.Term;

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository courseRepository;
    private TermRepository termRepository;
    private AssessmentRepository assessmentRepository;
    private MentorRepository mentorRepository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        termRepository = new TermRepository(application);
        assessmentRepository = new AssessmentRepository(application);
        mentorRepository = new MentorRepository(application);
    }

    public LiveData<Course> getCourse(int courseId) {
        return courseRepository.get(courseId);
    }

    public LiveData<List<Term>> getTerms() {
        return termRepository.getAll();
    }

    public LiveData<List<Assessment>> getAssessments() { return assessmentRepository.getAll(); }

    public LiveData<List<Mentor>> getMentors() { return mentorRepository.getAll(); }

    public void save(Course course, List<Assessment> assessments, List<Mentor> mentors) {
        courseRepository.insert(course);
        for (Assessment assessment : assessments)
            assessmentRepository.insert(assessment);
        for (Mentor mentor : mentors)
            mentorRepository.insert(mentor);
    }

    public void delete(Course course) {
        courseRepository.delete(course);
    }
}
