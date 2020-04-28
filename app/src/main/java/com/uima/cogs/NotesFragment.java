package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private View root;
    private GridView gv;
    private String gName;
    private ArrayList<Notes> noteList;
    private CustomAdapter1 customAdapter;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_meetings, container, false);

        FloatingActionButton fabN = root.findViewById(R.id.floatingActionButtonNote);
        gv = root.findViewById(R.id.noteGV);
        Intent intent2 = getActivity().getIntent();
        gName = intent2.getStringExtra("Group Name");

        noteList = new ArrayList<>();
        customAdapter = new CustomAdapter1(noteList, getActivity());
        gv.setAdapter(customAdapter);

        fabN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                intent.putExtra("Group Name", gName);
                startActivity(intent);
            }
        });

        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(gName).child("Group Notes");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customAdapter.notifyDataSetChanged();
                Notes note = dataSnapshot.getValue(Notes.class);
                noteList.add(note);
                System.out.println("Note!!!!!!!!!!!!!!: "+ note.getName());
                customAdapter.notifyDataSetChanged();
                gv.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}

class CustomAdapter1 extends BaseAdapter{

    private ArrayList<Notes> notes;
    private LayoutInflater inflter;

    public CustomAdapter1(ArrayList<Notes> noteList, Context applicationContext){
        notes = noteList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {
        return notes.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = inflter.inflate(R.layout.note_grid_view, null);
        ImageView noteImage = view1.findViewById(R.id.gridViewNoteImage);
        TextView noteName = view1.findViewById(R.id.gridViewNoteName);
        noteName.setText(notes.get(position).getName());
        Picasso.get().load(notes.get(position).getImageUrl()).into(noteImage);
        return view1;
    }
}
