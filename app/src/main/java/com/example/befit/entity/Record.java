package com.example.befit.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    public long date;

    public String date_show;
    public float height;
    public float weight;
    public String customerEmail;

    public Record(@NonNull String customerEmail, @NonNull long date, @NonNull String date_show, @NonNull float height, float weight) {
        this.customerEmail = customerEmail;
        this.date=date;
        this.date_show=date_show;
        this.height=height;
        this.weight = weight;
    }
    public long getDate() {
        return date;
    }

    public String getDate_show() {return date_show;}

    public float getWeight() {
        return weight;
    }
}
