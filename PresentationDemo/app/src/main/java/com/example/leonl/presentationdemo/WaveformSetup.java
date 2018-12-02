package com.example.leonl.presentationdemo;

public class WaveformSetup {
    private byte[] mRawWaveForm;

    public WaveformSetup(byte[] waveform, int offset, int len) {
        if (offset + len > waveform.length) throw new RuntimeException("Illegal offset and len");

        mRawWaveForm = new byte[len];
        System.arraycopy(waveform, offset, mRawWaveForm, 0, len);
    }

    public byte[] getRawWaveForm() {
        return mRawWaveForm;
    }
}
