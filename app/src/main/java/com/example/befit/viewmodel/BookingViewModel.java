package com.example.befit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.befit.entity.Booking;
import com.example.befit.repository.BookingRepository;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {
    private BookingRepository repository;
    private LiveData<List<Booking>> allBookings;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingRepository(application);
        allBookings = repository.getAllBookings();
    }

    public void insertBooking(Booking booking) {
        repository.insertBooking(booking);
    }

    public void deleteBooking(Booking booking) {
        repository.deleteBooking(booking);
    }

    public void deleteAllBookings() {
        repository.deleteAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings() {
        return allBookings;
    }
    public LiveData<List<Booking>> getBookingsForCustomer(String customerEmail) {
        return repository.getBookingsForCustomer(customerEmail);
    }

    public LiveData<Booking> checkCustomerBooking(String customerEmail, int classId){
        return repository.checkCustomerBooking(customerEmail,classId);
    }
}
