package com.example.glm;

import com.example.glm.models.ReminderData;
import com.example.glm.adapters.ReminderAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.glm.models.ListData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

public class ReminderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ReminderData> allReminders;
    private ReminderAdapter mAdapter;
    public EditText userInput;
    private TextView nameOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        recyclerView = findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allReminders = new ArrayList<>();
        mAdapter = new ReminderAdapter(ReminderActivity.this, allReminders);
        recyclerView.setAdapter(mAdapter);
        nameOfList = findViewById(R.id.tv_list_name);
        nameOfList.setText(getIntent().getStringExtra("BelongListName"));
        recyclerView.setLayoutManager(layoutManager);

        queryReminders();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminder:
                Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                Log.i("ReminderActivity", "String extra: " + getIntent().getStringExtra("BelongList"));
                intent.putExtra("BelongList", getIntent().getStringExtra("BelongList"));
                intent.putExtra("BelongListName", getIntent().getStringExtra("BelongListName"));
                startActivity(intent);
                return true;

            case R.id.rename_reminderlist:
//                EditText editText = new EditText(this);
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setTitle("Enter a new name:").setView(editText);
//                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        getSupportActionBar().setTitle(editText.getText().toString());
//                        mReminderlist.setName(editText.getText().toString());
//                        saveList();
//                        Toast.makeText(getApplicationContext(), "succeed!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                dialog.show();

                LayoutInflater li = LayoutInflater.from(ReminderActivity.this);
                View promptsView = li.inflate(R.layout.list_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReminderActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                userInput = promptsView.findViewById(R.id.etReminderListName);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String listName = userInput.getText().toString();
                                        if(listName.isEmpty()){
                                            return;
                                        }

                                        ParseQuery<ListData> query = ParseQuery.getQuery(ListData.class);
                                        query.whereEqualTo(ListData.KEY_OBJECT_ID, getIntent().getStringExtra("BelongList"));
                                        query.findInBackground(new FindCallback<ListData>() {
                                            @Override
                                            public void done(List<ListData> results, ParseException e) {
                                                if (e != null) {

                                                    Log.e("ReminderActivity", "Error renaming reminder");

                                                } else {

                                                    for (ListData list : results) {
                                                        list.put(ListData.KEY_LISTNAME, listName);
                                                        list.saveInBackground();
                                                    }

                                                    Intent intent = new Intent(ReminderActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                    Toast.makeText(getApplicationContext(), "List renamed", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
//
            case R.id.delete_reminderlist:
                AlertDialog.Builder dialog_2 = new AlertDialog.Builder(this);
                dialog_2.setTitle("Are you sure you want to delete this list?");
                dialog_2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseQuery<ListData> query = ParseQuery.getQuery(ListData.class);
                        query.whereEqualTo(ListData.KEY_OBJECT_ID, getIntent().getStringExtra("BelongList"));
                        query.findInBackground(new FindCallback<ListData>() {
                            @Override
                            public void done(List<ListData> results, ParseException e) {
                                if (e != null) {

                                    Log.e("ReminderActivity", "Error deleting reminder");

                                } else {

                                    for (ListData list : results) {
                                        list.deleteInBackground();
                                    }

                                    Intent intent = new Intent(ReminderActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(getApplicationContext(), "List deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                });
                dialog_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog_2.show();
                return true;

            case R.id.clear_checkoff:
                ParseQuery<ReminderData> query = ParseQuery.getQuery(ReminderData.class);
                query.whereEqualTo(ReminderData.KEY_BELONGLIST, getIntent().getStringExtra("BelongList"));
                query.findInBackground(new FindCallback<ReminderData>() {
                    @Override
                    public void done(List<ReminderData> results, ParseException e) {
                        if (e != null) {

                            Log.e("ReminderActivity", "Error clearing checks");

                        } else {

                            for (ReminderData reminder : results) {
                                reminder.setCheckOff(false);
                                reminder.saveInBackground();
                            }

                            startActivity(getIntent());
                            finish();

                            Toast.makeText(getApplicationContext(), "Checks cleared", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void queryReminders(){

        ParseQuery<ReminderData> query = ParseQuery.getQuery(ReminderData.class);
        query.whereEqualTo(ReminderData.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(ReminderData.KEY_BELONGLIST, getIntent().getStringExtra("BelongList"));
        Log.i("ReminderActivity", "User: " + ParseUser.getCurrentUser().getUsername());
        Log.i("ReminderActivity", "List: " + getIntent().getStringExtra("BelongList"));

        query.findInBackground(new FindCallback<ReminderData>() {
            @Override
            public void done(List<ReminderData> reminders, ParseException e) {
                if(e != null) {
                    Log.e("ReminderActivity", "Issue with getting reminders");
                    return;
                }

                for(ReminderData reminder : reminders){

                    Log.i("ReminderActivity", "Reminder: " +  reminder.getReminderName());

                }
                mAdapter.addAll(reminders);
                mAdapter.notifyDataSetChanged();

            }
        });

    }

}
