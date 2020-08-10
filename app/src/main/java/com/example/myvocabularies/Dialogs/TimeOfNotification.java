package com.example.myvocabularies.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myvocabularies.Activities.HardTest;
import com.example.myvocabularies.R;

import static android.content.Context.MODE_PRIVATE;

public class TimeOfNotification extends AppCompatDialogFragment {
    Button done_btn;
    Spinner spinner;
    RadioGroup radioGroup;
    RadioButton am,pm;
    public static int notificationHour;
    private ExampleDialogListener listener;
    String s;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.timenotification, null);
        builder.setView(view);
        notificationHour=1;
        am=view.findViewById(R.id.am);
        pm=view.findViewById(R.id.pm);
        radioGroup=view.findViewById(R.id.radio_group);
        am.setChecked(true);
        spinner=view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(view.getContext(),R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        done_btn = view.findViewById(R.id.done_time);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = view.getContext().getSharedPreferences("Prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("First_test",false);
                editor.apply();
                if (am.isChecked()) {
                    s = "am";
                    notificationHour+=spinner.getSelectedItemPosition();
                }
                else {
                    s = "pm";
                    notificationHour+=spinner.getSelectedItemPosition()+12;
                }
                Toast.makeText(view.getContext(), "your daily quiz time will be at "+notificationHour+" "+s, Toast.LENGTH_LONG).show();
                dismiss();
                Intent intent=new Intent(view.getContext(), HardTest.class);
                startActivity(intent);
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