package com.example.befit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.befit.database.Firestore;
import com.example.befit.entity.Customer;
import com.example.befit.viewmodel.CustomerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private CustomerViewModel customerViewModel;
    private String email;
    private String first_name;
    private String last_name;
    private String gender;
    private String dob;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        customerViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(CustomerViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        EditText emailEditText= findViewById(R.id.emailEditText);
        EditText passwordEditText= findViewById(R.id.passwordEditText);
        EditText firstNameEditText= findViewById(R.id.firstNameEditText);
        //first_name = firstNameEditText.getText().toString();
        EditText lastNameEditText= findViewById(R.id.lastNameEditText);
        //last_name = lastNameEditText.getText().toString();

        String[] genderOption = {"Male", "Female", "Other"};
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, genderOption);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        genderSpinner.setAdapter(spinnerAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderOption[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button dobPicker = findViewById(R.id.dobPicker);
        TextView dateOfBirth = findViewById(R.id.dateOfBirth);
        dobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the instance of our calendar.
                final Calendar c = Calendar.getInstance();
                int dob_year = c.get(Calendar.YEAR);
                int dob_month = c.get(Calendar.MONTH);
                int dob_day = c.get(Calendar.DAY_OF_MONTH);
                dob = dob_day + "-" +(dob_month + 1) + "-" + dob_year;
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateOfBirth.setText("Date of Birth: " + dayOfMonth + "-" +(monthOfYear + 1) + "-" + year);
                    }}, dob_year, dob_month, dob_day); // passing year, month and day of selected date.
                datePickerDialog.show();
            }
        });

        EditText addressEditText= findViewById(R.id.addressEditText);
        //address = addressEditText.getText().toString();

        Button registerButton=findViewById(R.id.submitButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                first_name = firstNameEditText.getText().toString();
                last_name = lastNameEditText.getText().toString();
                address = addressEditText.getText().toString();

                // validate email and password
                if (TextUtils.isEmpty(email)) {
                    toastMsg("Empty Email");
                } else if( TextUtils.isEmpty(password)){
                    toastMsg("Empty Password");
                } else if (password.length() < 8) {
                    toastMsg("Password must be at least 8 chars");
                }

                // validate first name and last name
                else if (first_name.isEmpty()){
                    toastMsg("Empty First Name");}
                else if (TextUtils.isEmpty(last_name)) {
                    toastMsg("Empty Last name");}
                else if (!first_name.matches("^[a-zA-Z\\s]*$") ||
                        !last_name.matches("^[a-zA-Z\\s]*$")){
                    toastMsg("No numbers or special characters allowed in name");}

                // validate Date of birth
                else if (dob == null){
                    toastMsg("Empty Date of Birth");}
                //TODO: more validation?

                // validate address
                else if (address.isEmpty()){
                    toastMsg("Empty Address");}

                // registration
                else
                    registerUser(email, password);
            }
        });

    }
    private void registerUser(String email_txt, String password_txt) {
        auth.createUserWithEmailAndPassword(email_txt,password_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    toastMsg("Registration Successful");
                    Customer customer = new Customer(email, first_name, last_name, gender, dob, address, 0);
                    // add into Room database
                    customerViewModel.insertCustomer(customer);
                    // add into Firestore
                    Firestore firestore = new Firestore();
                    firestore.addCustomer(customer);
                    // return back to launch screen
                    startActivity(new Intent(SignupActivity.this, LaunchActivity.class));
                }else {
                    toastMsg("Registration Unsuccessful");
                }
            }
        });
    }
    public void toastMsg(String message){Toast.makeText(this,message,Toast.LENGTH_SHORT).show();}
}
