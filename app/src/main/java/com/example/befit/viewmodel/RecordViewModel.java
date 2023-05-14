package com.example.befit.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.befit.entity.Record;
import com.example.befit.repository.RecordRepository;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {

    private static RecordRepository mRepository;
    private LiveData<List<Record>> allLiveRecords;
    private List<Record> allRecords;

    public RecordViewModel(Application application) {
        super(application);
        mRepository = new RecordRepository(application);
        allLiveRecords = mRepository.getAllRecords();
        allRecords = mRepository.getAll();
    }

    public LiveData<List<Record>> getAllRecords() {
        return allLiveRecords;
    }

    public List<Record> getAll() {
        return allRecords;
    }

    public LiveData<List<Record>> getCustomerRecords(String customerEmail) {
        return mRepository.getCustomerRecords(customerEmail);
    }

    public void insert(Record record) {
        mRepository.insert(record);
    }

    public void update(Record record) { mRepository.update(record);}





}

