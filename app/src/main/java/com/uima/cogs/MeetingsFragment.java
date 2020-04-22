package com.uima.cogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MeetingsFragment extends Fragment {

    private View root;
    private RecyclerView rv;
    private String gName;
    private ArrayList<Meetings> meetingsArrayList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        rv = root.findViewById(R.id.meetingRV);

        meetingsArrayList = new ArrayList<>();
        mAdapter = new MeetingAdapterList(meetingsArrayList, getContext(), getActivity());
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mAdapter);


        Intent intent1 = getActivity().getIntent();
        gName = intent1.getStringExtra("Group Name");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateMeetingActivity.class);
                intent.putExtra("Group Name", gName);
                startActivity(intent);
            }
        });

        DatabaseReference meetingRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(gName).child("Group Meetings");
        meetingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Meetings meeting = snapshot.getValue(Meetings.class);
                        System.out.println("Meeing Name!!!!!!!!!!!!!!1: "+ meeting.getName());
                        meetingsArrayList.add(0, meeting);
                        mAdapter.notifyItemInserted(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}


class MeetingAdapterList extends RecyclerView.Adapter<MeetingAdapterList.MyViewHolder> {

    private List<Meetings> meetingList;
    private Context context;
    private Activity activity1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, date, shortDes, rsvp;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.meetingNameCard);
            date = view.findViewById(R.id.cardMeetingDate);
            rsvp = view.findViewById(R.id.cardMeetingRSVP);
            shortDes = view.findViewById(R.id.cardShortDes);

            view.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(activity1, MeetingInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Meetings meeting = meetingList.get(getAdapterPosition());
            intent.putExtra("Meeting Name", meeting.getName());
            intent.putExtra("Meeting Location", meeting.getLocation() );
            String date = meeting.getMonth()+"/"+meeting.getDay()+"/"+meeting.getYear();
            String time = meeting.getHour()+":" + meeting.getMinute();
            intent.putExtra("Meeting Date", date);
            context.startActivity(intent);
        }


    }

    public MeetingAdapterList(List<Meetings> meetingList, Context context, Activity activity1 ) {
        this.meetingList = meetingList;
        this.context = context;
        this.activity1 = activity1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meetings meeting = meetingList.get(position);
        System.out.println("Meeing Name!!!!!!!!!!!!!!1: "+ meeting.getName());
        holder.name.setText(meeting.getName());
        holder.shortDes.setText(meeting.getDecription());
        String date = meeting.getMonth()+"/"+meeting.getDay()+"/"+meeting.getYear();
        holder.date.setText(date);
        String rsvp = Integer.toString(meeting.getAttendess().size())+" "+"RSVP'd";
        holder.rsvp.setText(rsvp);

        //holder.numMembers.setText(Integer.toString(group.getMembers().size()));
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }
}
