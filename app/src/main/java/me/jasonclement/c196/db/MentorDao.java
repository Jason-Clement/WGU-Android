package me.jasonclement.c196.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import me.jasonclement.c196.entities.Mentor;

@Dao
public interface MentorDao extends AppDao<Mentor> {

    @Query("SELECT * FROM mentors WHERE id = :id")
    LiveData<Mentor> get(int id);

    @Query("SELECT * FROM mentors ORDER BY name")
    LiveData<List<Mentor>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mentor t);

    @Delete
    void delete(Mentor t);

    @Query("DELETE FROM mentors")
    void deleteAll();
}
