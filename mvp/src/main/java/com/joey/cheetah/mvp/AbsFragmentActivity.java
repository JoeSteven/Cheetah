package com.joey.cheetah.mvp;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Description: Activity that holding fragment must extends this class
 * author:Joey
 * date:2018/7/26
 */
public abstract class AbsFragmentActivity extends AbsActivity {

    private FragmentManager mFragmentManager;
    private SparseArray<Fragment> currentFragments = new SparseArray<>();

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
        attachFragment();
    }

    protected abstract void createFragment();

    protected abstract void restoreFragment(Bundle savedInstanceState);

    protected abstract void attachFragment();


    /**
     * add new fragment to attach activity
     */
    protected void addFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragments.get(contentId) != null) {
            transaction.hide(currentFragments.get(contentId));
        }
        if (targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        } else {
            transaction.add(contentId, targetFragment, tag).commit();
        }
        currentFragments.put(contentId, targetFragment);
    }


    /**
     * add new fragment to attach activity and add it to the back stack
     */
    protected void addFragmentToStack(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragments.get(contentId) != null) {
            transaction.hide(currentFragments.get(contentId));
        }
        transaction.add(contentId, targetFragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
        currentFragments.put(contentId, targetFragment);
    }

    /**
     * destroy current fragment, replace it to a new fragment
     */
    protected void replaceFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(contentId, targetFragment, tag);
        transaction.commit();
        currentFragments.put(contentId, targetFragment);
    }

    /**
     * switch fragment between added fragments
     */
    protected void switchFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        Fragment current = currentFragments.get(contentId);
        if (current == null || targetFragment == null || current == targetFragment) return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.hide(current).add(contentId, targetFragment, tag).commit();
        } else {
            transaction.hide(current).show(targetFragment).commit();
        }
        currentFragments.put(contentId, targetFragment);
    }

    protected FragmentManager fragmentManager() {
        return mFragmentManager;
    }

    protected Fragment currentFragment(@IdRes int contentId) {
        return currentFragments.get(contentId);
    }
}
