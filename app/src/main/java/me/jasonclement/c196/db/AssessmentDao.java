package me.jasonclement.c196.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import me.jasonclement.c196.entities.Assessment;

@Dao
public interface AssessmentDao extends AppDao<Assessment> {

    @Query("SELECT * FROM assessments WHERE id = :id")
    LiveData<Assessment> get(int id);

    @Query("SELECT * FROM assessments ORDER BY goalDate")
    LiveData<List<Assessment>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assessment t);

    @Delete
    void delete(Assessment t);

    @Query("DELETE FROM assessments")
    void deleteAll();
}
