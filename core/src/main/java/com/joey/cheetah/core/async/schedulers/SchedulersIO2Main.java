package com.joey.cheetah.core.async.schedulers;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * author:Joey
 * date:2018/9/13
 */
public class SchedulersIO2Main<T> implements ObservableTransformer<T,
        T>, SingleTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>{

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return  upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return  upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return  upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return  upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
