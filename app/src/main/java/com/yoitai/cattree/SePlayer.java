package com.yoitai.cattree;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

public class SePlayer {
    private SoundPool sePlayer;
    private SparseArray<Integer> seMap = new SparseArray<>();
    private float volumeRate = 1;
    private int[] cat = {R.raw.cat_cry1, R.raw.cat_cry2};

    SePlayer() {
        sePlayer = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
    }

    void initialize(Activity activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        volumeRate = (float) currentVolume / maxVolume;
    }

    void load(Activity activity, int se) {
        seMap.put(se, sePlayer.load(activity, se, 1));
    }

    void play() {
        play(cat[(int) (Math.random() * cat.length)]);
    }

    void play(int se) {
        sePlayer.play(seMap.get(se), volumeRate, volumeRate, 0, 0, 1.0f);
    }
}
