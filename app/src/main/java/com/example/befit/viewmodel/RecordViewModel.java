package com.example.befit.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.befit.dao.RecordDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Record;
import com.example.befit.repository.RecordRepository;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {

    private static RecordRepository mRepository;
    private LiveData<List<Record>> mAllRecords;

    public RecordViewModel(Application application) {
        super(application);
        mRepository = new RecordRepository(application);
        mAllRecords = mRepository.getAllRecords();
    }



    public LiveData<List<Record>> getAllRecords() {
        return mAllRecords;
    }

    public void insert(Record record) {
        mRepository.insert(record);
    }

    public void update(Record record) { mRepository.update(record);}





}

