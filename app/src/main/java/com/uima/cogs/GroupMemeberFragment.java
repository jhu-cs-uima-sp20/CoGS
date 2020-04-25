package com.uima.cogs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupMemeberFragment extends Fragment {
    private CustomAdapter customAdapter;
    private GridView gridView;
    private View root;
    private ArrayList<User> groupMembers;
    private ArrayList<String> members;


    public GroupMemeberFragment() {
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
        root = inflater.inflate(R.layout.fragment_group_member_activity, container, false);
        gridView = root.findViewById(R.id.memberGridView);

        groupMembers = new ArrayList<>();
        customAdapter = new CustomAdapter(groupMembers, getContext());
        gridView.setAdapter(customAdapter);

        Intent intent = getActivity().getIntent();
        String groupName = intent.getStringExtra("Group Name");
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Description").child("members");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                members = dataSnapshot.getValue(t);
                getUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    public void getUsers(){

        for(int i = 0; i< members.size(); i++){

            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(members.get(i));
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    groupMembers.add(user);
                    customAdapter.notifyDataSetChanged();
                    gridView.setAdapter(customAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
