package com.joey.backup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


/**
 * description - 封装action，捕获回车
 * <p/>
 * author - Joe.
 * create on 16/7/16.
 * change
 * change on .
 */
public class ErpEditText extends EditText implements TextView.OnEditorActionListener{

    public ErpEditText(Context context) {
        super(context);
        setOnEditorActionListener(this);
    }

    public ErpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnEditorActionListener(this);
    }

    public ErpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, final KeyEvent event) {
        if(getImeOptions() == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getApplicationWindowToken(),0);
        }
        if(event!=null&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                &&event.getAction()== KeyEvent.ACTION_UP){
            if(mOnEnterListener!=null) mOnEnterListener.enter(this.getText().toString());
            return false;
        }
        return true;
    }
    private OnEnterListener mOnEnterListener;

    public void setOnEnterListener(OnEnterListener mOnEnterListener){
        this.mOnEnterListener = mOnEnterListener;
    }
    public interface OnEnterListener{
        void enter(String text);
    }
}
