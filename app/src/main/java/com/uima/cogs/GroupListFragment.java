package com.uima.cogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {
    private FirebaseAuth auth;
    private View root;
    private ArrayList<String> groupList;
    private ArrayList<GroupDescription> groupDescriptions;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private SharedPreferences mPrefs;
    public GroupListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_group_list, container, false);
        auth = FirebaseAuth.getInstance();
        groupDescriptions = new ArrayList<>();
        rv = root.findViewById(R.id.groupRV);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        mAdapter = new GroupAdapterList(groupDescriptions, getContext(), getActivity());
        rv.setAdapter(mAdapter);


        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("groups")){
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    groupList = dataSnapshot.child("groups").getValue(t);
                    getGroups(groupList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab = root.findViewById(R.id.floatingActionButtonGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        return root;
    }

    public void getGroups(ArrayList<String>  groupList){

        for(int i = 0; i< groupList.size(); i++){
            groupDescriptions.clear();
            mAdapter.notifyDataSetChanged();

            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupList.get(i)).child("Group Description");
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GroupDescription group = dataSnapshot.getValue(GroupDescription.class);
                    groupDescriptions.add(0, group);
                    mAdapter.notifyItemInserted(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


}

class GroupAdapterList extends RecyclerView.Adapter<GroupAdapterList.MyViewHolder> {

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

            Intent intent = new Intent(activity1, GroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Group Name", groupList.get(getAdapterPosition()).getGroupName());
            context.startActivity(intent);

        }


    }

    public GroupAdapterList(List<GroupDescription> groupList, Context context, Activity activity1 ) {
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




