package com.example.myvocabularies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Activities.HomeActivity;
import com.example.myvocabularies.Dialogs.OptionsDialogArchive;
import com.example.myvocabularies.Model.Vocab;
import com.example.myvocabularies.R;

import java.util.ArrayList;

public class VocabArchiveAdapter extends RecyclerView.Adapter<VocabArchiveAdapter.ViewHolder> {

    private  Context context;
    private ArrayList<Vocab> data;
    private OnItemClickListener mListener;

    public VocabArchiveAdapter(ArrayList<Vocab> data, Context context){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vocab_archive, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.word.setText(data.get(position).word);
        holder.meaning.setText(data.get(position).meaning);
        holder.index.setText((position+1)+"-");
        holder.example.setText("For example : "+data.get(position).example);

        holder.voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.speak(holder.word.getText().toString());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                HomeActivity.currentVocabId =HomeActivity.archiveVocabs.get(position).Id;
                HomeActivity.currentVocabWord =HomeActivity.archiveVocabs.get(position).word;
                HomeActivity.currentVocabMeaning =HomeActivity.archiveVocabs.get(position).meaning;
                HomeActivity.currentVocabExample =HomeActivity.archiveVocabs.get(position).example;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView word,meaning,example,index;
        public ImageView voice;
        public ViewHolder(View itemView) {
            super(itemView);
            word=itemView.findViewById(R.id.word_in_card);
            meaning=itemView.findViewById(R.id.meaning_in_card);
            example=itemView.findViewById(R.id.example_in_card);
            index=itemView.findViewById(R.id.index_in_card);
            voice=itemView.findViewById(R.id.voice_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (meaning.getVisibility()==View.INVISIBLE){
                        meaning.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }
    //open dialog method
    public void openDialog() {
        OptionsDialogArchive dialog = new OptionsDialogArchive();
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "example dialog");
    }

}

