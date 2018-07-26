package cheeta.core.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import cheeta.core.async.RxJavaManager;
import io.reactivex.disposables.Disposable;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsPresenter<T> implements LifecycleObserver {
    protected T mView;
    protected RxJavaManager mRxJavaManager;

    public AbsPresenter(T view) {
        mView = view;
        mRxJavaManager = new RxJavaManager();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroyed(@NonNull LifecycleOwner owner) {
        // destroy view to void memory leak
        mRxJavaManager.clear();
    }

    protected void add(Disposable disposable) {
        mRxJavaManager.add(disposable);
    }

    public abstract void onSaveData(Bundle outState);

    public abstract void onRestoredData(Bundle savedInstanceState);

    protected boolean isValid() {
        return mView != null;
    }
}
