package me.jasonclement.c196.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.jasonclement.c196.db.TermRepository;
import me.jasonclement.c196.entities.Term;

public class TermListViewModel extends AndroidViewModel {

    private TermRepository repository;
    private LiveData<List<Term>> allItems;

    public TermListViewModel(Application application) {
        super(application);
        repository = new TermRepository(application);
        allItems = repository.getAll();
    }

    public LiveData<List<Term>> getAll() { return allItems; }
}
