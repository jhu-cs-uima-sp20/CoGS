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
        notesFrag = new NotesFragment();
        toolbar = findViewById(R.id.group_toolbar);
        tabLayout = findViewById(R.id.tabLayout);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //Once notes is implemented change to notes frag
        transaction.add(R.id.group_fragment_container, meetingsFrag).commit();

        //Delete below after notes is implemented
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        assert tab != null;
        tab.select();
        //End Delete

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
                        transaction.replace(R.id.group_fragment_container, notesFrag).addToBackStack(null).commit();
                        break;
                    case 1:
                        transaction.replace(R.id.group_fragment_container, meetingsFrag).addToBackStack(null).commit();
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
