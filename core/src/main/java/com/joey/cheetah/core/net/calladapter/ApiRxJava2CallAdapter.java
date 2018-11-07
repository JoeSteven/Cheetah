package com.joey.cheetah.core.net.calladapter;

import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

/**
 * Description:
 * author:Joey
 * date:2018/10/23
 */
public class ApiRxJava2CallAdapter<R> implements CallAdapter<R, Object> {
    private final Type responseType;
    private final @Nullable
    Scheduler scheduler;
    private final boolean isAsync;
    private final boolean isResult;
    private final boolean isBody;
    private final boolean isFlowable;
    private final boolean isSingle;
    private final boolean isMaybe;
    private final boolean isCompletable;
    private final Consumer<Throwable> errorHandler;

    ApiRxJava2CallAdapter(Type responseType, @Nullable Scheduler scheduler, boolean isAsync,
                          boolean isResult, boolean isBody, boolean isFlowable, boolean isSingle, boolean isMaybe,
                          boolean isCompletable, Consumer<Throwable> errorHandler) {
        this.responseType = responseType;
        this.scheduler = scheduler;
        this.isAsync = isAsync;
        this.isResult = isResult;
        this.isBody = isBody;
        this.isFlowable = isFlowable;
        this.isSingle = isSingle;
        this.isMaybe = isMaybe;
        this.isCompletable = isCompletable;
        this.errorHandler = errorHandler;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        Observable<Response<R>> responseObservable = isAsync
                ? new CallEnqueueObservable<>(call)
                : new CallExecuteObservable<>(call);

        Observable<?> observable;
        if (isResult) {
            observable = new ResultObservable<>(responseObservable);
        } else if (isBody) {
            observable = new BodyObservable<>(responseObservable);
        } else {
            observable = responseObservable;
        }

        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }

        if (isFlowable) {
            return errorHandler != null ? observable.toFlowable(BackpressureStrategy.LATEST).doOnError(errorHandler)
                    :observable.toFlowable(BackpressureStrategy.LATEST);
        }
        if (isSingle) {
            return errorHandler != null ? observable.singleOrError().doOnError(errorHandler):
                    observable.singleOrError();
        }
        if (isMaybe) {
            return errorHandler != null ? observable.singleElement().doOnError(errorHandler):observable.singleElement();
        }
        if (isCompletable) {
            return errorHandler != null ? observable.ignoreElements().doOnError(errorHandler):observable.ignoreElements();
        }

        return errorHandler != null ? observable.doOnError(errorHandler):observable;

    }
}

