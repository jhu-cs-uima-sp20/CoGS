package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private TextView name;
    private TextView courses;
    private CircularImageView image;
    private Button saveBtn;
    private Button logoutBtn;
    private FirebaseAuth auth;
    private DatabaseReference reff;
    private ImageButton imageEditBtn;
    private Uri filePath;
    private String imageUrl;
    private FirebaseStorage myStorage;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name = findViewById(R.id.userName);
        courses = findViewById(R.id.userClasses);
        image = findViewById(R.id.userImage);
        saveBtn = findViewById(R.id.saveBtn);
        imageEditBtn = findViewById(R.id.imageButtonEdit);
        auth = FirebaseAuth.getInstance();
        myStorage = FirebaseStorage.getInstance();
        imageUrl = "tempUrl";
        toolbar = findViewById(R.id.settingsToolbar);
        logoutBtn = findViewById(R.id.logOutButton);

        reff = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        getSettings();
        saveBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String nameStr = name.getText().toString();
                 String coursesStr = courses.getText().toString();
                 User newUser = new User();
                 newUser.setName(nameStr);
                 newUser.setCourses(coursesStr);
                 newUser.setImageUrl(imageUrl);
                 reff.setValue(newUser);
                 saveImage();

                 Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                 startActivity(intent);
                 finish();

             }
        });

        imageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        toolbar.setTitle("Settings");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    void saveImage(){

        myStorage.getReference().child("User Image").child(auth.getUid()).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                myStorage.getReference().child("User Image").child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        reff.child("imageUrl").setValue(imageUrl);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SettingsActivity.this, "Error Uploading Image", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void getSettings(){

        if(reff!=null) {
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userCourses = dataSnapshot.child("courses").getValue().toString();
                        name.setText(userName);
                        courses.setText(userCourses);
                        String imageLink = dataSnapshot.child("imageUrl").getValue().toString();
                        imageUrl = imageLink;
                        Picasso.get().load(imageLink).into(image);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

}
