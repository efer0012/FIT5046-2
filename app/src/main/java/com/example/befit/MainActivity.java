package com.example.befit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.befit.adapter.RecyclerViewAdapter;
import com.example.befit.database.Firestore;
import com.example.befit.databinding.ActivityMainBinding;
import com.example.befit.entity.Customer;
import com.example.befit.model.BeFitClasses;
import com.example.befit.viewmodel.CustomerViewModel;

import java.util.List;

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

        // create view model
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

        // get customer's email from LoginActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // get customer from Room
        /*CompletableFuture<Customer> customerCompletableFuture = customerViewModel.findCustomerFuture(email);
        customerCompletableFuture.thenApply(customer -> {
            if (customer != null){
                System.out.println("!!!!!!!!!!" + customer.toString());
            } else {
                System.out.println("!!!!!!!!!! FAIL !!!!!!!!!!");
            }
            return customer;
        });*/

        // get customer from Firestore
        Firestore firestore = new Firestore();
        firestore.retrieve(email, new Firestore.FirestoreCallback() {
            @Override
            public void onCallback(Customer customer) {
                System.out.println("!!!!!!!!!! " + customer.toString());
            }
        });


        // upload all customers into Firestore
        firestore.upload(customerViewModel);
    }
}