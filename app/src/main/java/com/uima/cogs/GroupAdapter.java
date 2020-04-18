package com.uima.cogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private List<GroupDescription> groupList;
    private Context context;
    private Activity activity1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, numMembers, assignee;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.groupNameCard);
            numMembers = view.findViewById(R.id.numMembers);
            view.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(activity1, JoinGroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Group Name", groupList.get(getAdapterPosition()).getGroupName());
            intent.putExtra("Description", groupList.get(getAdapterPosition()).getShortDescription());
            intent.putExtra("Class", groupList.get(getAdapterPosition()).getCourseName());
            intent.putStringArrayListExtra("Members", groupList.get(getAdapterPosition()).getMembers());
            context.startActivity(intent);

        }


    }

    public GroupAdapter(List<GroupDescription> groupList, Context context, Activity activity1 ) {
        this.groupList = groupList;
        this.context = context;
        this.activity1 = activity1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupDescription group = groupList.get(position);
        holder.name.setText(group.getGroupName());
        holder.numMembers.setText(Integer.toString(group.getMembers().size()));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
