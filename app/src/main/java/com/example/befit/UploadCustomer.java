package com.example.befit;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadCustomer extends Worker {

    private static final String TAG = UploadCustomer.class.getName();

    public UploadCustomer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        /*
         * Override this method to do your actual background processing.
         * This method is called on a background thread - you are required to synchronously do your work and return the Result from this method.
         * Once you return from this method, the Worker is considered to have finished what its doing and will be destroyed.
         * <p>
         * A Worker is given a maximum of ten minutes to finish its execution and return a {@link Result}.
         * After this time has expired, the Worker will be signalled to stop.
         *
         * @return The {@link Result} of the computation;
         * note that dependent work will not execute if you use {@link Result#failure()} or
         */
        Context context = getApplicationContext();
        try {
            Log.d(TAG, "doWork Called");
            return Result.success();
        } catch (Throwable throwable) {
            Log.d(TAG, "Error Sending Notification" + throwable.getMessage());
            return Result.failure();
        }

    }
}
