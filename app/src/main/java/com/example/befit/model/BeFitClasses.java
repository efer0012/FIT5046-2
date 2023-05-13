package com.example.befit.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//[Edward] this is created for test purposes only
//public class BeFitClasses {
//    private String className;
//    private String classTime;
//
//    public BeFitClasses(String name, String time) {
//        className = name;
//        classTime = time;
//    }
//    public String getClassName() {
//        return className;
//    }
//    public String getClassTime() {
//        return classTime;
//    }
//    //this is used to populate the list with a few items at the start of the application
//    //it is static so it can be called without instantiating the class
//    public static List<BeFitClasses> createContactsList() {
//        List<BeFitClasses> classes = new ArrayList<BeFitClasses>();
//        classes.add(new BeFitClasses( "Boxing","11:00 AM"));
//        classes.add(new BeFitClasses( "Yoga","01:00 PM"));
//        classes.add(new BeFitClasses( "Body Pump","03:00 PM"));
//        return classes;
//    }
//}

//testing pre-populate database for beFitClasses
public class BeFitClasses extends RoomDatabase.Callback {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);

        /*Scheduled classes
        MONDAY
        08:00 AM - Yoga
        10:00 AM - Pilates
        01:00 PM - Boxing
        03:00 PM - Body Pump
        05:00 PM - Cycling
        07:00 PM - Hot Yoga

        TUESDAY
        08:00 AM - Pilates
        10:00 AM - Cycling
        02:00 PM - Body Balance
        04:00 PM - Body Step
        06:00 PM - Yin Yoga
        07:00 PM - Boxing
        08:00 PM - Body Pump

        WEDNESDAY
        08:00 AM - Yoga
        10:00 AM - Boxing
        02:00 PM - Cycling
        04:00 PM - Zumba
        06:00 PM - Body Attack
        07:00 PM - Sprint
        09:00 PM - Hot Yoga

        THURSDAY
        08:00 AM - Body Attack
        10:00 AM - Cycling
        02:00 PM - Zumba
        05:00 PM - Pilates
        06:00 PM - Yoga
        08:00 PM - Cycling

        FRIDAY
        08:00 AM - Yoga
        10:00 AM - Pilates
        02:00 PM - Boxing
        05:00 PM - Zumba
        06:00 PM - Sprint
        08:00 PM - Hot Yoga

        SATURDAY
        08:00 AM - Pilates
        09:00 AM - Yoga
        10:00 AM - Cycling
        02:00 PM - Body Pump
        05:00 PM - Zumba
        06:00 PM - Martial Arts
        07:00 PM - Cycling
        09:00 PM - Hot Yoga

        SUNDAY
        10:00 AM - Yoga
        02:00 PM - Cycling
        04:00 PM - Hot Yoga
        05:00 PM - Meditation
        07:00 PM - Yin Yoga

         */

//        // Insert data into database
//        //MONDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Monday', '08:00 AM', '08:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Monday', '10:00 AM', '11:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Monday', '01:00 PM', '02:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Monday', '03:00 PM', '03:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Monday', '05:00 PM', '05:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Monday', '07:00 PM', '08:00 PM');");
//
//        //TUESDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Tuesday', '08:00 AM', '09:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Tuesday', '10:00 AM', '10:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Balance', 'Tuesday', '02:00 PM', '02:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Step', 'Tuesday', '04:00 PM', '05:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yin Yoga', 'Tuesday', '06:00 PM', '07:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Tuesday', '07:00 PM', '08:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Tuesday', '08:00 PM', '08:45 PM');");
//
//        //WEDNESDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Wednesday', '08:00 AM', '08:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Wednesday', '10:00 AM', '11:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Wednesday', '02:00 PM', '02:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Wednesday', '04:00 PM', '04:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Attack', 'Wednesday', '06:00 PM', '07:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Sprint', 'Wednesday', '07:00 PM', '07:30 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Wednesday', '09:00 PM', '10:00 PM');");
//
//        //THURSDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Attack', 'Thursday', '08:00 AM', '09:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Thursday', '10:00 AM', '10:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Thursday', '02:00 PM', '02:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Thursday', '04:00 PM', '05:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Thursday', '06:00 PM', '06:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Thursday', '08:00 PM', '08:45 PM');");
//
//        //FRIDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Friday', '08:00 AM', '08:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Friday', '10:00 AM', '11:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Friday', '02:00 PM', '03:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Friday', '05:00 PM', '05:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Sprint', 'Friday', '06:00 PM', '06:30 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Friday', '08:00 PM', '09:00 PM');");
//
//        //SATURDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Saturday', '08:00 AM', '09:00 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Saturday', '09:00 AM', '09:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Saturday', '10:00 AM', '10:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Saturday', '02:00 PM', '02:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Saturday', '05:00 PM', '05:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Martial Arts', 'Saturday', '06:00 PM', '06:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Saturday', '07:00 PM', '07:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Saturday', '09:00 PM', '10:00 PM');");
//
//        //SUNDAY
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Sunday', '10:00 AM', '10:45 AM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Sunday', '02:00 PM', '02:45 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Sunday', '04:00 PM', '05:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Meditation', 'Sunday', '05:00 PM', '06:00 PM');");
//        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yin Yoga', 'Sunday', '07:00 PM', '08:00 PM');");

        // Insert data into database
        //MONDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Monday', '08:00', '08:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Monday', '10:00', '11:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Monday', '13:00', '14:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Monday', '15:00', '15:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Monday', '17:00', '17:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Monday', '19:00', '20:00');");

        //TUESDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Tuesday', '08:00', '09:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Tuesday', '10:00', '10:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Balance', 'Tuesday', '14:00', '14:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Step', 'Tuesday', '16:00', '17:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yin Yoga', 'Tuesday', '18:00', '19:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Tuesday', '19:00', '20:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Tuesday', '20:00', '20:45');");

        //WEDNESDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Wednesday', '08:00', '08:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Wednesday', '10:00', '11:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Wednesday', '14:00', '14:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Wednesday', '16:00', '16:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Attack', 'Wednesday', '18:00', '19:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Sprint', 'Wednesday', '19:00', '19:30');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Wednesday', '21:00', '22:00');");

        //THURSDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Attack', 'Thursday', '08:00', '09:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Thursday', '10:00', '10:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Thursday', '14:00', '14:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Thursday', '16:00', '17:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Thursday', '18:00', '18:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Thursday', '20:00', '20:45');");

        //FRIDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Friday', '08:00', '08:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Friday', '10:00', '11:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Boxing', 'Friday', '14:00', '15:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Friday', '17:00', '17:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Sprint', 'Friday', '18:00', '18:30');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Friday', '20:00', '21:00');");

        //SATURDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Pilates', 'Saturday', '08:00', '09:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Saturday', '09:00', '09:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Saturday', '10:00', '10:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Body Pump', 'Saturday', '14:00', '14:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Zumba', 'Saturday', '17:00', '17:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Martial Arts', 'Saturday', '18:00', '18:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Saturday', '19:00', '19:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Saturday', '21:00', '22:00');");

        //SUNDAY
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yoga', 'Sunday', '10:00', '10:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Cycling', 'Sunday', '14:00', '14:45');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Hot Yoga', 'Sunday', '16:00', '17:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Meditation', 'Sunday', '17:00', '18:00');");
        db.execSQL("INSERT INTO classes (className, day, startTime, endTime) VALUES ('Yin Yoga', 'Sunday', '19:00', '20:00');");


        //This is for testing purposes only
        //inserting dummy customer
        db.execSQL("INSERT INTO customer (email, firstName, lastName, gender, dateOfBirth, address, height) VALUES ('john.doe@example.com', 'John', 'Doe', 'Male', '1990-01-01', '123 Main St', 180.0);");
    }
}
