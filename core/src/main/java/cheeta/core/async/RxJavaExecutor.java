package cheeta.core.async;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: A implement for IAsyncExecutor
 * author:Joey
 * date:2018/7/26
 */
public class RxJavaExecutor implements IAsyncExecutor {
    private RxJavaManager mManager;

    public RxJavaExecutor() {
        mManager = new RxJavaManager();
    }

    @Override
    public void execute(AsyncTask task) {
        execute(task, null);
    }


    @Override
    public void execute(final AsyncTask task, IAsyncCallback callback) {
        mManager.add(Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                task.run();
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe());
    }


    @Override
    public <T> void execute(final AsyncResultTask<T> task, final IAsyncResultCallback<T> callback) {
        mManager.add(Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) {
                try {
                    emitter.onNext(task.run());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
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
        mManager.clear();
    }

}
