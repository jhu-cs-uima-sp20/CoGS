package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class GroupActivity extends AppCompatActivity {

    private Fragment meetingsFrag;
    private Fragment notesFrag;
    private String groupName;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        meetingsFrag = new MeetingsFragment();
        //notesFrag =
        toolbar = findViewById(R.id.group_toolbar);
        tabLayout = findViewById(R.id.tabLayout);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.group_fragment_container, meetingsFrag).commit();

        Intent intent = getIntent();
        groupName = intent.getStringExtra("Group Name");

        toolbar.setTitle(groupName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        //transaction.replace(R.id.group_fragment_container, notesFrag).commit();
                    case 1:
                        transaction.replace(R.id.group_fragment_container, meetingsFrag).commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main__group_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingsGroupIcon) {
            Intent intent = new Intent(GroupActivity.this, GroupSettingsActivity.class);
            intent.putExtra("Group Name", groupName);
            startActivity(intent);
            return true;
        } else if (id == R.id.searchIcon) {
            Intent intent = new Intent(GroupActivity.this, SearchGroupActivity.class);
            startActivity(intent);
            return true;
        }

            return super.onOptionsItemSelected(item);
        }
    }
