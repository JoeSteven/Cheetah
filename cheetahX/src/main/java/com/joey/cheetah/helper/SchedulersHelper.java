package com.joey.cheetah.helper;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * description - 线程转换帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class SchedulersHelper {
    /**
     * IO线程执行，主线程观察
     */
    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
