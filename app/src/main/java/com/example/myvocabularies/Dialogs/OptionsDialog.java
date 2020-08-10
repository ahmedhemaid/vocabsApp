package com.example.myvocabularies.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myvocabularies.Activities.HomeActivity;
import com.example.myvocabularies.R;

public class OptionsDialog extends AppCompatDialogFragment {
    Button delete_btn;
    Button archive_btn;
    Button edit_btn;
    private ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        delete_btn = view.findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.deleteVocab(getContext());
                dismiss();
            }
        });
        edit_btn = view.findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.editVocab(getContext());
                dismiss();
            }
        });
        archive_btn = view.findViewById(R.id.archive_btn);
        archive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.moveToArchive(getContext());
                dismiss();
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ExampleDialogListener {
    }
}