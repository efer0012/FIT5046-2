package com.example.befit.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Classes")
public class Classes
{
    @PrimaryKey
    public int classId;
    public String className;
    public String day;
    public String startTime;
    public String endTime;

    public Classes(@NonNull String className,
                   @NonNull String day,
                   @NonNull String startTime,
                   @NonNull String endTime) {
        this.className = className;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
