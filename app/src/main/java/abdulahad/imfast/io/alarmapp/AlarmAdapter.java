package abdulahad.imfast.io.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;


public class AlarmAdapter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        CameraManager cm = (CameraManager) context.getSystemService((Context.CAMERA_SERVICE));
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            boolean light;
            String str = "1010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010";
            for (int i = 0; i <= str.length() - 1; i++) {
                if (str.charAt(i) == '0') {
                    light = false;
                } else {
                    light = true;
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        cm.setTorchMode(cm.getCameraIdList()[0], light);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
