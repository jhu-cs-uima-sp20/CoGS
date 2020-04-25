package com.uima.cogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GroupSettingsFragment extends Fragment {

    private TextView course;
    private EditText shortDes;
    private TextView name;
    private Button saveBtn;
    private View root;
    private String groupName;
    public GroupSettingsFragment() {
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
        root = inflater.inflate(R.layout.fragment_group_settings, container, false);
        course = root.findViewById(R.id.groupCourseNameSettings);
        shortDes = root.findViewById(R.id.groupShortSettings);
        saveBtn = root.findViewById(R.id.saveGroupSettingsBtn);
        name = root.findViewById(R.id.groupNameSettings);

        groupName = getActivity().getIntent().getStringExtra("Group Name");
        name.setText(groupName);

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Description");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupDescription groupDes = dataSnapshot.getValue(GroupDescription.class);
                course.setText(groupDes.getCourseName());
                shortDes.setText(groupDes.getShortDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shortStr = shortDes.getText().toString();
                saveData(shortStr);
            }
        });

        return root;
    }

    public void saveData(String shortStr){
        DatabaseReference shortRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Description").child("shortDescription");
        shortRef.setValue(shortStr).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
