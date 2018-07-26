package cheeta.core.mvp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cheeta.core.R;
import cheeta.core.ui.CToast;


/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsActivity extends AppCompatActivity implements IView {
    private AbsPresenter absPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        Intent intent = getIntent();
        initArguments(intent);
        initView();
        absPresenter = initPresenter();
        registerPresenter(absPresenter);
        if (savedInstanceState != null) {
            absPresenter.onRestoredData(savedInstanceState);
            afterRestoredData();
        } else {
            initData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        absPresenter.onSaveData(outState);
        super.onSaveInstanceState(outState);
    }

    private void initArguments(Intent intent) {

    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract <T extends AbsPresenter> T initPresenter();

    protected abstract void initData();

    /**
     * if you want to do some logic after restored data
     */
    protected void afterRestoredData() {

    }

    /**
     * must call this method to register your presenter
     * otherwise view can't sync lifecycle to presenter
     */
    protected void registerPresenter(AbsPresenter presenter) {
        if (presenter != null) {
            getLifecycle().addObserver(presenter);
        }
    }

    @Override
    public void toast(int stringRes) {
        CToast.show(stringRes);
    }

    @Override
    public void toast(String msg) {
        CToast.show(msg);
    }

    public boolean isValid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(isDestroyed() || isFinishing());
        }
        return !isFinishing();
    }

    /**
     * quit this activity with twice back press
     */
    protected boolean isTwiceQuit() {
        return false;
    }

    private long lastTime = 0;

    @Override
    public void onBackPressed() {
        if (!isTwiceQuit()) {
            finish();
            return;
        }
        long duration = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (duration < 2000) {
            finish();
            return;
        }
        CToast.show(R.string.twice_quit_hint);
    }

}
