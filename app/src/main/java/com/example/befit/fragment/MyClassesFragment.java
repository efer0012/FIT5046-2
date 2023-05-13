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

import com.example.befit.adapter.RecyclerViewAdapter;
import com.example.befit.databinding.MyClassesFragmentBinding;
import com.example.befit.entity.Booking;
import com.example.befit.entity.Classes;
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

public class MyClassesFragment extends Fragment {

    private RecyclerViewAdapter MyClassesAdapter;
    private RecyclerView myClassesTodayRecycleView;
    private ClassesViewModel classesViewModel;
    private BookingViewModel bookingViewModel;
    private MyClassesFragmentBinding addBinding;
    public MyClassesFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = MyClassesFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        // Set customer name from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custEmail = user.getEmail();
        Log.d("Customer Email", custEmail);
        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        //String dummyEmail = "john.doe@example.com";
        String customerEmail = custEmail;

        // Get current day and date
        // Get current date
        Date currentDate = new Date();

        // Format date to display as "EEE, dd MMM" (e.g., "Mon, 10 June")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        addBinding.myClassesDayDate.setText(formattedDate);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String today = dayFormat.format(currentDate);

        // Get current time
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        //My Classes Today
        //initialise recycler view
        myClassesTodayRecycleView = addBinding.recyclerViewMyClassesToday;

        //initialise adapter
        MyClassesAdapter = new RecyclerViewAdapter(getContext());
        addBinding.recyclerViewMyClassesToday.setAdapter(MyClassesAdapter);

        addBinding.recyclerViewMyClassesToday.setLayoutManager(new LinearLayoutManager(getActivity()));

        super.onCreate(savedInstanceState);
        classesViewModel = new ViewModelProvider(this).get(ClassesViewModel.class);
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
                                            bookedClasses.add(bookedClass); //The booked class is scheduled to start after the current time
                                        } else if (bookedClassEndTimeInMinutes >= currentTimeInMinutes) {
                                            bookedClasses.add(bookedClass); //The booked class is ongoing
                                        } else {
                                            //class has ended
                                        }
                                    } else {
                                        Log.e("HomeFragment", "bookedClass is null");
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyClassesAdapter.setClasses(bookedClasses);

                                            if(bookedClasses.size() != 0){
                                                addBinding.myClassesBookClassesStatus.setText("Your booked classes today");
                                            } else{
                                                addBinding.myClassesBookClassesStatus.setText("No classes booked");
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                }
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
