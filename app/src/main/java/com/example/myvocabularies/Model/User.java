package com.example.myvocabularies.Model;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String id;
    public Map<String , Vocab> vocabs  = new HashMap<>();
    public User() {
    }
}
