package com.example.befit;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Record;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyWorker extends Worker {

    private static final String TAG = MyWorker.class.getName();

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG + ".doWork", getFormattedTimestamp() + " Start uploading data to Firestore");
        Context context = getApplicationContext();
        // set up Room db
        AppDatabase roomDb = AppDatabase.getInstance(context);
        // set up Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // get all record
        List<Record> allRecords = roomDb.recordDao().getAll();
        // upload record
        try {
            // Create a batched write operation
            WriteBatch batch = db.batch();
            for (Record r : allRecords) {
                DocumentReference recordRef = db.collection("records").document();
                batch.set(recordRef, r);
            }
            // Commit the batched write operation
            batch.commit()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Result.success();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Result.failure();
                        }
                    });
            Log.d(TAG + ".doWork", getFormattedTimestamp() + " Uploading work completed");
            return Result.success();
        } catch (Throwable throwable) {
            Log.d(TAG, "Error Sending Notification" + throwable.getMessage());
            return Result.failure();
        }
    }

    private String getFormattedTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
