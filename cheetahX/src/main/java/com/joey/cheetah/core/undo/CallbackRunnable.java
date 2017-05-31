package com.joey.cheetah.core.undo;

/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */

public interface CallbackRunnable<T> {
    T run() throws Exception;
}
