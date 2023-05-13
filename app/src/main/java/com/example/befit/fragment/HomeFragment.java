package com.example.befit.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.befit.RetrofitInterface;
import com.example.befit.adapter.RecyclerViewAdapter;
import com.example.befit.databinding.HomeFragmentBinding;
import com.example.befit.entity.Booking;
import com.example.befit.entity.Classes;
import com.example.befit.entity.Customer;
import com.example.befit.viewmodel.BookingViewModel;
import com.example.befit.viewmodel.ClassesViewModel;
import com.example.befit.viewmodel.CustomerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    //Weather retrofit
    private static final String API_KEY = "42a696cee4d1c09f5058be40642f2f7d";
    private String location = "Melbourne";
    private RetrofitInterface retrofitInterface;

    //Recycler Views
    private RecyclerViewAdapter MyClassesAdapter;
    private RecyclerViewAdapter AvailableClassesAdapter;
    private RecyclerView myClassesTodayRecycleView;
    private RecyclerView availableClassesTodayRecycleView;
    private ClassesViewModel classesViewModel;
    private BookingViewModel bookingViewModel;

    private HomeFragmentBinding addBinding;
    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();


        // Set customer name from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custEmail = user.getEmail();
        Log.d("Customer Email", custEmail);
        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

//        String dummyEmail = "john.doe@example.com";
        String customerEmail = custEmail;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d("Customer DB", String.valueOf(customerViewModel.getTotalCustomer()));
                Customer currentCustomer = customerViewModel.getCustomerByEmail(customerEmail);
                if (currentCustomer != null) {
                    String firstName = currentCustomer.firstName;
                    String lastName = currentCustomer.lastName;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addBinding.homeCustname.setText(firstName + " " + lastName);
                        }
                    });
                } else {
                    Log.e("HomeFragment", "Current customer is null");
                }
            }
        }).start();


        // Get current day and date
        // Get current date
        Date currentDate = new Date();

        // Format date to display as "EEE, dd MMM" (e.g., "Mon, 10 June")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        addBinding.homeDayDate.setText(formattedDate);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String today = dayFormat.format(currentDate);


        // Weather Retrofit
//        retrofitInterface = RetrofitClient.getRetrofitService();
//
//        Call<WeatherResponse> callAsync =
//                retrofitInterface.customSearch(API_KEY, location);
//        Log.d("URL", callAsync.request().url().toString());
//        //makes an async request & invokes callback methods when the response returns
//        callAsync.enqueue(new Callback<WeatherResponse>() {
//            @Override
//            public void onResponse(Call<WeatherResponse> call,
//                                   Response<WeatherResponse> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        //set weather text in home page
//                        double temperature = response.body().getCurrent().getTemperature();
//                        String temperatureFormatted = String.format("%.1f °C", temperature);
//                        addBinding.homeTemperature.setText(temperatureFormatted);
//                        addBinding.homeWeatherDesc.setText(response.body().getCurrent().getWeatherDescriptions().get(0));
//                    } else {
//                        Log.d("Weather", "Response body or current is null");
//                    }
//                }
//                else {
//                    Log.i("Error ","Response failed");
//                }
//            }
//            @Override
//            public void onFailure(Call<WeatherResponse> call, Throwable t){
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("ERROR", t.getMessage());
//            }
//        });

        //Recycle View
        // Get current time
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        Log.d("current_time", String.valueOf(currentHour) + ":" + String.valueOf(currentMinute));

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        classesViewModel = new ViewModelProvider(this).get(ClassesViewModel.class);

        //My Classes Today
        //initialise recycler view
        myClassesTodayRecycleView = addBinding.recyclerViewMyClassesToday;

        //initialise adapter
        MyClassesAdapter = new RecyclerViewAdapter(getContext());
        addBinding.recyclerViewMyClassesToday.setAdapter(MyClassesAdapter);

        addBinding.recyclerViewMyClassesToday.setLayoutManager(new LinearLayoutManager(getActivity()));

        super.onCreate(savedInstanceState);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        bookingViewModel.getBookingsForCustomer(customerEmail).observe(getViewLifecycleOwner(), new Observer<List<Booking>>() {
            @Override
            public void onChanged(List<Booking> bookings) {

                Log.d("customerBookings", String.valueOf(bookings.size()));

                // Filter out the classes that are booked ongoing and after the current time
                List<String> classId = new ArrayList<>();
                List<Classes> bookedClasses = new ArrayList<>();
                for (Booking booking : bookings) {

                    String bookingDayDateTime = booking.bookingDayDateTime;

                    String[] parts = bookingDayDateTime.split(", ");

                    //get the booked class day, date, time
                    String bookedClassDay = parts[0];
                    String bookedClassDate = parts[1];
                    //String bookedClassTime = parts[2];
                    bookedClassDay = bookedClassDay.trim();
                    bookedClassDate = bookedClassDate.trim();
//                    bookedClassTime = bookedClassTime.trim();

                    // Convert the booking date to a Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                    Date classDate;
                    try {
                        classDate = dateFormat.parse(bookedClassDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }

                    // Get the current date
                    Date currentDate = new Date();

                    if (bookedClassDay.equals(today)){ //get today's my class
                        if (dateFormat.format(classDate).equals(dateFormat.format(currentDate))){
                            //retrieve the class object

                            Classes bookedClass;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Classes bookedClass = classesViewModel.getClassById(booking.classId);
                                    if (bookedClass != null) {
                                        String bookedClassTime = parts[2];
                                        bookedClassTime = bookedClassTime.trim();

                                        String classEndTime = bookedClass.endTime;

                                        //check the time
                                        String[] classStartTimeParts = bookedClassTime.split(":");
                                        int bookedClassStartHour = Integer.parseInt(classStartTimeParts[0]);
                                        int bookedClassStartMinute = Integer.parseInt(classStartTimeParts[1]);
                                        int bookedClassStartTimeInMinutes = bookedClassStartHour * 60 + bookedClassStartMinute;
                                        String[] classEndTimeParts = classEndTime.split(":");
                                        int bookedClassEndHour = Integer.parseInt(classEndTimeParts[0]);
                                        int bookedClassEndMinute = Integer.parseInt(classEndTimeParts[1]);
                                        int bookedClassEndTimeInMinutes = bookedClassEndHour * 60 + bookedClassEndMinute;

                                        int currentTimeInMinutes = currentHour * 60 + currentMinute;

                                        // Compare the booked class time with the current time
                                        if (bookedClassStartTimeInMinutes > currentTimeInMinutes) {
                                            Log.d("debug", "Hello i am here");
                                            bookedClasses.add(bookedClass); //The booked class is scheduled to start after the current time
                                        } else if (bookedClassEndTimeInMinutes >= currentTimeInMinutes) {
                                            Log.d("debug", "Hello i am second");
                                            bookedClasses.add(bookedClass); //The booked class is ongoing
                                        } else {
                                            //class has ended
                                        }
                                    } else {
                                        Log.e("HomeFragment", "bookedClass is null");
                                    }
                                }
                            }).start();
                        }
                    }
                }
                MyClassesAdapter.setClasses(bookedClasses);


            }
        });


        //Available Classes Today
        //initialise recycler view
        availableClassesTodayRecycleView = addBinding.recyclerViewAvailableClassesToday;
        //initialise adapter
        AvailableClassesAdapter = new RecyclerViewAdapter(getContext());
        addBinding.recyclerViewAvailableClassesToday.setAdapter(AvailableClassesAdapter);

        addBinding.recyclerViewAvailableClassesToday.setLayoutManager(new LinearLayoutManager(getActivity()));

        super.onCreate(savedInstanceState);
        //classesViewModel = new ViewModelProvider(this).get(ClassesViewModel.class);

        classesViewModel.getClassesByDay(today).observe(getViewLifecycleOwner(), new Observer<List<Classes>>() {
            @Override
            public void onChanged(List<Classes> beFitClasses) {

                // Filter out the classes that are available after the current time
                List<Classes> availableClasses = new ArrayList<>();
                for (Classes classes : beFitClasses) {
                    String[] startTimeParts = classes.startTime.split(":");
                    int startHour = Integer.parseInt(startTimeParts[0]);
                    int startMinute = Integer.parseInt(startTimeParts[1]);
                    if (startHour > currentHour || (startHour == currentHour && startMinute >= currentMinute)) {
                        availableClasses.add(classes);
                    }
                }

                // Update RecyclerView Adapter with new data
                AvailableClassesAdapter.setClasses(availableClasses);
            }
        });



        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
