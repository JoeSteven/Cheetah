package com.joey.cheetah.mvp;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * Description: Activity that holding fragment must extends this class
 * author:Joey
 * date:2018/7/26
 */
public abstract class AbsFragmentActivity extends AbsActivity {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        //避免被系统回收后发生重复创建
        if (savedInstanceState == null) {
            createFragment();
            attachFragment();
        } else {
            restoreFragment(savedInstanceState);
        }
        initFragment();
    }

    protected abstract void createFragment();

    protected abstract void restoreFragment(Bundle savedInstanceState);

    protected abstract void attachFragment();

    protected void initFragment() {

    }


    /**
     * add new fragment to attach activity
     */
    protected void addFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragment(contentId) != null) {
            transaction.hide(currentFragment(contentId));
        }
        if (targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        } else {
            transaction.add(contentId, targetFragment, tag).commit();
        }
    }


    /**
     * add new fragment to attach activity and add it to the back stack
     */
    protected void addFragmentToStack(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = fragmentManager().beginTransaction();
        Fragment current = currentFragment(contentId);
        if (current != null && current != targetFragment) {
            transaction.hide(current);
        }
        if (targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        } else {
            transaction.add(contentId, targetFragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * destroy current fragment, replace it to a new fragment
     */
    protected void replaceFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(contentId, targetFragment, tag);
        transaction.commit();
    }

    /**
     * switch fragment between added fragments
     */
    protected void switchFragment(Fragment targetFragment, @IdRes int contentId, String tag) {
        Fragment current = currentFragment(contentId);
        if (current == null) {
            addFragment(targetFragment, contentId, tag);
            return;
        }
        if (targetFragment == null || current == targetFragment) return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.hide(current).add(contentId, targetFragment, tag).commit();
        } else {
            transaction.hide(current).show(targetFragment).commit();
        }
    }

    protected FragmentManager fragmentManager() {
        return mFragmentManager;
    }

    protected Fragment currentFragment(@IdRes int contentId) {
        List<Fragment> fragments = fragmentManager().getFragments();
        for (Fragment it : fragments) {
            if (it.getId() == contentId && it.isAdded() && it.isVisible()) return it;
        }
        return null;
    }
}
