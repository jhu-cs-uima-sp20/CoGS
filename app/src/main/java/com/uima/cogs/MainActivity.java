package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Button groupPageBtn;
    private Fragment groupFrag;
    private Fragment homeFeedFrag;
    private FrameLayout fragment_container;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        fragment_container = findViewById(R.id.fragment_container);
        groupFrag = new GroupListFragment();
        homeFeedFrag = new HomeFeedFragment();
        if (currentUser == null) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar = findViewById(R.id.main_toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        loadView(0);
        groupPageBtn = findViewById(R.id.groupPageBtn);

        groupPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView(1);
            }
        });

        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingsIcon) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.searchIcon) {
            Intent intent = new Intent(MainActivity.this, SearchGroupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadView(int frag) {
        fragment_container.removeAllViews();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (frag) {
            case 0:
                transaction.replace(R.id.fragment_container, homeFeedFrag).commit();
                toolbar.setTitle("Home Feed");
                break;
            case 1:
                transaction.replace(R.id.fragment_container, groupFrag).commit();
                toolbar.setTitle("Groups");
                break;
            case 2:
                //transaction.replace(R.id.fragment_container, groupFrag).commit();
                toolbar.setTitle("Liked");
                break;
            default:
                ;
        }
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {
        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new HomeFeedFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Groups";
                default:
                    return "Home";
                case 2:
                    return "Liked";
            }
        }
    }

}

