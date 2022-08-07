package com.example.glm;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.glm.models.ReminderData;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.UUID;

public class AddReminderActivity extends AppCompatActivity {

    public static String[] types = {"Appointment", "Event", "Meeting", "Task"};
    public static String[] days = {"None", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private CheckBox checkoff;
    private EditText nameField;
    private Spinner typeSpinner;
    private Spinner daysSpinner;
    private Button setReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        nameField = findViewById(R.id.reminder_name);

        typeSpinner = findViewById(R.id.spinner_reminder_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        String reminderID = UUID.randomUUID().toString();
        String belongList = getIntent().getStringExtra("BelongList");

        checkoff = findViewById(R.id.reminder_checkoff);
        checkoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        daysSpinner = findViewById(R.id.spinner_reminder_days);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(daysAdapter);

        setReminder = findViewById(R.id.btn_set);
        setReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newName = nameField.getText().toString();
                Boolean newCheck = checkoff.isChecked();
                String newType = typeSpinner.getSelectedItem().toString();
                String newDay = daysSpinner.getSelectedItem().toString();

                if(newName.isEmpty() || newType.isEmpty()){
                    Toast.makeText(AddReminderActivity.this, "Please fill out fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    saveReminder(belongList, ParseUser.getCurrentUser(), reminderID, newName, newType, newCheck, newDay);
                    Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(AddReminderActivity.this, "New reminder added to " + getIntent().getStringExtra("BelongListName"), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

    }

    public void saveReminder(String belongsToList, ParseUser user, String ID, String reminderName, String reminderType, Boolean isChecked, String newDay){

        ReminderData reminder = new ReminderData();

        reminder.setBelongList(belongsToList);

        reminder.setOwner(user);

        reminder.setReminderID(ID);

        reminder.setReminderName(reminderName);

        reminder.setReminderType(reminderType);

        reminder.setCheckOff(isChecked);

        reminder.setReminderDay(newDay);

        reminder.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("AddReminderActivity", "Error while saving", e);
                    Toast.makeText(AddReminderActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i("AddReminderActivity", "Reminder saved successfully");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_reminder:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Are you sure you want to delete this reminder?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ReminderListManager.getList(mReminder.getList_id()).deleteReminder(mReminder.getId());
//                        deleteReminder();
//                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteReminder(){

    }
}