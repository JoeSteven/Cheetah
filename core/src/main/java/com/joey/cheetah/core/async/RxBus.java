package com.joey.cheetah.core.async;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Description:
 * author:Joey
 * date:2018/7/27
 */
public class RxBus {
    private static RxBus instance;
    private ConcurrentHashMap<Class, Subject> subjectMap;

    public static synchronized RxBus getInstance() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
        subjectMap = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        if (event == null) return;
        if (subjectMap.containsKey(event.getClass())) {
            Subject<T> subject = subjectMap.get(event.getClass());
            subject.onNext(event);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Flowable<T> subscribe(Class<T> event) {
        Subject<T> subject;
        if (subjectMap.containsKey(event)) {
            subject = subjectMap.get(event);
        } else {
            subject = (Subject<T>) PublishSubject.create().toSerialized();
            subjectMap.put(event, subject);
        }
        return subject.toFlowable(BackpressureStrategy.BUFFER).ofType(event);
    }

    public <T> boolean hasObservers(Class<T> event) {
        return subjectMap.containsKey(event) && subjectMap.get(event).hasObservers();
    }

}

