package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name;
    private Button findNoteBtn;
    private Button uploadNoteBtn;
    private Toolbar toolbar;
    private Uri filePath;
    private String imageUrl;
    private FirebaseStorage noteStorageReference;
    private DatabaseReference noteDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        image = findViewById(R.id.noteImage);
        name = findViewById(R.id.noteName);
        findNoteBtn = findViewById(R.id.findNoteButton);
        uploadNoteBtn = findViewById(R.id.uploadNoteButton);
        toolbar = findViewById(R.id.notestoolbar);
        imageUrl = "temp";
        noteStorageReference = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        final String groupName = intent.getStringExtra("Group Name");
        noteDatabaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(groupName);

        uploadNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteName = name.getText().toString();
                Notes newNote = new Notes();
                newNote.setName(noteName);
                //uploadNote(groupName);
                newNote.setImageUrl(imageUrl);
                saveNote(groupName, newNote);
            }
        });

        findNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public void uploadNote(String groupName) {
        if (filePath != null) {
            noteStorageReference.getReference().child(groupName).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String noteName = name.getText().toString();
                    String noteID = noteDatabaseReference.push().getKey();
                    Notes note = new Notes(noteName, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    noteDatabaseReference.child(noteID).setValue(note);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNoteActivity.this, "Error uploading Image", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    /*
    void uploadNote(String groupName){
        noteStorage.getReference().child("Groups").child(groupName).child("Group Notes").putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                noteStorage.getReference().child("Groups").child(groupName).child("Group Notes").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                Toast.makeText(CreateNoteActivity.this, "Error Uploading Image", Toast.LENGTH_SHORT).show();
                // ...
            }
        });
    }


     */

    /*
    void uploadNote(String groupName) {
        (filePath != null) {
            StorageReference noteRef = storageReference.getInstance().getReference().child("Groups").child(groupName).child("Group Notes");
            noteRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateNoteActivity.this, "Note Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNoteActivity.this, "Note Upload Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

     */


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
