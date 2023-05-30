package com.example.takvim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takvim.databinding.ActivityMainBinding;
import com.example.takvim.databinding.ActivityTakvimBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class TakvimActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManagerCompat;
    public static final  String title = "Takvim";
    public static final  String CHANNEL_1_ID = "channel1";
    private ActivityTakvimBinding binding;
    private DatabaseReference databaseReference;
    private String stringDateSelected;

    private SimpleDateFormat dateFormat;
    private String date;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTakvimBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.createNotificationChannel();

        this.notificationManagerCompat = NotificationManagerCompat.from(this);



        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(stringDateSelected).setValue(binding.takvimEdit.getText().toString());
            }
        });
        binding.btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(TakvimActivity.this,MainActivity.class);
                startActivity(toLogin);
            }
        });


        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(stringDateSelected).removeValue();
            }
        });

        binding.takvim.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth);

                String date = (Integer.toString(dayOfMonth)+"-"+(month+1)+"-"+year);
                binding.txtDate.setText(date);
                calendarClicked();

            }
        });
        binding.btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnChannel();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
    }

    private void sendOnChannel(){

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(binding.txtDate.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        int notificationId = 1;
        this.notificationManagerCompat.notify(notificationId, notification);
    }

    //Bildirim için kanal yaratma
   private void createNotificationChannel() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel channel1 = new NotificationChannel(
                   CHANNEL_1_ID,
                   "Channel 1",
                   NotificationManager.IMPORTANCE_HIGH
           );
           channel1.setDescription("This is channel 1");
           NotificationManager manager = this.getSystemService(NotificationManager.class);
           manager.createNotificationChannel(channel1);
       }
   }


    private void calendarClicked(){
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    binding.takvimEdit.setText(snapshot.getValue().toString());
                    binding.txtCalendar.setText(binding.takvimEdit.getText().toString());
                }else{
                    binding.takvimEdit.setText(" ");
                    binding.txtCalendar.setText(binding.takvimEdit.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
}