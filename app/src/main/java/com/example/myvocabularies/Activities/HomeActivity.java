package com.example.myvocabularies.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Adapters.VocabAdapter;
import com.example.myvocabularies.Adapters.VocabArchiveAdapter;
import com.example.myvocabularies.Dialogs.OptionsDialog;
import com.example.myvocabularies.Dialogs.TimeOfNotification;
import com.example.myvocabularies.Model.Vocab;
import com.example.myvocabularies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OptionsDialog.ExampleDialogListener , TimeOfNotification.ExampleDialogListener {
    public static TextToSpeech mTTS;
    public static ArrayList<Vocab> staticVocabs=new ArrayList<>();
    public static ArrayList<Vocab> archiveVocabs=new ArrayList<>();
    public static String vocabIDToHint;
    public static DatabaseReference mDatabase;
    private static RecyclerView vocabsRecyclerView;
    static VocabAdapter vocabsAdapter;
    static VocabArchiveAdapter vocabArchiveAdapter;
    private RecyclerView.LayoutManager vocabsLayoutManager;
    static RelativeLayout notFirst;
    static LinearLayout isFirst;
    public static int isEdit;
    public static String currentVocabId, currentVocabWord, currentVocabMeaning, currentVocabExample;
    static int size=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        isFirst=findViewById(R.id.first_vocab);
        notFirst=findViewById(R.id.not_first_vocab);
        isEdit=0;
        //prepare speech
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        //firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initVocabData();
        //sharedPreferences
        SharedPreferences preferences=getSharedPreferences("Prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("is_logged_in",true);
        editor.apply();
        //process of recycler of vocabs
        vocabsRecyclerView =  findViewById(R.id.recycler_view_all_vocabs);
        vocabsRecyclerView.setHasFixedSize(true);
        vocabsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vocabsRecyclerView.setLayoutManager(vocabsLayoutManager);
        vocabsAdapter = new VocabAdapter(staticVocabs,this);
        vocabsRecyclerView.setAdapter(vocabsAdapter);


        }
    //first vocab button
    public void makeFirstVocab(View view) {
        Intent intent=new Intent(this, NewVocab.class);
        startActivity(intent);

    }
    //put data on firebase
    public static void writeVocab(Vocab vocab) {
        String userId = FirebaseAuth.getInstance().getUid();
        Log.d("000", "writeVocab: "+userId);
        mDatabase.child("user").child(userId).child("vocab").child(vocab.Id).setValue(vocab);
    }
    //put data on firebase to archive
    public static void writeArchivedVocab(Vocab vocab) {
        String userId = FirebaseAuth.getInstance().getUid();
        Log.d("000", "writeVocab: "+userId);
        mDatabase.child("user").child(userId).child("archive").child(vocab.Id).setValue(vocab);
    }
    //logout
    public void logout() {
        SharedPreferences preferences=getSharedPreferences("Prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("is_logged_in",false);
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    //get data from firebase
    public static void initVocabData() {
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("vocab")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        staticVocabs.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                            Vocab vocab = snapshot.getValue(Vocab.class);
                            staticVocabs.add(vocab);
                            size=1;
                        }
                        vocabsAdapter.notifyDataSetChanged();
                        if (staticVocabs.size()==0){
                            vocabsRecyclerView.setVisibility(View.INVISIBLE);
                            isFirst.setVisibility(View.VISIBLE);
                            notFirst.setVisibility(View.INVISIBLE);

                        }else {
                            vocabsRecyclerView.setVisibility(View.VISIBLE);
                            isFirst.setVisibility(View.INVISIBLE);
                            notFirst.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
    //get archived data from firebase
    public static void getArchivedData() {
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("archive")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        archiveVocabs.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                            Vocab vocab = snapshot.getValue(Vocab.class);
                            archiveVocabs.add(vocab);
                            size=1;
                        }
                        vocabArchiveAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
    //speech method
    public static void speak(String s){
        String text =s;
        mTTS.setSpeechRate(0.7f);
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    //options menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_vocab:
                Intent intent=new Intent(this, NewVocab.class);
                startActivity(intent);
                return true;
            case R.id.easy_test:
                intent=new Intent(this, EasyTest.class);
                startActivity(intent);
                return true;
            case R.id.hard_test:
                //sharedPreference
                SharedPreferences preferences;
                preferences=getSharedPreferences("Prefs",MODE_PRIVATE);
                if (preferences.getBoolean("First_test", true)) {
                    openDialog();
                }else{
                    intent=new Intent(this, HardTest.class);
                    startActivity(intent);
                }

                return true;
            case R.id.archive:
                intent=new Intent(this, Archive.class);
                startActivity(intent);
            return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //dialog options methods
    //delete vocab method
    public static void deleteVocab(Context context){
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        DatabaseReference vocabRef=FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("vocab").child(currentVocabId);
        vocabRef.removeValue();
    }
    //move to archive
    public static void moveToArchive(Context context){
        Toast.makeText(context, "Moved to archive", Toast.LENGTH_SHORT).show();
        DatabaseReference vocabRef=FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("vocab").child(currentVocabId);
        vocabRef.removeValue();
        Vocab vocab=new Vocab(currentVocabId,currentVocabWord,currentVocabMeaning,currentVocabExample);
        archiveVocabs.add(vocab);
        writeArchivedVocab(vocab);

    }
    //move to archive / firebase
    public static void moveCorrectToArchive(Vocab vocab){

        DatabaseReference vocabRef=FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("vocab").child(vocab.Id);
        vocabRef.removeValue();
        archiveVocabs.add(vocab);
        writeArchivedVocab(vocab);

    }
    //edit
    public static void editVocab(Context context){
        Intent intent=new Intent(context,NewVocab.class);
        intent.putExtra("word_to_edit", currentVocabWord);
        intent.putExtra("meaning_to_edit", currentVocabMeaning);
        intent.putExtra("example_to_edit", currentVocabExample);
        isEdit=1;
        context.startActivity(intent);
    }
    //dialog of notification
    public void openDialog() {
        TimeOfNotification dialog = new TimeOfNotification();
        dialog.show(((AppCompatActivity)this).getSupportFragmentManager(), "example dialog");
    }


}
