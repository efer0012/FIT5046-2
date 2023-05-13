package com.example.befit.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.befit.entity.Classes;

import java.util.List;

@Dao
public interface ClassesDao {
    @Insert
    void insert(Classes beFitClass);

    @Update
    void update(Classes beFitClass);

    @Delete
    void delete(Classes beFitClass);

    @Query("SELECT * FROM classes")
    LiveData<List<Classes>> getAllClasses();

    @Query("SELECT * FROM classes WHERE day = :day")
    LiveData<List<Classes>> findByDay(String day);

    @Query("DELETE FROM classes")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM classes")
    int getTotalClasses();

    @Query("SELECT * FROM classes WHERE classId = :classId")
    Classes getClassById(int classId);
}
