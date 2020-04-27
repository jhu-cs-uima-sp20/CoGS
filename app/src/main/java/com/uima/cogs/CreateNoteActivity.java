package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageView image;
    private Button findNoteBtn;
    private Button uploadNoteBtn;
    private Intent intent;
    private FirebaseAuth auth;
    private SharedPreferences mPrefs;
    private Toolbar toolbar;
    private Uri filePath;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        image = findViewById(R.id.noteImage);
        findNoteBtn = findViewById(R.id.findNoteButton);
        uploadNoteBtn = findViewById(R.id.uploadNoteButton);
        toolbar = findViewById(R.id.notestoolbar);

        auth = FirebaseAuth.getInstance();

        uploadNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void saveNote(String groupName, Notes note){
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Notes");
        noteRef.push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "New Note Has Been Successfully Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
