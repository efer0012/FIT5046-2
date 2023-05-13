package com.example.befit.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Map;

import com.example.befit.entity.Customer;
import com.example.befit.entity.Record;
import com.example.befit.viewmodel.CustomerViewModel;
import com.example.befit.viewmodel.RecordViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Firestore extends AppCompatActivity {

    private static final String TAG = Firestore.class.getName();

    //private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public void uploadCustomer(CustomerViewModel customerViewModel) {
        // get all customers
        List<Customer> allCustomers = customerViewModel.getAll();
        for (Customer c : allCustomers) {
            addCustomer(c);
        }
    }

    public void addCustomer(Customer customer) {
        // set up Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // add Customer
        // If the document does not exist, it will be created.
        // If the document does exist, its contents will be overwritten with the newly provided data
        db.collection("customers")
                .document(customer.email)
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Customer :" + customer.email + " successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing " + customer.email + e);
                    }
                });
    }


    public void retrieveCustomer(String email, FirestoreCallback firestoreCallback){

        // set up
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // get Customer
        DocumentReference customerRef = db.collection("customers").document(email);
        customerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot customerDoc = task.getResult();
                    if (customerDoc.exists()) {
                        Map<String, Object> customerData = customerDoc.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + customerData);
                        Customer result = new Customer(
                                customerDoc.getId(),
                                (String) customerData.get("firstName"),
                                (String)customerData.get("lastName"),
                                (String)customerData.get("address"),
                                (String)customerData.get("gender"),
                                (String)customerData.get("dateOfBirth"),
                                (Double) customerData.get("height"));
                        firestoreCallback.onCallback(result);
                    } else {
                        Log.d(TAG, "No such document: " + email);
                    }
                } else {
                    Log.d(TAG, "Fail to get ", task.getException());
                }
            }
        });
    }

    public void uploadRecord(RecordViewModel recordViewModel) {
        // get all customers
        /*LiveData<List<Record>> allRecords = recordViewModel.getAllRecords();
        for (Record r : allRecords) {
            addRecord(r);
        }*/
        recordViewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> allRecords) {
                for (Record r : allRecords) {
                    addRecord(r);
                }
            };
        });
    }
    public void addRecord(Record record) {
        // set up Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // add Record
        db.collection("records")
                .add(record)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Record: " + record.toString() + " successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing " + record + e);
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(Customer customer);
    }

    public void getUserInfo(String email, final OnGetDataListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        listener.onSuccess(document);
                    } else {
                        listener.onFailure();
                    }
                } else {
                    listener.onFailure();
                }
            }
        });
    }

    public interface OnGetDataListener {
        void onSuccess(DocumentSnapshot document);

        void onFailure();
    }

}
