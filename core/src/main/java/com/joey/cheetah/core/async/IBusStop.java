package com.joey.cheetah.core.async;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

/**
 * Description: An interface to register event or post event
 * author:Joey
 * date:2018/8/1
 */
public interface IBusStop {
    /**
     * post event
     *
     * @param event event instance
     */
    <T> void post(T event);

    /**
     * register to receive an event, like EventBus, this is RxBus
     * poster will post event in io thread!
     * receiver will receive event in main thread
     *
     * @param event    your event, like EventBus
     * @param consumer callback, pass event to subscriber
     */
    <T> void subscribe(Class<T> event, Consumer<T> consumer);

    /**
     * poster will post event in its own thread
     * receiver will receive this event in poster's thread !!
     *
     * @param event
     * @param consumer
     * @param <T>
     */
    <T> void subscribeOnPostThread(Class<T> event, Consumer<T> consumer);

    /**
     * invoke this method to custom the thread of post event and receive event
     */
    <T> void subscribeCustomThread(Class<T> event, Consumer<T> consumer, Scheduler postScheduler,
                                   Scheduler subscribeScheduler);

    /**
     * unsubscribe an event
     *
     * @param event event class
     */
    <T> void unsubscribe(Class<T> event);

    /**
     * remove all subscription
     */
    void clear();
}
