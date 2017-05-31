package com.joey.backup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ujipin.erp.R;

/**
 * description - 两列的单元格
 * <p/>
 * author - Joe.
 * create on 16/7/16.
 * change
 * change on .
 */
public class TableView extends FrameLayout {

    private TextView mColumns1;
    private TextView mColumns2;

    public TableView(Context context) {
        super(context);
        initView();
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.TableView);

        setColumns1(typedArray.getString(R.styleable.TableView_columns_one));
        setColumns2(typedArray.getString(R.styleable.TableView_columns_two));
//        setColumns2(attrs.getAttributeValue(nameSpace,"columns_two"));
        String nameSpace = "http://schemas.android.com/apk/res-auto";
        setRatio(attrs.getAttributeFloatValue(nameSpace,"ratio",1));
        typedArray.recycle();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_table_row,this);
        mColumns1 = (TextView) findViewById(R.id.tv_columns_one);
        mColumns2 = (TextView) findViewById(R.id.tv_columns_two);
    }

    /**
     * 比列
     * @param ratio 第一行比第二行
     */
    public void setRatio(float ratio){
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mColumns1.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) mColumns2.getLayoutParams();
        if(ratio>=1){
            params1.weight = ratio;
            params2.weight = 1;
        }else{
            params1.weight = 1;
            params2.weight = 1/ratio;
        }
        mColumns1.setLayoutParams(params1);
        mColumns2.setLayoutParams(params2);
    }

    public void setColumns(CharSequence one, CharSequence two){
        setColumns1(one);
        setColumns2(two);
    }
    public void setColumns1(CharSequence text){
        mColumns1.setText(text);
    }
    public void setColumns2(CharSequence text){
        mColumns2.setText(text);
    }
}
