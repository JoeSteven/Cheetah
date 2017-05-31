package com.joey.cheetah.helper.subscriber;

import rx.Subscriber;

/**
 * description - 简单封装的Subscriber
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public abstract class NSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public abstract void onNext(T t) ;
}
