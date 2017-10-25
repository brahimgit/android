package bsa.fr.flashlight_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private boolean flash;
    private ImageButton button;
    private Vibrator vibrator;
    private ConstraintLayout layout;
    private CameraManager cameraManager;
    private Camera cam;
    private Camera.Parameters p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("2631FC5DF872A585F19CDFF4790E33C0")  // An example device ID
        .build();
        mAdView.loadAd(adRequest);
        button = (ImageButton) findViewById(R.id.imageButton);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        layout = (ConstraintLayout) findViewById(R.id.LinearLayout1);
        if (Build.VERSION.SDK_INT >= 23) {
            cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        } else {
            cam = Camera.open();
            p = cam.getParameters();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    try {
                        if (flash) {
                            button.setImageResource(R.mipmap.off_btn_rouge_metal);
                            cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                            vibrator.vibrate(50);
                            layout.setBackgroundResource(R.mipmap.background_black);
                            flash = false;
                        } else {
                            button.setImageResource(R.mipmap.on_btn_vert_metal);
                            layout.setBackgroundResource(R.mipmap.background_on);
                            CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                            cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                            vibrator.vibrate(50);
                            flash = true;
                        }
                    } catch (Exception e) {

                    }
                } else {
                    if (flash) {
                        button.setImageResource(R.mipmap.off_btn_rouge_metal);
                        layout.setBackgroundResource(R.mipmap.background_black);
                        vibrator.vibrate(100);
                        flash = false;
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        cam.setParameters(p);
                        cam.stopPreview();
                    } else {
                        flash = true;
                        button.setImageResource(R.mipmap.on_btn_vert_metal);
                        layout.setBackgroundResource(R.mipmap.background_on);
                        vibrator.vibrate(100);
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        cam.startPreview();
                    }
                }
            }
        });
    }

    private void checkDevice() {

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            new AlertDialog.Builder(this).setTitle(Build.VERSION.SDK_INT)
                    .setMessage("Your device do not have Camera, Click Ok to quit application")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    }).create().show();
        }

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            new AlertDialog.Builder(this).setTitle(Build.VERSION.SDK_INT)
                    .setMessage("Your device do not have flash, Click Ok to quit application")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    }).create().show();
        }
    }
}
