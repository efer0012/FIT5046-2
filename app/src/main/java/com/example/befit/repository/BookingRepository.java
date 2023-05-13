package com.example.befit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.befit.dao.BookingDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Booking;
import com.example.befit.entity.Classes;

import java.util.List;

public class BookingRepository {
    private BookingDao bookingDao;
    private LiveData<List<Booking>> allBookings;

    public BookingRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        bookingDao = database.bookingDao();
        allBookings = bookingDao.getAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings() {
        return allBookings;
    }
    public LiveData<Booking> checkCustomerBooking(String customerEmail, int classId) {
        return bookingDao.checkCustomerBooking(customerEmail,classId);
    }
    public LiveData<List<Booking>> getBookingsForCustomer(String customerEmail) {
        return bookingDao.getBookingsForCustomer(customerEmail);
    }

    public void insertBooking(final Booking booking){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookingDao.insert(booking);
            }
        });
    }

    public void deleteAllBookings(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookingDao.deleteAll();
            }
        });
    }

    public void deleteBooking(final Booking booking){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookingDao.delete(booking);
            }
        });
    }
}

