package abdulahad.imfast.io.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TimePicker tp_time;
    TextView tv_display;
    Button btn_set, btn_reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        } else{
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        tv_display = (TextView)findViewById(R.id.tv_display);
        tp_time = (TimePicker)findViewById(R.id.tp_time);
        btn_set = (Button)findViewById(R.id.btn_set);
        btn_reset = (Button)findViewById(R.id.btn_reset);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                if(Build.VERSION.SDK_INT >= 23) {

                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            tp_time.getHour(),
                            tp_time.getMinute(),
                            0
                    );



                }else{
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            tp_time.getCurrentHour(),
                            tp_time.getCurrentMinute(),
                            0
                    );
                }


                setAlarm(calendar.getTimeInMillis(), calendar);
            }


            private void setAlarm(long timeInMillis, Calendar c) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(MainActivity.this, AlarmAdapter.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }

                alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                        1000 * 60 * 1, pendingIntent);

                Toast.makeText(MainActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int ampm = c.get(Calendar.AM_PM);
                String day = "";
                if(ampm == Calendar.AM){
                    day = "AM";
                }else if(ampm == Calendar.PM){
                    day = "PM";
                }
                String timeText = "Alarm set for: ";
                timeText += hour +": " + minute + " " + day;
                tv_display.setText(timeText);

            }


        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(MainActivity.this, AlarmAdapter.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                alarmManager.cancel(pendingIntent);

                tv_display.setText("Alarm not set");

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 123) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}
