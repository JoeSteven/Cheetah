package cheeta.core.async;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description: manage RxJava Dispose And RxBus, avoid memory leak
 * author:Joey
 * date:2018/7/26
 */
public class RxJavaManager {

    private CompositeDisposable mCompositeDisposable;

    public RxJavaManager() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public void add(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }


    public void clear() {
        mCompositeDisposable.clear();
    }
}
