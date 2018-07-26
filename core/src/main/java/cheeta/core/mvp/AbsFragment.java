package cheeta.core.mvp;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cheeta.core.ui.CToast;
import cheeta.core.utils.ResGetter;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsFragment extends Fragment implements IView {
    protected static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected View mRootView;
    private AbsPresenter absPresenter;
    protected Activity mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resource = initLayout();
        mRootView = inflater.inflate(resource, container, false);
        initArguments(getArguments());
        initView();
        absPresenter = initPresenter();
        registerPresenter(absPresenter);
        if (savedInstanceState != null) {
            absPresenter.onRestoredData(savedInstanceState);
        } else {
            initData();
        }
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRestoreFragmentInstance(savedInstanceState);
    }

    private void onRestoreFragmentInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        absPresenter.onSaveData(outState);
        super.onSaveInstanceState(outState);
    }

    protected abstract void initArguments(Bundle arguments);

    protected abstract void initView();

    protected abstract <T extends AbsPresenter> T initPresenter();

    protected abstract void initData();

    protected abstract int initLayout();

    protected void registerPresenter(AbsPresenter presenter) {
        if (presenter != null) {
            getLifecycle().addObserver(presenter);
        }
    }

    public <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    protected boolean isValid() {
        if (mActivity == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(mActivity.isDestroyed() || mActivity.isFinishing());
        }
        return !mActivity.isFinishing();
    }

    @Override
    public void toast(String msg) {
        CToast.show(msg);
    }

    @Override
    public void toast(int stringRes) {
        CToast.show(ResGetter.string(stringRes));
    }
}
