package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    private RecyclerView rv;
    private EditText search;
    private ImageView searchIcon;
    private ArrayList<GroupDescription> groupDescriptionList;
    DatabaseReference courseRef;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        groupDescriptionList = new ArrayList<>();
        search = findViewById(R.id.searchBar);
        searchIcon = findViewById(R.id.searchIcon);
        rv = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.searchToolbar);

        mAdapter = new GroupAdapter(groupDescriptionList, getApplicationContext(), this);
        rv.setAdapter(mAdapter);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseId = search.getText().toString();
                courseId.replace(".","");
                if(courseId.isEmpty()){
                    Toast.makeText(SearchGroupActivity.this, "Please enter a course name", Toast.LENGTH_SHORT).show();
                }
                else{
                    courseId = courseId.replace(".", "");
                    groupDescriptionList.clear();
                    mAdapter.notifyDataSetChanged();
                    getCourseGroups(courseId);
                }
            }
        });

        toolbar.setTitle("Search");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getCourseGroups(final String courseId){
        //courseId.replace(".", "");
        String str = "";
        for(int i = 0; i<courseId.length(); i++){
            if(courseId.charAt(i)!='.'){

            }
        }
        courseRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(courseId)){
                    getGroupsList(courseId);
                }
                else{
                    Toast.makeText(SearchGroupActivity.this, "There are currently no groups for this course", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getGroupsList(final String courseId){

        DatabaseReference groupListRef = FirebaseDatabase.getInstance().getReference().child("Courses").child(courseId);
        groupListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> groupList = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                groupList =  dataSnapshot.getValue(t);
                System.out.println("Group List: !!!!!"+groupList);
                getGroup(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getGroup(ArrayList<String> groupList){
        int length = groupList.size();
        String groupName = "";

        for(int i = 0; i < length; i++ ){
            groupName = groupList.get(i);
            DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Description");
            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GroupDescription groupDes = dataSnapshot.getValue(GroupDescription.class);
                    System.out.println("GroupName: "+ groupDes.getGroupName());
                    groupDescriptionList.add(0, groupDes);
                    mAdapter.notifyItemInserted(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}

class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private List<GroupDescription> groupList;
    private Context context;
    private Activity activity1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, deadline, assignee;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.groupNameCard);
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
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}

