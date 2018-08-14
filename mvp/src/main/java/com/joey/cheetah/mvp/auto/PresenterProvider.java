package com.joey.cheetah.mvp.auto;

import android.os.Bundle;

import com.joey.cheetah.core.mvp.AbsPresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class PresenterProvider implements IPresenterProvider {
    private List<AbsPresenter> presenters;

    @Override
    public void create(Object host) {
        try {
            for (Field f : host.getClass().getDeclaredFields()) {
                if (f.getAnnotation(Presenter.class) != null) {
                    f.setAccessible(true);
                    Class<? extends AbsPresenter> p = (Class<? extends AbsPresenter>) f.getType();
                    ParameterizedType type = (ParameterizedType) p.getGenericSuperclass();
                    Constructor constructor = p.getConstructor((Class<?>) type.getActualTypeArguments()[0]);
                    f.set(host, constructor.newInstance(host));
                    if (presenters == null) {
                        presenters = new ArrayList<>();
                    }
                    presenters.add((AbsPresenter) f.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestore(Bundle data) {
        if (presenters == null || presenters.isEmpty()) return;
        for (AbsPresenter p : presenters) {
            p.onRestoredData(data);
        }

    }

    @Override
    public void onSave(Bundle data) {
        if (presenters != null && !presenters.isEmpty()) {
            for (AbsPresenter p : presenters) {
                p.onSaveData(data);
            }
        }
    }
}
