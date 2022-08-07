package com.example.glm.adapters;

import com.example.glm.models.ListData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.glm.ReminderActivity;
import com.example.glm.R;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {

    private Context context;
    private List<ListData> mReminderlists;

    public ReminderListAdapter(Context context, List<ListData> mReminderlists){
        this.context = context;
        this.mReminderlists = mReminderlists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvListName;
        private RelativeLayout container;

        public ViewHolder(View view) {
            super(view);
//            view.setOnClickListener(this);
            container = view.findViewById(R.id.linearLayout2);
            tvListName = view.findViewById(R.id.reminder_list_name_title);
        }

        public void bind(ListData list) {
            tvListName.setText(list.getListName());
            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ReminderActivity.class);
                    i.putExtra("BelongList", list.getObjectId());
                    i.putExtra("BelongListName", list.getListName());
                    v.getContext().startActivity(i);
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListData list = mReminderlists.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount() {
        return mReminderlists.size();
    }

}