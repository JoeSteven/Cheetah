package com.joey.cheetah.core.async;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: A implement for IAsyncExecutor
 * author:Joey
 * date:2018/7/26
 */
public class RxJavaExecutor implements IAsyncExecutor {
    private CompositeDisposable mCompositeDisposable;

    public RxJavaExecutor() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void execute(AsyncTask task) {
        execute(task, null);
    }

    @Override
    public void execute(AsyncTask task, long delay) {
        execute(task, null, delay);
    }

    @Override
    public void execute(AsyncTask task, IAsyncCallback callback) {
        execute(task, callback, 0L);
    }

    @Override
    public void execute(final AsyncTask task, final IAsyncCallback callback, long delay) {
        mCompositeDisposable.add(Observable.timer(delay, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Object>>) aLong -> Observable.create(emitter -> {
                    task.run();
                    emitter.onNext("no result");
                    emitter.onComplete();
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (callback != null) {
                        callback.done();
                    }
                }, throwable -> {
                    if (callback != null) {
                        callback.error(throwable);
                    }
                }));
    }

    @Override
    public <T> void execute(final AsyncResultTask<T> task, final IAsyncResultCallback<T> callback) {
        execute(task, callback, 0L);
    }

    @Override
    public <T> void execute(final AsyncResultTask<T> task, final IAsyncResultCallback<T> callback, long delay) {
        mCompositeDisposable.add(Observable.timer(delay, TimeUnit.MILLISECONDS)
                        .flatMap((Function<Long, ObservableSource<T>>) aLong -> Observable.create(emitter -> {
                            try {
                                emitter.onNext(task.run());
                                emitter.onComplete();
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        })).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    if (callback != null) {
                        callback.done(t);
                    }
                }, throwable -> {
                    if (callback != null) {
                        callback.error(throwable);
                    }
                }));
    }

    @Override
    public void clear() {
        mCompositeDisposable.clear();
    }

}
