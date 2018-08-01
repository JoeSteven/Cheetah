package joey.cheetah.sample.fragmentsample;

import android.os.Bundle;

import cheetah.core.mvp.AbsPresenter;

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
public class DemoFragmentPresenter extends AbsPresenter<IDemoFragmentView>{
    private String textShow = "null";
    public DemoFragmentPresenter(IDemoFragmentView view) {
        super(view);
    }

    public void setShow(String text) {
        textShow = text;
        if (isValid()) mView.show(textShow);
    }

    @Override
    public void onSaveData(Bundle outState) {
        outState.putString("stored", textShow +"  been stored!");
    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {
        textShow = savedInstanceState.getString("stored");
        if (isValid()) mView.show(textShow);
    }
}
