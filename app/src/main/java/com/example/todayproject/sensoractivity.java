package com.example.todayproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class sensoractivity extends Service implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor accelSensor, gyroSensor;
    private Vibrator vib;
    float maccel, maccelCurrent, maccelLast;
    float roll,pitch,yaw;
    long mtime;
    int ok=0;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        ok=0;
        //현재 가속도
        maccel=0.00f;
        maccelCurrent=SensorManager.GRAVITY_EARTH;
        maccelLast=SensorManager.GRAVITY_EARTH;
        mtime=System.currentTimeMillis(); //현재 시간
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabTime = (currentTime - mtime); //시간 차이

            //stopwatch 클래스의 타이머가 작동하고 시간 차이가 2 이상 일때 (모션 감지)
            if (stopwatch.flag &&gabTime>2) {
                mtime = currentTime; //현재시간


                //모션 감지 코드
                roll = event.values[0];
                pitch = event.values[1];
                yaw = event.values[2];

                maccelLast=maccelCurrent;
                maccelCurrent=(float)Math.sqrt(roll*roll+pitch*pitch+yaw*yaw);
                float delta=maccelCurrent-maccelLast;
                maccel=maccel*0.9f+delta;

                //Log.i("변화량: ", String.valueOf(maccel));

                if (maccel > 3 && ok < 3) { //계속 알람 울리는 거 방지
                    ok+=1;
                    NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    NotificationChannel channel;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        channel=new NotificationChannel("10001","타이머",NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(sensoractivity.this,"10001")
                                .setContentText("시간")
                                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                                .setContentText("똑딱똑딱...!!.")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setAutoCancel(true)
                                .setShowWhen(true)
                                .setVibrate(new long[]{1000,2000})
                                .setDefaults(Notification.DEFAULT_VIBRATE);

                    manager.notify(0,builder.build());

                    Toast.makeText(sensoractivity .this.

                        getApplicationContext(), "똑딱똑딱..!!",Toast.LENGTH_SHORT).

                        show();

                      vib =(Vibrator) getSystemService(VIBRATOR_SERVICE);
                      vib.vibrate(1000);

                }

            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent,flags,startId);
        sensorManager.registerListener(this, accelSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroSensor,SensorManager.SENSOR_DELAY_NORMAL);

        maccel=0.00f;
        maccelCurrent=SensorManager.GRAVITY_EARTH;
        maccelLast=SensorManager.GRAVITY_EARTH;
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        vib.cancel();
        if(sensorManager!=null)
        sensorManager.unregisterListener(this);

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
