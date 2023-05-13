package com.example.befit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.befit.dao.ClassesDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Classes;

import java.util.List;

public class ClassesRepository {
    private ClassesDao classesDao;
    private LiveData<List<Classes>> allClasses;
    private LiveData<List<Classes>> classesByDay;

    public ClassesRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        classesDao = database.classesDao();
        allClasses = classesDao.getAllClasses();
    }

    public LiveData<List<Classes>> getAllClasses() {
        return allClasses;
    }
    public LiveData<List<Classes>> getClassesByDay(String day) {
        classesByDay = classesDao.findByDay(day);
        return classesByDay;
    };

    public Classes getClassById(int classId){
        return classesDao.getClassById(classId);
    }

    public int getNumberOfClasses(){
        return classesDao.getTotalClasses();
    }

    public void insertClasses(final Classes beFitClass){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                classesDao.insert(beFitClass);
            }
        });
    }

    public void deleteAllClasses(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                classesDao.deleteAll();
            }
        });
    }

    public void deleteClasses(final Classes beFitClass){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                classesDao.delete(beFitClass);
            }
        });
    }

    public void updateClasses(final Classes beFitClass){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                classesDao.update(beFitClass);
            }
        });
    }
}

