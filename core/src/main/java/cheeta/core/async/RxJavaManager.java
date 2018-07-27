package cheeta.core.async;


import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
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
public class RxJavaManager {

    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable mCompositeSubscription;
    private ConcurrentHashMap<Class, Disposable> subscriptionMap;
    private RxBus mRxBus = RxBus.getInstance();

    public RxJavaManager() {
        mCompositeDisposable = new CompositeDisposable();
        mCompositeSubscription = new CompositeDisposable();
    }

    /**
     * when you start to subscribe an observable, add Disposable
     * @param disposable
     */
    public void add(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    /**
     * stop all subscription, avoid memory leak
     */
    public void clear() {
        mCompositeDisposable.clear();
        mCompositeSubscription.clear();
        subscriptionMap.clear();
    }

    /**
     * post event
     * @param event event instance
     */
    public <T> void post(T event) {
        mRxBus.post(event);
    }

    /**
     * register to receive an event, like EventBus, this is RxBus
     * poster will post event in io thread!
     * receiver will receive event in main thread
     * @param event your event, like EventBus
     * @param consumer callback, pass event to subscriber
     */
    public <T> void subscribe(Class<T> event, Consumer<T> consumer) {
        subscribeCustomThread(event, consumer, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    /**
     * poster will post event in its own thread
     * receiver will receive this event in poster's thread !!
     * @param event
     * @param consumer
     * @param <T>
     */
    public <T> void subscribeOnPostThread(Class<T> event, Consumer<T> consumer) {
        subscribeCustomThread(event, consumer, null, null);
    }

    /**
     * invoke this method to custom the thread of post event and receive event
     */
    public <T> void subscribeCustomThread(Class<T> event, Consumer<T> consumer,  Scheduler postScheduler,
                              Scheduler subscribeScheduler) {
        Flowable<T> observable = mRxBus.subscribe(event);
        if (postScheduler != null) {
            observable = observable.subscribeOn(postScheduler);
        }
        if (subscribeScheduler != null) {
            observable = observable.observeOn(subscribeScheduler);
        }
        Disposable disposable =observable.subscribe(consumer);
        mCompositeSubscription.add(disposable);
        if (subscriptionMap == null) {
            subscriptionMap = new ConcurrentHashMap<>();
        }
        subscriptionMap.put(event, disposable);
    }

    /**
     * unsubscribe an event
     * @param event event class
     */
    public <T> void unsubscribe(Class<T> event) {
        if (subscriptionMap != null && subscriptionMap.containsKey(event)) {
            mCompositeSubscription.remove(subscriptionMap.get(event));
        }
    }
}
