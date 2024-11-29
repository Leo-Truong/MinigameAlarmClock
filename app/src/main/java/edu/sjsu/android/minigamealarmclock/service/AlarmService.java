package edu.sjsu.android.minigamealarmclock.service;

import static edu.sjsu.android.minigamealarmclock.application.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.activity.RingActivity;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Vibrator vibrator;
    int maxVolume, originalVolume;
    Alarm alarm;
    Uri ringtone;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.getBaseContext(), RingtoneManager.TYPE_ALARM);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle!=null)
            alarm = (Alarm) bundle.getParcelable(getString(R.string.arg_alarm_obj));
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra(getString(R.string.bundle_alarm_obj),bundle);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        String alarmName = getString(R.string.alarm_name);
        if(alarm!=null) {
            alarmName = alarm.getAlarmName();
            try {
                mediaPlayer.setDataSource(this.getBaseContext(), Uri.parse(alarm.getAlarmSound()));
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{

            try {
                mediaPlayer.setDataSource(this.getBaseContext(),ringtone);
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Click to turn off: ")
                .setContentText(alarmName)
                .setSmallIcon(R.drawable.ic_alarm_white_24)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent,true)
                .build();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (alarm.isMaxVolume()){
                    // Saves original volume to reset after minigame is completed
                    originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    // Gets the max volume of the device
                    maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    // Sets the device volume to max
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
                    // Sets mediaPlayer volume to max
                    mediaPlayer.setVolume(1.0f, 1.0f);
                }
                mediaPlayer.start();
            }
        });

        if(alarm.isVibration()) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();

        // Only reset volume back to normal if it was changed originally
        if (alarm.isMaxVolume())
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, AudioManager.FLAG_SHOW_UI);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
