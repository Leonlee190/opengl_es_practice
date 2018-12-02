package com.example.leonl.presentationdemo;

public class FftSetup {
    /**
     * Maximum value of dB. Used for controlling wave height percentage.
     */
    private static final float MAX_DB_VALUE = 76;

    private byte[] mRawFFT;
    private float[] mDbs;


    public FftSetup(byte[] fft, int offset, int len) {
        if (offset + len > fft.length) throw new RuntimeException("Illegal offset and len");

        mRawFFT = new byte[len];
        System.arraycopy(fft, offset, mRawFFT, 0, len);
    }

    /**
     * Calculate dBs and amplitudes
     */
    public void calculate(int arraySize, float[] dBmArray) {
        int dataSize = mRawFFT.length / 2 - 1;

        if (mDbs == null || mDbs.length != dataSize) {
            mDbs = new float[dataSize];
        }
        for (int i = 0; i < dataSize; i++) {
            float re = mRawFFT[2 * i];
            float im = mRawFFT[2 * i + 1];
            float sqMag = re * re + im * im;
            mDbs[i] = magnitude2Db(sqMag);
        }

        for (int i = 0; i < arraySize; i++) {
            int index = (int) (i * 1f * dataSize / arraySize);
            dBmArray[i] = mDbs[index] / MAX_DB_VALUE;
        }
    }

    private static float magnitude2Db(float squareMag) {
        if (squareMag == 0) return 0;
        return (float) (20 * Math.log10(squareMag));
    }

    // http://forum.processing.org/topic/super-fast-square-root
    private static float fastSqrt(float x) {
        return Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));
    }
}
