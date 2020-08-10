package com.example.myvocabularies.Activities;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Adapters.VocabArchiveAdapter;
import com.example.myvocabularies.R;

public class EasyTest extends AppCompatActivity {
    private RecyclerView.LayoutManager vocabsLayoutManager;
    private  RecyclerView vocabsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_test);
        getSupportActionBar().setTitle("Easy Test");
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
        HomeActivity.vocabArchiveAdapter= new VocabArchiveAdapter(HomeActivity.staticVocabs,this);
        vocabsRecyclerView.setAdapter(HomeActivity.vocabArchiveAdapter);
        //init data
        HomeActivity.initVocabData();
    }

}