package com.joey.cheetah.core.undo;

/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */

public class Call<T> {
    private T result;
    private Exception error;
    public void setResult(T result, Exception error) {
        this.result = result;
        this.error = error;
    }
    public void call(Callback<T> callback){
        if (error != null) {
            callback.onError(error);
        } else {
            callback.onCall(result);
        }
    }
}
