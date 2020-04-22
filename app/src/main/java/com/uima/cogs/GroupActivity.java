package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroupActivity extends AppCompatActivity {

    private Fragment meetingsFrag;
    private Fragment notesFrag;
    private Button meetingsBtn;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        meetingsBtn = findViewById(R.id.meetingBtn);
        meetingsFrag = new MeetingsFragment();

        transaction = getSupportFragmentManager().beginTransaction();
        meetingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.group_fragment_container, meetingsFrag).commit();
            }
        });
    }
}
