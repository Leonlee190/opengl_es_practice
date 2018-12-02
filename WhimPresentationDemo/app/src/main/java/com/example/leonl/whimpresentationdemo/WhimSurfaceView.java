package com.example.leonl.whimpresentationdemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class WhimSurfaceView extends GLSurfaceView {
    private static int captureSize;
    private static float density;
    private static byte[] fftCaptured;
    private static byte[] waveformCaptured;

    private WhimRenderer whimRenderer;

    public WhimSurfaceView(Context context) {
        super(context);
    }

    public WhimSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRenderer(WhimRenderer inputRenderer, float inputDensity, int captureSize){
        this.whimRenderer = inputRenderer;
        this.density = inputDensity;
        this.captureSize = captureSize;

        this.fftCaptured = new byte[captureSize];
        this.waveformCaptured = new byte[captureSize];

        super.setRenderer(inputRenderer);
    }

    public void updateFft(byte[] fft){
        System.arraycopy(fft, 0, fftCaptured, 0, captureSize);
    }

    public void updateWaveform(byte[] waveform){
        System.arraycopy(waveform, 0, waveformCaptured, 0, captureSize);
    }
}
