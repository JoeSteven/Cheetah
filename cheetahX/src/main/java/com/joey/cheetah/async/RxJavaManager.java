package com.joey.cheetah.async;

import com.joey.cheetah.undo.AsyncManager;
import com.joey.cheetah.undo.Call;
import com.joey.cheetah.undo.CallbackRunnable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 管理异步任务及事件，及时remove异步任务
 * Created by Joe on 16/7/12.
 */
public class RxJavaManager implements AsyncManager{

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者
    public RxBus mRxBus = RxBus.getInstance();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察者

    /**
     * 将一次观察加入订阅组
     */
    public void add(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }


    public void remove(Subscription subscription) {
        mCompositeSubscription.remove(subscription);
    }
    /**
     * 订阅事件--RxBus
     */
    public Observable subscribe(String eventName, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.subscribe(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }));
        return mObservable;
    }

    /**
     * 取消订阅--RxBus
     */
    public void unSubscribe(String event, Observable<?> observable) {
        mRxBus.unSubscribe(event, observable);
    }

    /**
     * 取消订阅--RxBus
     */
    public void unSubscribe(String event) {
        mRxBus.unSubscribe(event);
    }

    /**
     * 发布事件--RxBus
     */
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

    public <T> Call<T> async(final CallbackRunnable<T> runnable) {
        final Call<T> call = new Call();
        add(Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(runnable.run());
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        call.setResult(null, new Exception(e));
                    }

                    @Override
                    public void onNext(T t) {
                        call.setResult(t, null);
                    }
                }));
        return call;
    }

    @Override
    public void async(final Runnable runnable) {
        add(Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                runnable.run();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    /**移除订阅组中的所有订阅*/
    @Override
    public void onDestroy() {
        mCompositeSubscription.clear();
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unSubscribe(entry.getKey(), entry.getValue());// 移除观察
        }
    }
}
