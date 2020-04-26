package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class GroupSettingsActivity extends AppCompatActivity {

    private Fragment groupMemberFrag;
    private Fragment settingFrag;
    private String groupName;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        tabLayout = findViewById(R.id.groupSettingsTabLayout);
        toolbar= findViewById(R.id.groupSettingsToolbar);

        groupMemberFrag = new GroupMemeberFragment();
        settingFrag = new GroupSettingsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_fragment_container, settingFrag).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        transaction.replace(R.id.settings_fragment_container, settingFrag).commit();
                        break;
                    case 1:
                        transaction.replace(R.id.settings_fragment_container, groupMemberFrag).commit();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = getIntent();
        groupName = intent.getStringExtra("Group Name");

        toolbar.setTitle(groupName + " Settings");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
