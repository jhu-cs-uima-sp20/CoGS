package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

public class NoteViewActivity extends AppCompatActivity {

    private ImageView image;
    private ImageView icon;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private boolean like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        toolbar = findViewById(R.id.note_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        like = false;
        image = findViewById(R.id.noteImageLargeView);
        icon = findViewById(R.id.favouriteIcon);

        Intent intent = getIntent();
        final String noteName = intent.getStringExtra("Notes Name");
        toolbar.setTitle(noteName);
        final String noteId = intent.getStringExtra("Notes ID");
        final String noteURL = intent.getStringExtra("Notes URL");
        Picasso.get().load(noteURL).into(image);

        //icon.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
        icon.setColorFilter(Color.RED);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes note = new Notes();
                note.setName(noteName);
                note.setImageUrl(noteURL);
                note.setNoteId(noteId);
                likeNote(note);
            }
        });

        auth = FirebaseAuth.getInstance();
        DatabaseReference likeReff = FirebaseDatabase.getInstance().getReference().child("Liked Notes").child(auth.getUid());
        likeReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild(noteId)) {
                        like = true;
                        icon.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light), PorterDuff.Mode.MULTIPLY);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void likeNote(Notes note){
        String id = note.getNoteId();
        DatabaseReference likeReff = FirebaseDatabase.getInstance().getReference().child("Liked Notes").child(auth.getUid()).child(id);
        if(!like) {
            likeReff.setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    like = true;
                    icon.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light), PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(getApplicationContext(), "like", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{

            likeReff.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    like = false;
                    icon.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(getApplicationContext(), "unlike", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
