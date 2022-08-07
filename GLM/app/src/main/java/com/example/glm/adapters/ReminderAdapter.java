package com.example.glm.adapters;

import com.example.glm.MainActivity;
import com.example.glm.ReminderActivity;
import com.example.glm.models.ListData;
import com.example.glm.models.ReminderData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glm.AddReminderActivity;
import com.example.glm.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.List;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.ParseException;

import android.util.Log;
import android.widget.Toast;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private Context context;
    private List<ReminderData> mReminders;

    public ReminderAdapter(Context context, List<ReminderData> mReminders) {
        this.context = context;
        this.mReminders = mReminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int position) {
        ReminderData reminder = mReminders.get(position);
        holder.bind(reminder);

    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    // Add a list of items
    public void addAll(List<ReminderData> reminders) {
        for(ReminderData newReminder : reminders){
            mReminders.add(newReminder);
        }
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameTextView;
        protected TextView typeTextView;
        protected TextView typeSpinnerView;
        protected TextView daySpinnerView;
        protected CheckBox checkoffView;
        protected ImageButton deleteButton;
        protected ConstraintLayout container;

        public ViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.reminder_name_title);
            typeTextView = view.findViewById(R.id.reminder_type_title);
            daySpinnerView = view.findViewById(R.id.reminder_repeated_title);
            checkoffView = view.findViewById(R.id.reminder_checkoff_title);
            typeSpinnerView = view.findViewById(R.id.reminder_type_title);
            deleteButton = view.findViewById(R.id.delete_reminder_inlist);
            container = view.findViewById(R.id.layout_reminderItem);
        }

        public void bind(ReminderData reminder) {

            nameTextView.setText(reminder.getReminderName());
            typeTextView.setText(reminder.getReminderType());
            daySpinnerView.setText("Repeats on " + reminder.getReminderDay());
            checkoffView.setChecked(reminder.getCheckOff());
            checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    reminder.setCheckOff(b);
                    reminder.saveInBackground();

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle("Are you sure you want to delete this reminder?");
                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ParseQuery<ReminderData> query = ParseQuery.getQuery(ReminderData.class);
                            query.whereEqualTo(ReminderData.KEY_UUID, mReminders.remove(getAdapterPosition()).getReminderID());
                            query.findInBackground(new FindCallback<ReminderData>() {
                                @Override
                                public void done(List<ReminderData> results, ParseException e) {
                                    if (e != null) {

                                        Log.e("ReminderAdapter", "Error deleting reminder");

                                    } else {

                                        for (ReminderData reminder : results) {
                                            reminder.deleteInBackground();
                                            notifyDataSetChanged();
                                        }

                                        Toast.makeText(view.getContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }});

            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AddReminderActivity.class);
                    i.putExtra("editReminder", reminder.getObjectId());
                    v.getContext().startActivity(i);
                }
            });

//    public void updateReminderList (ReminderList rs){
//        mReminders = rs.getReminders();
//    }

        }
    }
}



