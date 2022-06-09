package com.example.projectcalendarscheduler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static TextView tvStartDate, tvEndDate;
    static EditText etTitle, etLocation, etTasks;
    static Spinner sprDuration;
    static RecyclerView recyclerView;
    Button btnAdd;
    static List<Model> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();
        DurationFunc();
        AddButtonfunc();
        StartDatefunc();
    }

    private void StartDatefunc() {
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(tvStartDate);
            }
        });
    }

    private void AddButtonfunc() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                } else if (etLocation.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Location", Toast.LENGTH_SHORT).show();
                } else if (etTasks.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter task", Toast.LENGTH_SHORT).show();
                } else if (tvStartDate.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please select calendar for start date", Toast.LENGTH_SHORT).show();
                } else if (tvEndDate.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please select time duration", Toast.LENGTH_SHORT).show();
                } else {
                    addFunc();
                }
            }
        });
    }

    private void Initialization() {
        //TextView
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);

        //EditText
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        etTasks = findViewById(R.id.etTasks);

        //Button
        btnAdd = findViewById(R.id.btnAdd);

        //Spinner
        sprDuration = findViewById(R.id.sprDuration);

        //Array
        modelList = new ArrayList<>();

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void DurationFunc() {
        sprDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String duration = sprDuration.getSelectedItem().toString();

                if (duration.equals("5 mins")) {
                    getDurationTime("MINUTE", 5);
                } else if (duration.equals("10 mins")) {
                    getDurationTime("MINUTE", 10);
                } else if (duration.equals("15 mins")) {
                    getDurationTime("MINUTE", 15);
                } else if (duration.equals("30 mins")) {
                    getDurationTime("MINUTE", 30);
                } else if (duration.equals("1 hr")) {
                    getDurationTime("HOUR", 1);
                } else if (duration.equals("2 hrs")) {
                    getDurationTime("HOUR", 2);
                } else if (duration.equals("3 hrs")) {
                    getDurationTime("HOUR", 3);
                } else if (duration.equals("5 hrs")) {
                    getDurationTime("HOUR", 5);
                } else if (duration.equals("1 day")) {
                    getDurationTime("DAY", 1);
                } else if (duration.equals("Select Duration")) {
                    tvEndDate.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getDurationTime(String per, int i) {

        String getStartDate = tvStartDate.getText().toString();

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dateStart = simpleDateFormat.parse(getStartDate);
            Calendar cal = Calendar.getInstance();

            cal.setTime(dateStart);

            if (per.equals("MINUTE")) {
                cal.add(Calendar.MINUTE, i);
            } else if (per.equals("HOUR")) {
                cal.add(Calendar.HOUR, i);
            } else if (per.equals("DAY")) {
                cal.add(Calendar.DATE, i);
            }

            tvEndDate.setText(simpleDateFormat.format(cal.getTime()));
            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            Toast.makeText(this, "Please select calender for start date", Toast.LENGTH_SHORT).show();
            sprDuration.setSelection(0);
        }
    }

    private String removeLastChar(String s) {
        return s.substring(0, s.length() - 6);
    }


    Boolean Process;

    private void addFunc() {

        try {
            Process = false;
            for (Model model : modelList) {
                if (model.getTask().equals(etTasks.getText().toString())) {

                    String getEndDate = model.getEndDate();
                    String duration = sprDuration.getSelectedItem().toString();
                    String getStartTime = tvStartDate.getText().toString();

                    String getStartModel = model.getStartDate();
                    String getEndModel = model.getEndDate();

                    String getStartDate = removeLastChar(tvStartDate.getText().toString());
                    String getEndTime = tvEndDate.getText().toString();

                    String StartDateModelrem = removeLastChar(getStartModel);
                    try {

                        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

                        String startRem = getStartTime.substring(11);
                        String endRem = getEndTime.substring(11);
                        String startRemModel = getStartModel.substring(11);
                        String endtRemModel = getEndModel.substring(11);

                        Date dateStart = formatTime.parse(startRem);
                        Date dateEnd = formatTime.parse(endRem);

                        Date dateStartModel = formatTime.parse(startRemModel);
                        Date dateEndModel = formatTime.parse(endtRemModel);

                        long longStart = dateStart.getTime();
                        long longEnd = dateEnd.getTime();

                        long longStartModel = dateStartModel.getTime();
                        long longEndModel = dateEndModel.getTime();
                        if (StartDateModelrem.equals(getStartDate)) {
                            if ((longStartModel <= longStart && longStartModel<=longEnd) ||(longStartModel >= longStart && longStartModel<=longEnd) ) {
                                if ((longEndModel >= longStart &&longEndModel >= longEnd)||(longEndModel >= longStart &&longEndModel <= longEnd)) {
                                    tvStartDate.setText(getEndDate);
                                    if (duration.equals("5 mins")) {
                                        getDurationTime("MINUTE", 5);
                                    } else if (duration.equals("10 mins")) {
                                        getDurationTime("MINUTE", 10);
                                    } else if (duration.equals("15 mins")) {
                                        getDurationTime("MINUTE", 15);
                                    } else if (duration.equals("30 mins")) {
                                        getDurationTime("MINUTE", 30);
                                    } else if (duration.equals("1 hr")) {
                                        getDurationTime("HOUR", 1);
                                    } else if (duration.equals("2 hrs")) {
                                        getDurationTime("HOUR", 2);
                                    } else if (duration.equals("3 hrs")) {
                                        getDurationTime("HOUR", 3);
                                    } else if (duration.equals("5 hrs")) {
                                        getDurationTime("HOUR", 5);
                                    } else if (duration.equals("1 day")) {
                                        getDurationTime("DAY", 1);
                                    }

                                    Process = true;
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (Process == true) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                alert.setMessage("There is already a scheduled time similar to your Task.").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog1 = alert.create();
                alertDialog1.setTitle("Changed Schedule Time");
                alertDialog1.show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
        }

        Model model = new Model();
        model.setTitle(etTitle.getText().toString());
        model.setLocation(etLocation.getText().toString());
        model.setTask(etTasks.getText().toString());
        model.setDuration(sprDuration.getSelectedItem().toString());
        model.setStartDate(tvStartDate.getText().toString());
        model.setEndDate(tvEndDate.getText().toString());

        modelList.add(model);
        PutDataIntoRecycleView(modelList);
        Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show();

        etTitle.setText("");
        etLocation.setText("");
        etTasks.setText("");
        tvStartDate.setText("");
        sprDuration.setSelection(0);
        tvEndDate.setText("");
    }

    private void showDateDialog(TextView date_in) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        date_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(MainActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void PutDataIntoRecycleView(List<Model> modelList) {
        try {
            Adapter adapter = new Adapter(this, modelList);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } catch (Exception ex) {
        }
    }
}