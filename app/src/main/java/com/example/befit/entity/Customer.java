package com.example.befit.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Customer")
public class Customer {
    @PrimaryKey
    @NonNull
    public String email;
    @NonNull
    public String firstName;
    @NonNull
    public String lastName;
    @NonNull
    public String gender;
    @NonNull
    public String dateOfBirth;
    @NonNull
    public String address;
    @NonNull
    public double height; //TODO: need entry in sign up, or default 0?

    public Customer(@NonNull String email,
                    @NonNull String firstName,
                    @NonNull String lastName,
                    @NonNull String gender,
                    @NonNull String dateOfBirth,
                    @NonNull String address,
                    @NonNull double height) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.height = height;
    }
    @Override
    public String toString(){
        return "Customer {" + email + "," + firstName + "," + lastName + "," + gender + "," + dateOfBirth + "," + address + "," + height + "}";
    }

    public Customer()
    {}
}
