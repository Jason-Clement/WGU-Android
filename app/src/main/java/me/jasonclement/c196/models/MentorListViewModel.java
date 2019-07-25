package me.jasonclement.c196.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.MentorRepository;
import me.jasonclement.c196.entities.Mentor;

public class MentorListViewModel extends AndroidViewModel {

    private MentorRepository repository;
    private LiveData<List<Mentor>> allItems;

    public MentorListViewModel(Application application) {
        super(application);
        repository = new MentorRepository(application);
        allItems = repository.getAll();
    }

    public LiveData<List<Mentor>> getAll() { return allItems; }
}
