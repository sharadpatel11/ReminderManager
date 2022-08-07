package com.example.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import java.util.ArrayList;
import android.view.MenuItem;

import com.example.glm.adapters.ReminderListAdapter;
import com.example.glm.models.ListData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ListData> allLists;
    private ReminderListAdapter mAdapter;
    public EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allLists = new ArrayList<>();
        mAdapter = new ReminderListAdapter(MainActivity.this, allLists);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        queryReminderLists();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminderlist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminderlist:
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.list_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
                                            Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        saveList(listName, ParseUser.getCurrentUser());

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

            case R.id.user_logout:
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveList(String name, ParseUser currentUser){

        ListData list = new ListData();

        list.setListName(name);

        list.setOwner(currentUser);

        list.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("MainActivity", "Error while saving", e);
                    Toast.makeText(MainActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i("MainActivity", "List saved successfully");
                allLists.add(list);
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    private void queryReminderLists(){

        ParseQuery<ListData> query = ParseQuery.getQuery(ListData.class);
        query.whereEqualTo(ListData.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ListData>() {
            @Override
            public void done(List<ListData> reminderLists, ParseException e) {
                if(e != null) {
                    Log.e("MainActivity", "Issue with getting lists");
                    return;
                }

                for(ListData list : reminderLists){

                    Log.i("MainActivity", "List: " +  list.getListName());

                }

                allLists.addAll(reminderLists);
                mAdapter.notifyDataSetChanged();

            }
        });

    }

}