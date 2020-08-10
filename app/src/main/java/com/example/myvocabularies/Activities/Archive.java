package com.example.myvocabularies.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Adapters.VocabArchiveAdapter;
import com.example.myvocabularies.Dialogs.OptionsDialogArchive;
import com.example.myvocabularies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myvocabularies.Activities.HomeActivity.currentVocabId;

public class Archive extends AppCompatActivity implements OptionsDialogArchive.ExampleDialogListener {
    private RecyclerView.LayoutManager vocabsLayoutManager;
    private  RecyclerView vocabsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        getSupportActionBar().setTitle("Archive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Information")
                .setMessage("The meanings will be hidden .\nYou should press on the vocab to show its meaning")
                .setPositiveButton("Ok", null)
                .show();
        //recycler
        vocabsRecyclerView =  findViewById(R.id.recycler_view_all_vocabs);
        vocabsRecyclerView.setHasFixedSize(true);
        vocabsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vocabsRecyclerView.setLayoutManager(vocabsLayoutManager);
         HomeActivity.vocabArchiveAdapter= new VocabArchiveAdapter(HomeActivity.archiveVocabs,this);
        vocabsRecyclerView.setAdapter(HomeActivity.vocabArchiveAdapter);
        //init data
        HomeActivity.getArchivedData();
    }
    public static void deleteArchiveVocab(Context context){
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        Log.d("1111", "deleteArchiveVocab: "+currentVocabId);
        DatabaseReference vocabRef= FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("archive").child(currentVocabId);

        vocabRef.removeValue();
    }
}