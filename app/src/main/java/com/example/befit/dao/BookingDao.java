package com.example.befit.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.befit.entity.Booking;
import com.example.befit.model.BookingWithClass;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insert(Booking booking);

    @Update
    void update(Booking booking);

    @Delete
    void delete(Booking booking);

    @Query("SELECT * FROM Booking")
    LiveData<List<Booking>> getAllBookings();

    @Query("SELECT * FROM Booking WHERE customerEmail = :customerEmail")
    LiveData<List<Booking>> getBookingsForCustomer(String customerEmail);

    @Query("SELECT * FROM Booking WHERE classId = :classId")
    LiveData<List<Booking>> getBookingsForClass(int classId);

    //get booking for customer and a particular booked class
    @Query("SELECT * FROM Booking WHERE classId = :classId AND customerEmail = :customerEmail LIMIT 1")
    LiveData<Booking> checkCustomerBooking(String customerEmail, int classId);

    @Transaction
    @Query("SELECT * FROM Booking WHERE customerEmail = :customerEmail")
    LiveData<List<BookingWithClass>> getBookingsWithClassesForCustomer(String customerEmail);
    @Query("DELETE FROM booking")
    void deleteAll();
}
