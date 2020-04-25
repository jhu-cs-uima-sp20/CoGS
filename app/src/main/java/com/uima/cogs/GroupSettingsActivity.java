package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroupSettingsActivity extends AppCompatActivity {

    private Fragment groupMemberFrag;
    private Button membersBtn;
    private Button groupSettingBtn;
    private FragmentTransaction transaction;
    private String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        membersBtn = findViewById(R.id.groupMemberBtn);
        groupMemberFrag = new GroupMemeberFragment();

        transaction = getSupportFragmentManager().beginTransaction();

        membersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.settings_fragment_container, groupMemberFrag).commit();
            }
        });

    }
}
