package com.joey.cheetah.sample.java.scan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.mvp.auto.Presenter;
import com.joey.cheetah.sample.R;
import com.polidea.rxandroidble2.scan.ScanResult;

import butterknife.BindView;

public class BleScanActivity extends AbsActivity implements IBleScanView{

    @Presenter
    BleScanPresenter mPresenter;

    @BindView(R.id.rv_scan)
    RecyclerView rvScan;

    @Override
    protected int initLayout() {
        return R.layout.activity_ble_scan;
    }

    @Override
    protected void initView() {
        mPresenter.mark();
        findViewById(R.id.bt_open).setOnClickListener(v -> mPresenter.open());
        findViewById(R.id.bt_close).setOnClickListener(v -> mPresenter.close());
        findViewById(R.id.bt_scan).setOnClickListener(v -> mPresenter.scan());
        rvScan.setLayoutManager(new LinearLayoutManager(this));
        rvScan.setHasFixedSize(true);
        rvScan.setItemAnimator(null);
    }

    @Override
    protected void onDestroy() {
        mPresenter.restore();
        super.onDestroy();
    }

    @Override
    public void refresh(ScanResult result) {

    }
}
