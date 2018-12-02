package com.example.leonl.presentationdemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class WhimSurface extends GLSurfaceView {
    private static final int DRAW_INTERVAL = 1000 / 30;
    private static final float DBM_INTERP = 0.75f;

    private float myDensity;
    private WhimRenderer myWhimRenderer;
    private FftSetup fftSetup;
    private WaveformSetup waveformSetup;
    private float[] dbmArray;

    public WhimSurface(Context context) {
        super(context);
    }

    public WhimSurface(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setRenderer(WhimRenderer myRenderer, float density){
        this.myWhimRenderer = myRenderer;
        this.myDensity = density;

        super.setRenderer(myRenderer);
    }

    public void updateWaveFormFrame(WaveformSetup frame) {
        this.waveformSetup = frame;
    }

    public void updateFFTFrame(FftSetup frame) {
        this.fftSetup = frame;
    }

    private void fillFFT(byte[] buf, int offset, int len) {
        if (fftSetup == null) return;
        if (offset + len > buf.length) return;

        float[] dBmArray = new float[len];
        fftSetup.calculate(len, dBmArray);

        float maxDbm = Float.MIN_VALUE;
        float minDbm = Float.MAX_VALUE;
        for (int i = 0; i < len; i++) {
            float dbm = dBmArray[i];
            if (dbm < 0f) dbm = 0f;
            if (dbm > 1f) dbm = 1f;

            if (dbmArray != null) {
                float oldDbm = dbmArray[i];
                if (oldDbm - dbm > 0.025f) {
                    dbm = oldDbm - 0.025f;
                } else {
                    dbm = dbmArray[i] * DBM_INTERP + dbm * (1f - DBM_INTERP);
                }
            }

            if (dbm > maxDbm) maxDbm = dbm;
            if (dbm < minDbm) minDbm = dbm;

            dBmArray[i] = dbm;
            buf[offset + i] = (byte) (dbm * 255);
        }

        dbmArray = dBmArray;
    }
}
