package com.joey.cheetah.core.net.calladapter;

import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

/**
 * Description:
 * author:Joey
 * date:2018/10/23
 */
public class ApiRxJava2CallAdapterFactory extends  CallAdapter.Factory{
        /**
         * Returns an instance which creates synchronous observables that do not operate on any scheduler
         * by default.
         */
        public static ApiRxJava2CallAdapterFactory create(Consumer<Throwable> errorHandler) {
            return new ApiRxJava2CallAdapterFactory(null, false, errorHandler);
        }

        /**
         * Returns an instance which creates asynchronous observables. Applying
         * {@link Observable#subscribeOn} has no effect on stream types created by this factory.
         */
        public static ApiRxJava2CallAdapterFactory createAsync(Consumer<Throwable> errorHandler) {
            return new ApiRxJava2CallAdapterFactory(null, true, errorHandler);
        }

        /**
         * Returns an instance which creates synchronous observables that
         * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
         */
        @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
        public static ApiRxJava2CallAdapterFactory createWithScheduler(Scheduler scheduler, Consumer<Throwable> errorHandler) {
            if (scheduler == null) throw new NullPointerException("scheduler == null");
            return new ApiRxJava2CallAdapterFactory(scheduler, false, errorHandler);
        }

        private final @Nullable
        Scheduler scheduler;
        private final boolean isAsync;
        private Consumer<Throwable> errorHandler;

        private ApiRxJava2CallAdapterFactory(@Nullable Scheduler scheduler, boolean isAsync, Consumer<Throwable> errorHandler) {
            this.scheduler = scheduler;
            this.isAsync = isAsync;
            this.errorHandler = errorHandler;
        }

        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            Class<?> rawType = getRawType(returnType);

            if (rawType == Completable.class) {
                // Completable is not parameterized (which is what the rest of this method deals with) so it
                // can only be created with a single configuration.
                return new ApiRxJava2CallAdapter(Void.class, scheduler, isAsync, false, true, false, false,
                        false, true, errorHandler);
            }

            boolean isFlowable = rawType == Flowable.class;
            boolean isSingle = rawType == Single.class;
            boolean isMaybe = rawType == Maybe.class;
            if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
                return null;
            }

            boolean isResult = false;
            boolean isBody = false;
            Type responseType;
            if (!(returnType instanceof ParameterizedType)) {
                String name = isFlowable ? "Flowable"
                        : isSingle ? "Single"
                        : isMaybe ? "Maybe" : "Observable";
                throw new IllegalStateException(name + " return type must be parameterized"
                        + " as " + name + "<Foo> or " + name + "<? extends Foo>");
            }

            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            Class<?> rawObservableType = getRawType(observableType);
            if (rawObservableType == Response.class) {
                if (!(observableType instanceof ParameterizedType)) {
                    throw new IllegalStateException("Response must be parameterized"
                            + " as Response<Foo> or Response<? extends Foo>");
                }
                responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            } else if (rawObservableType == Result.class) {
                if (!(observableType instanceof ParameterizedType)) {
                    throw new IllegalStateException("Result must be parameterized"
                            + " as Result<Foo> or Result<? extends Foo>");
                }
                responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
                isResult = true;
            } else {
                responseType = observableType;
                isBody = true;
            }

            return new ApiRxJava2CallAdapter(responseType, scheduler, isAsync, isResult, isBody, isFlowable,
                    isSingle, isMaybe, false, errorHandler);
        }
}
