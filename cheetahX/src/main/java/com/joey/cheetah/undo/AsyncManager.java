package com.joey.cheetah.undo;

/**
 * Describe
 * Author Joe
 * created at 17/5/15.
 */

public interface AsyncManager {
    void async(Runnable runnable);
    void onDestroy();
}
