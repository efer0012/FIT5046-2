package com.example.befit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.befit.entity.Booking;
import com.example.befit.entity.Classes;

public class BookingWithClass {
    @Embedded
    public Booking booking;
    @Relation(
            parentColumn = "classId",
            entityColumn = "classId"
    )
    public Classes relatedClass;
}
