package com.joey.cheetah.helper;

import android.media.AudioManager;
import android.media.SoundPool;


import java.util.HashMap;
import java.util.Map;

/**
 * description - 声音帮助类
 * <p/>
 * author - Joe.
 * create on 16/8/2.
 * change
 * change on .
 */


public class SoundHelper {
//    private static SoundHelper mInstance ;
//    private final SoundPool mPool;
//    private Map<Integer,Integer> mSounds;
//
//    public static final int SOUND_ERROR = 1;
//    public static final int SOUND_SUCCESS = 2;
//    private SoundHelper(){
//        mPool = new SoundPool(5, AudioManager.STREAM_NOTIFICATION,0);
//        mSounds = new HashMap<>();
//    }
//
//    public static synchronized SoundHelper getInstance(){
//        if(mInstance==null) mInstance = new SoundHelper();
//        return mInstance;
//    }
//
//    public void play(int key){
//        // 如果没有加载,先加载
//        if(mSounds.get(key)==null)load(key);
//        final Integer id = mSounds.get(key);
//        if(id == -1){
//            ELog.w("SoundHelper","unLoad sounds!");
//            return;
//        }
//        int i = mPool.play(id,1,1,1,0,1);
//        // 如果失败可能是还未加载完全
//        if(i == 0){
//            mPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                @Override
//                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                    if(sampleId == id){
//                        mPool.play(id,1,1,1,0,1);
//                    }
//                }
//            });
//        }
//    }
//
//    private int load(int key) {
//        switch (key){
//            case SOUND_ERROR:
//                return load(SOUND_ERROR, R.raw.error);
//            case SOUND_SUCCESS:
//                return load(SOUND_SUCCESS,R.raw.success);
//            default:
//                mSounds.put(key,0);
//                return -1;
//        }
//    }
//
//    public int load(int key,int rawId){
//        int id = mPool.load(Erp.getContext(), rawId,1);
//        mSounds.put(key,id);
//        return id;
//    }
}
