package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    setSupportActionBar(toolbar);

    bottomNavigation = findViewById(R.id.bottom_navigation);

    bottomNavigation.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_group:
                            loadView(1);
                            Toast.makeText(getApplicationContext(), "TEST", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.nav_home:
                            loadView(0);
                            return true;
                        case R.id.nav_liked:
                            loadView(2);
                            return true;
                    }
                    return false;
                }
            });

        loadView(0);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadView(int frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(frag) {
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
            default:;
        }
    }

}
