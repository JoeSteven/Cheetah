package com.joey.cheetah.sample.java.scan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.joey.cheetah.core.list.CheetahAdapter;
import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.mvp.auto.Presenter;
import com.joey.cheetah.sample.R;
import com.joey.cheetah.sample.constant.Constant;
import com.joey.cheetah.sample.java.connect.BleConnectActivity;
import com.joey.cheetah.sample.java.scan.adapter.BleScanDiffCallback;
import com.joey.cheetah.sample.java.scan.adapter.ScanViewBinder;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.List;

import butterknife.BindView;

public class BleScanActivity extends AbsActivity implements IBleScanView{

    @Presenter
    BleScanPresenter mPresenter;

    @BindView(R.id.rv_scan)
    RecyclerView rvScan;

    @BindView(R.id.bt_scan)
    Button btScan;

    private CheetahAdapter scanAdapter;

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
        findViewById(R.id.bt_quit).setOnClickListener(v -> finish());

        scanAdapter = new CheetahAdapter();
        scanAdapter.enableDiff(new BleScanDiffCallback());
        scanAdapter.register(ScanResult.class, new ScanViewBinder().setOnClickListener((position, data) -> connect(data)));
        rvScan.setLayoutManager(new LinearLayoutManager(this));
        rvScan.setHasFixedSize(true);
        rvScan.setItemAnimator(null);
        rvScan.setAdapter(scanAdapter);
    }

    private void connect(ScanResult result) {
        Jumper.make(this, BleConnectActivity.class)
                .putString(Constant.INTENT_BLE_DEVICE, result.getBleDevice().getMacAddress())
                .jump();
    }

    @Override
    protected void onDestroy() {
        mPresenter.restore();
        super.onDestroy();
    }

    @Override
    public void refresh(List<ScanResult> results) {
        scanAdapter.setItems(results);
        scanAdapter.notifyDataSetChanged();
    }

    @Override
    public void showScan() {
        btScan.setText("Start Scan");
    }

    @Override
    public void showStopScan() {
        btScan.setText("Stop Scan");
    }
}
