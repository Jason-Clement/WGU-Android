package me.jasonclement.c196.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import me.jasonclement.c196.entities.Assessment;
import me.jasonclement.c196.entities.Course;
import me.jasonclement.c196.entities.Mentor;

@Dao
public interface CourseDao extends AppDao<Course> {

    @Query("SELECT * FROM courses WHERE id = :id")
    LiveData<Course> get(int id);

    @Query("SELECT * FROM courses ORDER BY startDate")
    LiveData<List<Course>> getAll();

    @Query("SELECT * FROM courses WHERE termId IS NULL OR termId = :termId ORDER BY startDate")
    LiveData<List<Course>> getAvailableForTerm(int termId);

    @Query("SELECT * FROM courses WHERE termId IS NULL ORDER BY startDate")
    LiveData<List<Course>> getUnenrolled();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course t);

    @Delete
    void delete(Course t);

    @Query("DELETE FROM courses")
    void deleteAll();
}
