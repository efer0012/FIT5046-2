package com.example.befit.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Customer.class,
                parentColumns = "email",
                childColumns = "customerEmail",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Classes.class,
                parentColumns = "classId",
                childColumns = "classId",
                onDelete = ForeignKey.CASCADE)
})
public class Booking {
    @PrimaryKey(autoGenerate = true)
    public int bookingId;
    public String customerEmail;
    public int classId;
    public String bookingMadeDateTime; //the date time when the user book the class
    public String bookingDayDateTime; //the day date time for the booked class

    public Booking(@NonNull String customerEmail, @NonNull int classId, @NonNull String bookingMadeDateTime, @NonNull String bookingDayDateTime) {
        this.customerEmail = customerEmail;
        this.classId = classId;
        this.bookingMadeDateTime = bookingMadeDateTime;
        this.bookingDayDateTime = bookingDayDateTime;
    }

}
