package com.example.befit.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.befit.dao.RecordDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Record;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordRepository {
    private RecordDao recordDao;
    private LiveData<List<Record>> allLiveRecords;
    private List<Record> allRecords;
    private ExecutorService executorService;

    public RecordRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        recordDao = database.recordDao();
        allLiveRecords = recordDao.getAllRecords();
        allRecords = recordDao.getAll();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Record>> getAllRecords() {
        return allLiveRecords;
    }

    public List<Record> getAll() {
        return allRecords;
    }

    public LiveData<List<Record>> getCustomerRecords(String customerEmail) {
        return recordDao.getCutsomerRecords(customerEmail);
    }

    public void insert(Record record) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.insert(record);
            }
        });
    }

    public void update(Record record) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.update(record);
            }
        });
    }

    public void delete(Record record) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.delete(record);
            }
        });
    }




}

