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
import java.util.Calendar;
import java.util.Date;

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
    private Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        currentTime = Calendar.getInstance().getTime();

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
                if(noteName.isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Please Enter A Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadNote(groupName, noteName);
                }

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

    public void uploadNote(String groupName, final String noteName) {
        final String noteID = noteDatabaseReference.push().getKey();
        if (filePath != null) {
            noteStorageReference.getReference().child("Group Notes").child(noteID).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    noteStorageReference.getReference().child("Group Notes").child(noteID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            Notes note = new Notes();
                            note.setName(noteName);
                            note.setImageUrl(imageUrl);
                            note.setNoteId(noteID);
                            noteDatabaseReference.child(noteID).setValue(note);
                            finish();
                        }
                    });
                    Toast.makeText(CreateNoteActivity.this, "Image uploaded successfully",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNoteActivity.this, "Error uploading Image", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{

            Toast.makeText(CreateNoteActivity.this, "Please select an Image", Toast.LENGTH_SHORT).show();
        }
    }



}
