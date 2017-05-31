package com.joey.cheetah.undo;

/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */

public interface Callback<T> {
    void onCall(T t);
    void onError(Exception e);
}
