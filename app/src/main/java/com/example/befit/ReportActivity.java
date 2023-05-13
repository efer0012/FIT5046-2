package com.example.befit;

import android.app.Notification;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.befit.dao.RecordDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Record;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Button generateReportButton = findViewById(R.id.generate_report_button);
        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker startDatePicker = findViewById(R.id.start_date_picker);
                DatePicker endDatePicker = findViewById(R.id.end_date_picker);

                // Start Date
                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDay = startDatePicker.getDayOfMonth();
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(startYear, startMonth, startDay);
                String startDate = startYear + "-" + (startMonth + 1) + "-" + startDay;

                // End Date
                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDay = endDatePicker.getDayOfMonth();
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(endYear, endMonth, endDay);
                String endDate = endYear + "-" + (endMonth + 1) + "-" + endDay;

                // Execute the database query in the background
                new GetHeightDataAsyncTask().execute(startDate, endDate);
            }
        });

        //Line chart
        Button generateLineChartButton = findViewById(R.id.generate_line_chart_button);
        generateLineChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker startDatePicker = findViewById(R.id.start_date_picker);
                DatePicker endDatePicker = findViewById(R.id.end_date_picker);

                // Get the start date
                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDay = startDatePicker.getDayOfMonth();
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(startYear, startMonth, startDay);
                String startDate = startYear + "-" + (startMonth + 1) + "-" + startDay;

                // Get the end date
                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDay = endDatePicker.getDayOfMonth();
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(endYear, endMonth, endDay);
                String endDate = endYear + "-" + (endMonth + 1) + "-" + endDay;

                // Execute the database query in the background
                new GetWeightDataAsyncTask().execute(startDate, endDate);
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetWeightDataAsyncTask extends AsyncTask<String, Void, List<Record>> {

        // Set customer name from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custEmail = user.getEmail();

        @Override
        protected List<Record> doInBackground(String... params) {
            String startDate = params[0];
            String endDate = params[1];

            RecordDao recordDao = AppDatabase.getInstance(ReportActivity.this).recordDao();
            return recordDao.getWeightsBetweenDates(custEmail, startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<Record> weightList) {
            // Create the bar chart using the weightList data
            createBarChart(weightList);
        }
    }

    private class GetHeightDataAsyncTask extends AsyncTask<String, Void, List<Record>> {

        // Set customer name from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custEmail = user.getEmail();

        @Override
        protected List<Record> doInBackground(String... params) {
            String startDate = params[0];
            String endDate = params[1];

            RecordDao recordDao = AppDatabase.getInstance(ReportActivity.this).recordDao();
            return recordDao.getWeightsBetweenDates(custEmail, startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<Record> weightList) {
            // Create the bar chart using the weightList data
            createLineChart(weightList);
        }
    }

    private void createBarChart(List<Record> weightList) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < weightList.size(); i++) {
            Record record = weightList.get(i);
            float weight = record.getWeight();
            float xValue = i;

            entries.add(new BarEntry(xValue, weight));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Weight");
        BarData barData = new BarData(dataSet);
        BarChart barChart = findViewById(R.id.bar_chart);
        barChart.setData(barData);

        // Set the customised xLables, e.g. Date 1, Date 2;
        ArrayList<String> xLabels = new ArrayList<>();
        for (int i = 0; i < weightList.size(); i++) {
            xLabels.add("Date " + (i + 1)); // 自定义标签，可以根据需求进行修改
        }
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        barChart.invalidate();
    }

    private void createLineChart(List<Record> weightList) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < weightList.size(); i++) {
            Record record = weightList.get(i);
            float weight = record.getWeight();
            float xValue = i;

            entries.add(new Entry(xValue, weight));

        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        dataSet.setColor(Color.RED);
        LineData lineData = new LineData(dataSet);
        LineChart lineChart = findViewById(R.id.line_chart);
        lineChart.setData(lineData);

        // Set custom x-axis labels
        ArrayList<String> xLabels = new ArrayList<>();
        for (int i = 0; i < weightList.size(); i++) {
            xLabels.add("Date " + (i + 1));
        }
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        lineChart.invalidate();
    }




}