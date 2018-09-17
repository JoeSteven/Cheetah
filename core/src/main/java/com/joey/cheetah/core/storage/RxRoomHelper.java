package com.joey.cheetah.core.storage;

import io.reactivex.Single;

/**
 * Description:
 * author:Joey
 * date:2018/9/13
 */
public class RxRoomHelper {
    public interface RxRoomOperator<T>{
        T execute();
    }

    public static <T> Single<T> toSingle(RxRoomOperator<T> operator) {
        return Single.create(emitter -> {
            try {
                T result = operator.execute();
                if (result != null) {
                    emitter.onSuccess(result);
                } else {
                    throw new Exception("result is null !");
                }
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });

    }
}
