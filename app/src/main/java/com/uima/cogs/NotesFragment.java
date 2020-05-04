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
import android.widget.AdapterView;
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
    private DatabaseReference databaseReference;


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
        root = inflater.inflate(R.layout.fragment_notes, container, false);

        FloatingActionButton fabN = root.findViewById(R.id.floatingActionButtonNote);
        Intent intent2 = getActivity().getIntent();
        gName = intent2.getStringExtra("Group Name");
        gv = root.findViewById(R.id.noteGV);

        fabN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                intent.putExtra("Group Name", gName);
                startActivity(intent);
            }
        });



        noteList = new ArrayList<>();
        customAdapter = new CustomAdapter1(noteList, getActivity());
        gv.setAdapter(customAdapter);

        System.out.println("Group Name: "+gName);
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(gName);
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                customAdapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Notes notes = postSnapshot.getValue(Notes.class);
                    System.out.println("Get Data: "+ notes.getName());
                    noteList.add(0, notes);
                    //customAdapter.notifyItemInserted(0);
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                System.out.println("Position!!!!!!!!1: "+position);
                Intent intent = new Intent(getActivity(), NoteViewActivity.class);
                Notes viewNote = noteList.get(position);
                intent.putExtra("Notes Name", viewNote.getName());
                intent.putExtra("Notes URL", viewNote.getImageUrl());
                intent.putExtra("Notes ID", viewNote.getNoteId());
                startActivity(intent);

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
