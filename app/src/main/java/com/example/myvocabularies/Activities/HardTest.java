package com.example.myvocabularies.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocabularies.Adapters.VocabTestAdapter;
import com.example.myvocabularies.NotificationClasses.MyNotificationPublisher;
import com.example.myvocabularies.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.myvocabularies.Activities.HomeActivity.initVocabData;
import static com.example.myvocabularies.Activities.HomeActivity.moveCorrectToArchive;
import static com.example.myvocabularies.Activities.HomeActivity.staticVocabs;
import static com.example.myvocabularies.Dialogs.TimeOfNotification.notificationHour;

public class HardTest extends AppCompatActivity  {
    private static RecyclerView testVocabsRecyclerView;
    static VocabTestAdapter vocabsAdapter;
    private RecyclerView.LayoutManager vocabsLayoutManager;
    public static ArrayList<String> editContents=new ArrayList<>(staticVocabs.size());
    public static ArrayList<String>correctAnswers;
    public static ArrayList<String>wrongAnswers;
    String correctString="";
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        editContents=new ArrayList<>();
        correctAnswers=new ArrayList<>(staticVocabs.size());
        wrongAnswers=new ArrayList<>(staticVocabs.size());
        getSupportActionBar().setTitle("Test vocabs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //calender
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        //firebase
        HomeActivity.mDatabase = FirebaseDatabase.getInstance().getReference();
        initVocabData();
        //process of recycler of vocabs
        testVocabsRecyclerView =  findViewById(R.id.recycler_view_all_vocabs_test);
        testVocabsRecyclerView.setHasFixedSize(true);
        vocabsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        testVocabsRecyclerView.setLayoutManager(vocabsLayoutManager);
        vocabsAdapter = new VocabTestAdapter(staticVocabs,this);
        testVocabsRecyclerView.setAdapter(vocabsAdapter);
        //notification
        scheduleNotification(getNotification( "don't be lazy , let's have the daily english test")) ;
    }
    //test process
    public void done_test(View view) {

        int res=0;
        for (int i = 0; i < staticVocabs.size(); i++){
            if (VocabTestAdapter.answersInAdapter.get(i).trim().toLowerCase().equals(staticVocabs.get(i).word.trim().toLowerCase())){
                moveCorrectToArchive(staticVocabs.get(i));
                correctString+="\n"+staticVocabs.get(i).word;
                res++;
            }
            else{

            }
        }
        //test result dialog
         AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage("Your result is "+res+"/"+staticVocabs.size()+"\nAll correct answers moved to archive\n\nCorrect Answers :"+correctString)
                .setPositiveButton("Ok", null)
                .setNegativeButton("go to archive", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HardTest.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HardTest.this,Archive.class);
                startActivity(intent);
                finish();
            }
        });

    }
    //notification part
    private void scheduleNotification (Notification notification ) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager1 != null;
        alarmManager1.setRepeating( AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent );

    }
    private Notification getNotification (String content) {
        PendingIntent notificIntent = PendingIntent.getActivity(this, 0, new Intent(HardTest.this, HomeActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
                builder.setContentTitle( "Daily Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.notifications ) ;
        builder.setContentIntent(notificIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

}