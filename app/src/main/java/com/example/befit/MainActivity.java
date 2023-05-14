package com.example.befit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.befit.adapter.RecyclerViewAdapter;
import com.example.befit.database.Firestore;
import com.example.befit.databinding.ActivityMainBinding;
import com.example.befit.entity.Customer;
import com.example.befit.model.BeFitClasses;
import com.example.befit.viewmodel.CustomerViewModel;
import com.example.befit.viewmodel.RecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private List<BeFitClasses> classes;
    private RecyclerViewAdapter adapter;
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private CustomerViewModel customerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // create Firestore
        Firestore firestore = new Firestore();

        // create View model
        customerViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(CustomerViewModel.class);

        setSupportActionBar(binding.appBar.toolbar); //app_bar_main

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_club_fragment,
                R.id.nav_book_classes_fragment,
                R.id.nav_my_classes_fragment,
                R.id.nav_goal_fragment,
                R.id.nav_profile_fragment)
                //to display the Navigation button as a drawer symbol,not being shown as an Up button
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager= getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar,navController, mAppBarConfiguration);


        // "start work" button
        View headerview = binding.navView.getHeaderView(0);
        Button workButton = headerview.findViewById(R.id.workmanagerButton);
        workButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeWorkRequest oneTimeUploadRequest = new OneTimeWorkRequest
                        .Builder(MyWorker.class)
                        .build();
                WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeUploadRequest);
                Log.d(MainActivity.class.getName(), "start working ...");
            }
        }));

        // set up continuous work
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Ensure network connectivity
                .build();
        PeriodicWorkRequest contUploadRequest = new PeriodicWorkRequest
                .Builder(MyWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(contUploadRequest);


        // get customer's email from LoginActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // get customer from Firestore
        firestore.retrieveCustomer(email, new Firestore.FirestoreCallback() {
            @Override
            public void onCallback(Customer customer) {
                // add into Room database
                customerViewModel.insertCustomer(customer);
                // set name and email in Navi header
                TextView nameTextView = headerview.findViewById(R.id.nav_menu_custname);
                nameTextView.setText(customer.firstName + " " + customer.lastName);
                TextView emailTextView = headerview.findViewById(R.id.nav_menu_custemail);
                emailTextView.setText(email);
            }
        });
        // Set customer name from database
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custEmail = user.getEmail();
        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Customer currentCustomer = customerViewModel.getCustomerByEmail(customerEmail);
                if (currentCustomer != null) {
                    String firstName = currentCustomer.firstName;
                    String lastName = currentCustomer.lastName;

                    View headerview = binding.navView.getHeaderView(0);
                    TextView custNameTextView = headerview.findViewById(R.id.nav_menu_custname);
                    TextView custEmailTextView = headerview.findViewById(R.id.nav_menu_custemail);

                    custNameTextView.setText(firstName + " " + lastName);
                    custEmailTextView.setText(customerEmail);
                }
            }
        }).start();*/
    }
}