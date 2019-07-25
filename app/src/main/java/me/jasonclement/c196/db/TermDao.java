package me.jasonclement.c196.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Term;

@Dao
public interface TermDao extends AppDao<Term> {

    @Query("SELECT * FROM terms WHERE id = :id ORDER BY startDate")
    LiveData<Term> get(int id);

    @Query("SELECT * FROM terms ORDER BY startDate")
    LiveData<List<Term>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Term t);

    @Delete
    void delete(Term t);

    @Query("DELETE FROM terms")
    void deleteAll();
}
