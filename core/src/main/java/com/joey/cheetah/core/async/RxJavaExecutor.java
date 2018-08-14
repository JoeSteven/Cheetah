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
        execute(task, null, 0L);
    }

    @Override
    public void execute(AsyncTask task, IAsyncCallback callback) {
        execute(task, callback, 0L);
    }

    @Override
    public void execute(final AsyncTask task, final IAsyncCallback callback, long delay) {
        mCompositeDisposable.add(Observable.timer(delay, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                task.run();
                                emitter.onNext("no result");
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (callback != null) {
                            callback.done();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callback != null) {
                            callback.error(throwable);
                        }
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
                        .flatMap(new Function<Long, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(Long aLong) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<T> emitter) {
                                        try {
                                            emitter.onNext(task.run());
                                            emitter.onComplete();
                                        } catch (Exception e) {
                                            emitter.onError(e);
                                        }
                                    }
                                });
                            }
                        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        if (callback != null) {
                            callback.done(t);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callback != null) {
                            callback.error(throwable);
                        }
                    }
                }));
    }

    @Override
    public void clear() {
        mCompositeDisposable.clear();
    }

}
