package com.example.befit.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.befit.entity.Record;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(Record record);

    @Update
    void update(Record record);

    @Delete
    void delete(Record record);

    @Query("SELECT * FROM record")
    LiveData<List<Record>> getAllRecords();

    @Query("SELECT * FROM record WHERE customerEmail = :customerEmail")
    LiveData<List<Record>> getCutsomerRecords(String customerEmail);

    @Query("SELECT * FROM record WHERE customerEmail = :email AND date_show BETWEEN :startDate AND :endDate")
    List<Record> getWeightsBetweenDates(String email, String startDate, String endDate);

}

