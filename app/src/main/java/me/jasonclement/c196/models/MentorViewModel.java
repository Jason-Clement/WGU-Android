package me.jasonclement.c196.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.MentorRepository;
import me.jasonclement.c196.db.CourseRepository;
import me.jasonclement.c196.entities.Mentor;
import me.jasonclement.c196.entities.Course;

public class MentorViewModel extends AndroidViewModel {

    private MentorRepository mentorRepository;
    private CourseRepository courseRepository;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        mentorRepository = new MentorRepository(application);
        courseRepository = new CourseRepository(application);
    }

    public LiveData<Mentor> get(int id) {
        return mentorRepository.get(id);
    }

    public LiveData<List<Course>> getCourses() {
        return courseRepository.getAll();
    }

    public void save(Mentor mentor) {
        mentorRepository.insert(mentor);
    }

    public void delete(Mentor mentor) {
        mentorRepository.delete(mentor);
    }
}
