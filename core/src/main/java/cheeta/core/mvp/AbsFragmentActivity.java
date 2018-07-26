package cheeta.core.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Description: Activity that holding fragment must extends this class
 * author:Joey
 * date:2018/7/26
 */
public abstract class AbsFragmentActivity extends AbsActivity {

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        //避免被系统回收后发生重复创建
        if (savedInstanceState == null) {
            createFragment();
        } else {
            restoreFragment(savedInstanceState);
        }
    }

    protected abstract void createFragment();

    protected abstract void restoreFragment(Bundle savedInstanceState);


    /**
     * add new fragment to attach activity
     */
    protected void addFragment(Fragment targetFragment, int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        transaction.add(contentId, targetFragment, tag);
        transaction.commit();
        mCurrentFragment = targetFragment;
    }


    /**
     * add new fragment to attach activity and add it to the back stack
     */
    protected void addFragmentToStack(Fragment targetFragment, int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        transaction.add(contentId, targetFragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
        mCurrentFragment = targetFragment;
    }

    /**
     * destroy current fragment, replace it to a new fragment
     */
    protected void replaceFragment(Fragment targetFragment, int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(contentId, targetFragment, tag);
        transaction.commit();
        mCurrentFragment = targetFragment;
    }

    /**
     * switch fragment between added fragments
     */
    protected void switchFragment(Fragment targetFragment, int id, String tag) {
        if (mCurrentFragment == null || targetFragment == null) return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.hide(mCurrentFragment).add(id, targetFragment, tag).commit();
        } else {
            transaction.hide(mCurrentFragment).show(targetFragment).commit();
        }
        mCurrentFragment = targetFragment;
    }

}
