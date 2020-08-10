package com.example.myvocabularies.Model;

import com.google.firebase.database.FirebaseDatabase;

public class Vocab {
    public String Id;
    public String word;
    public String meaning;
    public String example;
    public Vocab(){}
    public Vocab(String Id,String word,String meaning,String example){
        this.Id=Id;
        this.word=word;
        this.meaning=meaning;
        this.example=example;
    }
    public static String generateVocabID(){
        return FirebaseDatabase.getInstance().getReference().child("User").child("Vocab").push().getKey();
    }
}
