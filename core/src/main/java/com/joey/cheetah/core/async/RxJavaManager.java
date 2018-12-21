package com.joey.cheetah.core.async;


import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: manage RxJava Dispose And RxBus, avoid memory leak
 * author:Joey
 * date:2018/7/26
 */
public class RxJavaManager implements IAsyncExecutor, IBusStop {

    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable mCompositeSubscription;
    private ConcurrentHashMap<Class, Disposable> subscriptionMap;
    private RxBus mRxBus = RxBus.getInstance();
    private IAsyncExecutor mExecutor;

    public RxJavaManager() {
        mCompositeDisposable = new CompositeDisposable();
        mCompositeSubscription = new CompositeDisposable();
        mExecutor = AsyncManger.createNew();
    }

    /**
     * when you start to subscribe an observable, add Disposable
     *
     * @param disposable
     */
    public void add(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    /**
     * stop all subscription, avoid memory leak
     */
    @Override
    public void clear() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
        if (subscriptionMap != null) {
            subscriptionMap.clear();
        }
        mExecutor.clear();
    }

    /**
     * post event
     *
     * @param event event instance
     */
    public <T> void post(T event) {
        mRxBus.post(event);
    }

    /**
     * register to receive an event, like EventBus, this is RxBus
     * poster will post event in io thread!
     * receiver will receive event in main thread
     *
     * @param event    your event, like EventBus
     * @param consumer callback, pass event to subscriber
     */
    public <T> void subscribe(Class<T> event, Consumer<T> consumer) {
        subscribeCustomThread(event, consumer, AndroidSchedulers.mainThread());
    }

    /**
     * poster will post event in its own thread
     * receiver will receive this event in poster's thread !!
     *
     * @param event
     * @param consumer
     * @param <T>
     */
    public <T> void subscribeOnPostThread(Class<T> event, Consumer<T> consumer) {
        subscribeCustomThread(event, consumer, null);
    }

    /**
     * invoke this method to custom the thread of post event and receive event
     */
    public <T> void subscribeCustomThread(Class<T> event, Consumer<T> consumer, Scheduler subscribeScheduler) {
        if (subscriptionMap.containsKey(event)) {
            // 不重复注册事件
            return;
        }
        Flowable<T> observable = mRxBus.subscribe(event);
        if (subscribeScheduler != null) {
            observable = observable.observeOn(subscribeScheduler);
        }
        Disposable disposable = observable.subscribe(consumer);
        mCompositeSubscription.add(disposable);
        if (subscriptionMap == null) {
            subscriptionMap = new ConcurrentHashMap<>();
        }
        subscriptionMap.put(event, disposable);
    }

    /**
     * unsubscribe an event
     *
     * @param event event class
     */
    public <T> void unsubscribe(Class<T> event) {
        if (subscriptionMap != null && subscriptionMap.containsKey(event)) {
            mCompositeSubscription.remove(subscriptionMap.get(event));
        }
    }


    @Override
    public void execute(AsyncTask task) {
        mExecutor.execute(task, null);
    }

    @Override
    public void execute(AsyncTask task, long delay) {
        mExecutor.execute(task, delay);
    }


    @Override
    public void execute(final AsyncTask task, final IAsyncCallback callback) {
        mExecutor.execute(task, callback);
    }

    @Override
    public void execute(AsyncTask task, IAsyncCallback callback, long delay) {
        mExecutor.execute(task, callback, delay);
    }


    @Override
    public <T> void execute(final AsyncResultTask<T> task, final IAsyncResultCallback<T> callback) {
        mExecutor.execute(task, callback);
    }

    @Override
    public <T> void execute(AsyncResultTask<T> task, IAsyncResultCallback<T> callback, long delay) {
        mExecutor.execute(task, callback, delay);
    }
}
