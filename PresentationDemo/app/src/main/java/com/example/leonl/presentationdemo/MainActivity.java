package com.example.leonl.presentationdemo;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Visualizer.OnDataCaptureListener {
    private static final int REQUEST_PERMISSION = 101;
    private MediaPlayer mediaPlayer;
    private Visualizer visualizer;
    private WhimRenderer whimRenderer;
    private WhimSurface whimSurface;
    private TextView fftShow = (TextView) findViewById(R.id.fft);
    private TextView waveformShow = (TextView) findViewById(R.id.waveform);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Audio Record Permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "RECORD_AUDIO permission is required.", Toast.LENGTH_SHORT).show();

            }

            // If no permission then request it to the user
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_PERMISSION);
            }

        } else {
            init();
        }
    }

    /**
     * When the user clicks on the accept permission
     *
     * @param requestCode - Audio Record Permission Code
     * @param permissions
     * @param grantResults - Which permission is granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If Permission is granted then start the initialization
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
            }
        }
    }

    /**
     * When Visualizer captures a data (WaveForm version)
     *
     * @param visualizer - Visualizer with the audio session Id
     * @param waveform  - byte array that has the waveform data
     * @param samplingRate - rate of the sampling
     */
    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        char number = (char)(waveform.length + '0');
        waveformShow.setText(number);
    }

    /**
     * When Visualizer captures a data (WaveForm version)
     *
     * @param visualizer - Visualizer with the audio session Id
     * @param fft - byte array that has the fft data
     * @param samplingRate - rate of the sampling
     */
    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        char number = (char)(fft.length + '0');
        fftShow.setText(number);
    }

    /**
     * Initialize the procedure
     */
    public void init(){
        int audioSampleSize = Visualizer.getCaptureSizeRange()[1];

        if(audioSampleSize > 512){
            audioSampleSize = 512;
        }

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        whimSurface = new WhimSurface(this);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            whimSurface.setEGLContextClientVersion(2);

            whimRenderer = new WhimRenderer();
            // Set the renderer to our demo renderer, defined below.
            whimSurface.setRenderer(whimRenderer, displayMetrics.density);
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.ritual);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(audioSampleSize);
        visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, true);
        visualizer.setEnabled(true);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        whimSurface.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        whimSurface.onPause();
    }
}
