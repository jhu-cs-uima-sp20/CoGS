package com.uima.cogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LikedFragment extends Fragment {
    private View root;
    private GridView gv;
    private ArrayList<Notes> likeNotesList;
    private CustomAdapter2 customAdapter;

    public LikedFragment() {
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
        root = inflater.inflate(R.layout.fragment_liked, container, false);
        gv = root.findViewById(R.id.likeGV);
        likeNotesList = new ArrayList<>();
        customAdapter = new CustomAdapter2(likeNotesList, getActivity());
        gv.setAdapter(customAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("Liked Notes").child(auth.getUid());
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likeNotesList.clear();
                customAdapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Notes notes = postSnapshot.getValue(Notes.class);
                    System.out.println("Get Data: "+ notes.getName());
                    likeNotesList.add(0, notes);
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
                Notes viewNote = likeNotesList.get(position);
                intent.putExtra("Notes Name", viewNote.getName());
                intent.putExtra("Notes URL", viewNote.getImageUrl());
                intent.putExtra("Notes ID", viewNote.getNoteId());
                startActivity(intent);

            }
        });


        return root;
    }
}
class CustomAdapter2 extends BaseAdapter {

    private ArrayList<Notes> notes;
    private LayoutInflater inflter;

    public CustomAdapter2(ArrayList<Notes> noteList, Context applicationContext){
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