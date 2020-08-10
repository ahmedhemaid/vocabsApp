package com.example.myvocabularies.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myvocabularies.Model.Vocab;
import com.example.myvocabularies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myvocabularies.Activities.HomeActivity.currentVocabId;

public class NewVocab extends AppCompatActivity {
    EditText word, meaning, example;
    String sWord, sMeaning, sExample;
    TextView trans;
    static Vocab vocab = new Vocab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vocab);
        getSupportActionBar().setTitle("Add vocab");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        word = findViewById(R.id.word_in_new);
        meaning = findViewById(R.id.meaning_in_new);
        example = findViewById(R.id.example_in_new);
        trans = findViewById(R.id.translate_in_new);
        trans.setPaintFlags(trans.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (HomeActivity.isEdit == 1) {
            word.setText(getIntent().getStringExtra("word_to_edit"));
            meaning.setText(getIntent().getStringExtra("meaning_to_edit"));
            example.setText(getIntent().getStringExtra("example_to_edit"));
        }

    }

    //onclick save button to save new vocab
    public void saveNewVocab(View view) {
        sWord = word.getText().toString();
        sMeaning = meaning.getText().toString();
        sExample = example.getText().toString();
        vocab = new Vocab(Vocab.generateVocabID(), sWord, sMeaning, sExample);
        if (HomeActivity.isEdit == 1) {
            DatabaseReference vocabRef = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("vocab").child(currentVocabId);
            vocabRef.removeValue();
            HomeActivity.staticVocabs.add(vocab);
            Toast.makeText(this, "Vocab edited successfully...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vocab added successfully...", Toast.LENGTH_SHORT).show();
            HomeActivity.staticVocabs.add(vocab);
        }
        Intent intent = new Intent(this, HomeActivity.class);
        HomeActivity.writeVocab(vocab);
        startActivity(intent);
        HomeActivity.isEdit = 0;
        finish();
    }

    // TODO: 8/10/2020 translation part 
    public void onClickTranslate(View view) {
//        String translationString = sWord;
//        Http.post(translationString, "en", "ar", new JsonHttpResponseHandler() {
//            @Override
//             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    JSONObject serverResp = new JSONObject(response.toString());
//                    JSONObject jsonObject = serverResp.getJSONObject("data");
//                    JSONArray transObject = jsonObject.getJSONArray("translations");
//                    JSONObject transObject2 = transObject.getJSONObject(0);
//                    meaning.setText(transObject2.getString("translatedText"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
//
    }
}




