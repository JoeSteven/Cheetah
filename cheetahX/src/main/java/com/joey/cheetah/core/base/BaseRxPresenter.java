package com.joey.cheetah.core.base;

import com.joey.cheetah.async.RxJavaManager;
import com.joey.cheetah.core.undo.AsyncManager;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */
public class BaseRxPresenter<T extends BaseView,E extends BaseModel>  extends BasePresenter<T, E>{

    @Override
    protected AsyncManager getAsyncManager() {
        return new RxJavaManager();
    }

    protected void add(Subscription subscription) {
        if (mAsyncManager instanceof RxJavaManager) {
            ((RxJavaManager) mAsyncManager).add(subscription);
        }
    }

    protected void remove(Subscription subscription) {
        if (mAsyncManager instanceof RxJavaManager) {
            ((RxJavaManager) mAsyncManager).remove(subscription);
        }
    }

    protected void subscribe(String event, Action1<Object> action) {
        if (mAsyncManager instanceof RxJavaManager) {
            ((RxJavaManager) mAsyncManager).subscribe(event, action);
        }
    }

    protected void unSubscribe(String event) {
        if (mAsyncManager instanceof RxJavaManager) {
            ((RxJavaManager) mAsyncManager).unSubscribe(event);
        }
    }

    protected void unSubscribe(String event, Observable observable) {
        if (mAsyncManager instanceof RxJavaManager) {
            ((RxJavaManager) mAsyncManager).unSubscribe(event, observable);
        }
    }
}
