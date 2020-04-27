package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JoinGroupActivity extends AppCompatActivity {

    private TextView groupName;
    private TextView groupDescription;
    private TextView groupClass;
    private ArrayList<User> groupMembers;
    private GridView gridView;
    private Button joinBtn;
    private String groupNameStr;
    private FirebaseAuth auth;
    private ArrayList<String> members;
    private CustomAdapter customAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupName = findViewById(R.id.groupNameText1);
        groupDescription = findViewById(R.id.groupDescriptionText1);
        groupClass = findViewById(R.id.groupClassText1);
        gridView = findViewById(R.id.joinGridView);
        joinBtn  = findViewById(R.id.joinGroupBtn);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.joinToolbar);

        Intent intent = getIntent();

        groupNameStr = intent.getStringExtra("Group Name");
        groupName.setText(intent.getStringExtra("Group Name"));
        groupDescription.setText(intent.getStringExtra("Description"));
        groupClass.setText(intent.getStringExtra("Class"));
        members = intent.getStringArrayListExtra("Members");

        groupMembers = new ArrayList<>();
        customAdapter = new CustomAdapter(groupMembers, this);
        gridView.setAdapter(customAdapter);

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupNameStr).child("Group Description").child("members");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                members = dataSnapshot.getValue(t);
                getUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser();
            }
        });

        toolbar.setTitle("Join " + intent.getStringExtra("Group Name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getUsers(){
        for(int i = 0; i< members.size(); i++){

            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(members.get(i));
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    groupMembers.add(user);
                    System.out.println("User!!!!!!!!!!!!!!: "+ user.getName());
                    customAdapter.notifyDataSetChanged();
                    gridView.setAdapter(customAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    public void addUser(){
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupNameStr).child("Group Description").child("members");

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> memberList = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                memberList =  dataSnapshot.getValue(t);
                memberList.add(auth.getUid());
                addUserGroup(memberList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addUserGroup(ArrayList<String> users){

        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupNameStr).child("Group Description").child("members");
        memberRef.setValue(users);
        addGroupToUser();
    }

    public void addGroupToUser(){
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("groups")){
                    ArrayList<String> userGroupList = new ArrayList<>();
                    userGroupList.add(groupNameStr);
                    addGroupToUser2(userGroupList);

                }
                else{
                    ArrayList<String> userGroupList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    userGroupList = dataSnapshot.child("groups").getValue(t);
                    userGroupList.add(groupNameStr);
                    addGroupToUser2(userGroupList);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addGroupToUser2(ArrayList<String> list){
        final DatabaseReference userGroupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("groups");
        userGroupRef.setValue(list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Successfully Joined Group", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}

class CustomAdapter extends BaseAdapter{

    private ArrayList<User> users;
    private LayoutInflater inflter;

    public CustomAdapter(ArrayList<User> userList, Context applicationContext){

        users = userList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {
        return users.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = inflter.inflate(R.layout.user_grid_view, null);
        TextView userName = view1.findViewById(R.id.gridViewUserName);
        CircularImageView userImage = view1.findViewById(R.id.gridViewUserImage);
        userName.setText(users.get(position).getName());
        Picasso.get().load(users.get(position).getImageUrl()).into(userImage);
        return view1;
    }
}
