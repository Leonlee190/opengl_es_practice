package com.example.leonl.presentationdemo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class AudioAnalyze {
    private int samplingSize;
    private byte[] fftAudioData;
    private byte[] waveAudioData;

    public void AudioAnalyze(){
        this.fftAudioData = null;
        this.waveAudioData = null;
    }

    public void AudioAnalyze(int audioSize){
        this.fftAudioData = null;
        this.waveAudioData = null;
        this.samplingSize = audioSize;
    }

    public void setFftAudioData(byte[] fft){
        System.arraycopy(fft, 0, fftAudioData, 0, fft.length);
    }

    public void setWaveAudioData(byte[] waveform){
        System.arraycopy(waveform, 0, waveAudioData, 0, waveform.length);
    }

    public void fftAnalysis(byte[] fft){

    }

    public void waveformAnalysis(byte[] waveform){

    }

    public FloatBuffer getFftResult(){
        FloatBuffer floatBuffer = ByteBuffer.wrap(fftAudioData).order(ByteOrder.nativeOrder()).asFloatBuffer();

        return floatBuffer;
    }

    public FloatBuffer getWaveformResult(){
        FloatBuffer floatBuffer = ByteBuffer.wrap(waveAudioData).order(ByteOrder.nativeOrder()).asFloatBuffer();

        return floatBuffer;
    }
}
