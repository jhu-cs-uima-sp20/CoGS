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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private BottomNavigationView bottomNavigation;
    private int curView = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        fragment_container =findViewById(R.id.fragment_container);
        groupFrag = new GroupListFragment();
        homeFeedFrag = new HomeFeedFragment();
       if(currentUser==null){

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    toolbar = findViewById(R.id.main_toolbar);
    toolbar.setTitle("Home Feed");
    tabLayout = findViewById(R.id.tab_layout);
    viewPager = findViewById(R.id.pager);
    setSupportActionBar(toolbar);

    bottomNavigation = findViewById(R.id.bottom_navigation);

    bottomNavigation.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_group:
                            loadView(1, "");
                            //viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.nav_home:
                            loadView(0, "");
                            tabLayout.setVisibility(View.GONE);
                            return true;
                        case R.id.nav_liked:
                            loadView(2, "");
                            return true;
                    }
                    return false;
                }
            });

        loadView(0, "");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch(curView) {
            case 0:
                getMenuInflater().inflate(R.menu.main_menu, menu);
                return true;
            case 1:
                getMenuInflater().inflate(R.menu.main_groups_menu, menu);
                return true;
            case 2:
                getMenuInflater().inflate(R.menu.main_liked_menu, menu);
                return true;
            case 3:
                getMenuInflater().inflate(R.menu.main__group_detail_menu, menu);
                return true;
                //for detailed group view
            default:;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settingsIcon) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.searchIcon) {
            Intent intent = new Intent(MainActivity.this, SearchGroupActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.groupFilterIcon) {
            //For filter groups
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //GroupName used when loading detailed Group View
    private void loadView(int frag, String groupName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(frag) {
            case 0: //Home
                transaction.replace(R.id.fragment_container, homeFeedFrag).commit();
                toolbar.setTitle("Home Feed");
                curView = 0;
                break;
            case 1: //Groups
                transaction.replace(R.id.fragment_container, groupFrag).commit();
                toolbar.setTitle("Groups");
                curView = 1;
                break;
            case 2: //Implement later Liked Notes
                //transaction.replace(R.id.fragment_container, groupFrag).commit();
                toolbar.setTitle("Liked");
                curView = 2;
                break;
            case 3: //Detailed Group View
                toolbar.setTitle(groupName);
                curView = 3;
                break;
            default:;
        }
        invalidateOptionsMenu();
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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
                    return "Notes";
                case 1:
                    return "Meetings";
            }
        }
    }
}
