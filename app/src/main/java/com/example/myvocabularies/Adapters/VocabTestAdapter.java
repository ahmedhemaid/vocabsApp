package com.example.myvocabularies.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Activities.HomeActivity;
import com.example.myvocabularies.Dialogs.OptionsDialog;
import com.example.myvocabularies.Model.Vocab;
import com.example.myvocabularies.R;

import java.util.ArrayList;

public class VocabTestAdapter extends RecyclerView.Adapter<VocabTestAdapter.ViewHolder> {

    private  Context context;
    private ArrayList<Vocab> data;
    public static ArrayList<String>answersInAdapter;
    private OnItemClickListener mListener;
    public VocabTestAdapter(ArrayList<Vocab> data, Context context){
        this.data = data;
        this.context = context;


    }

    //onItemClick
    public interface OnItemClickListener{
        void onItemClick(int position);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_test_vocab, parent, false);
        ViewHolder holder = new ViewHolder(view);
        answersInAdapter=new ArrayList<>();
        for (int i=0;i<HomeActivity.staticVocabs.size();i++){
            answersInAdapter.add("asd");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.meaning.setText(data.get(position).meaning);
        holder.index.setText((position+1)+"-");
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                HomeActivity.vocabIDToHint=HomeActivity.staticVocabs.get(position).Id;
                openDialog();
                return true;
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {
        public EditText word;
        public TextView meaning,index;

        public ViewHolder(final View itemView) {
            super(itemView);
            word=itemView.findViewById(R.id.word_in_card_test);
            meaning=itemView.findViewById(R.id.meaning_in_card_test);
            index=itemView.findViewById(R.id.index_in_card_test);
            final Vocab vocab=new Vocab();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            word.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                answersInAdapter.set(getAdapterPosition(),word.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
//                    if (word.getText().toString().equals(HomeActivity.staticVocabs.get(getAdapterPosition()).word)){
//                        Test.correctAnswers.add(getAdapterPosition(),word.getText().toString());
//                        Log.d("change", "onTextChanged: "+word.getText().toString()+"  "+HomeActivity.staticVocabs.get(getAdapterPosition()).word+" "+Test.correctAnswers.size());
//                    }
//                    else{
//                        Test.wrongAnswers.add(getAdapterPosition(),word.getText().toString());
//                        Log.d("change", "onTextChanged:R "+word.getText().toString()+"  "+HomeActivity.staticVocabs.get(getAdapterPosition()).word);
//                    }
                }
            });
        }
    }
    //open dialog method
    public void openDialog() {
        OptionsDialog dialog = new OptionsDialog();
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "example dialog");
    }


}

