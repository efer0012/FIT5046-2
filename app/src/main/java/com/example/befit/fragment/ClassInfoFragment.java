package com.example.befit.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.befit.R;
import com.example.befit.databinding.ClassInfoFragmentBinding;
import com.example.befit.entity.Booking;
import com.example.befit.entity.Classes;
import com.example.befit.entity.Customer;
import com.example.befit.viewmodel.BookingViewModel;
import com.example.befit.viewmodel.ClassesViewModel;
import com.example.befit.viewmodel.CustomerViewModel;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClassInfoFragment extends Fragment {
    private ClassInfoFragmentBinding addBinding;
    private ClassesViewModel classesViewModel;
    private BookingViewModel bookingViewModel;
    private Classes selectedClass;
    public ClassInfoFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = ClassInfoFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        //get the classID
        Bundle bundle = getArguments();
        int classId = Integer.parseInt(bundle.getString("classId"));

        ClassesViewModel classesViewModel = new ViewModelProvider(this).get(ClassesViewModel.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                selectedClass = classesViewModel.getClassById(classId);
                if (selectedClass != null) {
                    //get the required data
                    String className = selectedClass.className;
                    String classStartTime = selectedClass.startTime;
                    String classEndTime = selectedClass.endTime;

                    //get the day and date
                    String classDay = selectedClass.day;
                    //get the current date and time
                    Calendar calendar = Calendar.getInstance();
                    // set the calendar to the day of the week for the selected class
                    int selectedDayOfWeek = getDayOfWeek(selectedClass.day);
                    calendar.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    // get the date as a string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                    String classDate = dateFormat.format(calendar.getTime());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addBinding.className.setText(className);
                            addBinding.classStartTime.setText(classStartTime);
                            addBinding.classEndTime.setText(classEndTime);
                            addBinding.classDay.setText(classDay);
                            addBinding.classDate.setText(classDate);
                        }
                    });
                } else {
                    Log.e("ClassInfoFragment", "Selected Class is null");
                }
            }
        }).start();

        //get customer from database
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String custEmail = user.getEmail();
        //Log.d("Customer Email", custEmail);
        String dummyEmail = "john.doe@example.com";
        String customerEmail = dummyEmail;
//        final Customer[] currentCustomer = new Customer[1];
//        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                currentCustomer[0] = customerViewModel.getCustomerByEmail(dummyEmail);
//            }
//        }).start();

        BookingViewModel bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        bookingViewModel.checkCustomerBooking(customerEmail, classId).observe(getViewLifecycleOwner(), booking -> {
            // check if the customer has booked the class or no (for button change)
            if(booking != null){ //customer made the booking
                addBinding.classBookButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                addBinding.classBookButton.setText("Cancel");

                //click button to cancel booking (delete)
                addBinding.classBookButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        bookingViewModel.deleteBooking(booking);
                        Log.d("ClassBookingStatus", "Booking is deleted");
                    }
                });

            }else{ //booking has not been made
                addBinding.classBookButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_700));
                addBinding.classBookButton.setText("Book");
                addBinding.classBookButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //handle the booking event
                        //get the current date time
                        SimpleDateFormat bookingMadeDateTimeFormat = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
                        String bookingMadeDateTime = bookingMadeDateTimeFormat.format(new Date());

                        //get the booked class day date time
                        //Assuming for now the app only handles today's booking only
                        SimpleDateFormat bookedClassDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                        String bookedClassDate = bookedClassDateFormat.format(new Date());
                        String bookedClassDayDateTime = selectedClass.day + ", " + bookedClassDate + ", " + selectedClass.startTime;

                        //Log.d("ClassInfoBookedClassDayDateTime", bookedClassDayDateTime);

                        Booking newBooking = new Booking(customerEmail, classId, bookingMadeDateTime, bookedClassDayDateTime);
                        bookingViewModel.insertBooking(newBooking);
                        Log.d("ClassBookingStatus", "Class is booked");
                    }
                });
            }
        });


        return view;
    }

    private int getDayOfWeek(String dayName) {
        switch (dayName) {
            case "Monday":
                return Calendar.MONDAY;
            case "Tuesday":
                return Calendar.TUESDAY;
            case "Wednesday":
                return Calendar.WEDNESDAY;
            case "Thursday":
                return Calendar.THURSDAY;
            case "Friday":
                return Calendar.FRIDAY;
            case "Saturday":
                return Calendar.SATURDAY;
            case "Sunday":
                return Calendar.SUNDAY;
            default:
                return Calendar.MONDAY;  // default to Monday
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
