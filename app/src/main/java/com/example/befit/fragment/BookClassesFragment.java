package com.example.befit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.befit.adapter.RecyclerViewAdapter;
import com.example.befit.databinding.BookClassesFragmentBinding;
import com.example.befit.entity.Classes;
import com.example.befit.viewmodel.ClassesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BookClassesFragment extends Fragment {

    //Recycler View
    private ClassesViewModel classesViewModel;
    private RecyclerViewAdapter AvailableClassesAdapter;
    private RecyclerView availableClassesTodayRecycleView;

    private BookClassesFragmentBinding addBinding;
    public BookClassesFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = BookClassesFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        // Get current day and date
        // Get current date
        Date currentDate = new Date();

        // Format date to display as "EEE, dd MMM" (e.g., "Mon, 10 June")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        addBinding.bookClassesDayDate.setText(formattedDate);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String today = dayFormat.format(currentDate);

        // Get current time
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);


        //Available Classes Today
        classesViewModel = new ViewModelProvider(this).get(ClassesViewModel.class);
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
