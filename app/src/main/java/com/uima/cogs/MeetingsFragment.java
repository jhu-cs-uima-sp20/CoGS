package com.uima.cogs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MeetingsFragment extends Fragment {

    private View root;
    public MeetingsFragment() {
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
        root = inflater.inflate(R.layout.fragment_meetings, container, false);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButtonMeeting);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = getActivity().getIntent();
                String gName = intent1.getStringExtra("Group Name");
                Intent intent = new Intent(getContext(), CreateMeetingActivity.class);
                intent.putExtra("Group Name", gName);
                startActivity(intent);
            }
        });
        return root;
    }
}
