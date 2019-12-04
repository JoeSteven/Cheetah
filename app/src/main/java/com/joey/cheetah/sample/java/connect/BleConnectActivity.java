package com.joey.cheetah.sample.java.connect;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joey.cheetah.core.list.CheetahAdapter;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.mvp.auto.Presenter;
import com.joey.cheetah.sample.R;
import com.joey.cheetah.sample.constant.Constant;
import com.joey.cheetah.sample.java.connect.adapter.BleCharacteristicViewBinder;
import com.joey.cheetah.sample.java.connect.adapter.BleDescriptorViewBinder;
import com.joey.cheetah.sample.java.connect.adapter.BleServiceViewBinder;
import com.joey.rxble.RxBle;

import java.util.ArrayList;
import java.util.List;

public class BleConnectActivity extends AbsActivity implements IBleConnectView {

    @Presenter
    BleConnectPresenter mPresenter;

    Button btConnect;

    Button btNotify;

    TextView tvDevice;

    TextView tvMessage;

    RecyclerView rvDiscover;

    EditText etWrite;

    private String mDeviceAddress;
    private CheetahAdapter mAdapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_ble_connect;
    }

    @Override
    protected void initArguments(Intent intent) {
        mDeviceAddress = intent.getStringExtra(Constant.INTENT_BLE_DEVICE);
    }

    @Override
    protected void initView() {
        btConnect = findViewById(R.id.bt_connect);
        btNotify = findViewById(R.id.bt_notify);
        tvDevice = findViewById(R.id.tv_mac);
        tvMessage = findViewById(R.id.tv_read_content);
        rvDiscover = findViewById(R.id.rv_discover);
        etWrite = findViewById(R.id.et_write);

        btConnect.setOnClickListener(v -> mPresenter.connectOrDisconnect(mDeviceAddress));
        btNotify.setOnClickListener(v -> mPresenter.disableNotifyOrIndicate());
        tvDevice.setText(mDeviceAddress);
        mAdapter = new CheetahAdapter();
        mAdapter.register(BluetoothGattService.class, new BleServiceViewBinder());
        mAdapter.register(BluetoothGattCharacteristic.class, new BleCharacteristicViewBinder().setOnClickListener(this::dialog));
        mAdapter.register(BluetoothGattDescriptor.class, new BleDescriptorViewBinder().setOnClickListener(this::dialog));
        rvDiscover.setLayoutManager(new LinearLayoutManager(this));
        rvDiscover.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mPresenter.connectOrDisconnect(mDeviceAddress);
    }

    @Override
    public void showConnect() {
        btConnect.setText("Connect");
        btConnect.setClickable(true);
    }

    @Override
    public void showConnecting() {
        btConnect.setText("Connecting");
        btConnect.setClickable(false);
    }

    @Override
    public void showDisconnect() {
        btConnect.setText("Disconnect");
        btConnect.setClickable(true);
    }

    @Override
    public void showConnectList(List<?> services) {
        mAdapter.setItems(services);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void switchNotifyOrIndicate(String text, boolean show) {
        btNotify.setText(text);
        btNotify.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        tvMessage.setText(message);
    }

    private void handleCharacteristics(DialogInterface dialog, int which, List<String> choice, BluetoothGattCharacteristic result) {
        String operation = choice.get(which);
        if ("Read".equals(operation)) {
            mPresenter.read(result.getUuid());
        } else if ("Write".equals(operation)) {
            mPresenter.write(result.getUuid(), etWrite.getText().toString());
        } else if ("Notify".equals(operation)) {
            mPresenter.notification(result.getUuid());
        } else if ("Indicate".equals(operation)) {
            mPresenter.indicate(result.getUuid());
        }
        dialog.dismiss();
    }

    private void handleDescriptor(DialogInterface dialog, int which, BluetoothGattDescriptor result) {
        switch (which) {
            case 0:
                mPresenter.readDescriptor(result);
                break;
            case 1:
                mPresenter.writeDescriptor(result, etWrite.getText().toString());
                break;
        }
        dialog.dismiss();
    }

    private void dialog(int i, Object result) {
        if (result instanceof BluetoothGattCharacteristic) {
            List<String> choice = new ArrayList<>(3);
            if (RxBle.isCharacteristicReadable((BluetoothGattCharacteristic) result))
                choice.add("Read");
            if (RxBle.isCharacteristicWritable((BluetoothGattCharacteristic) result))
                choice.add("Write");
            if (RxBle.isCharacteristicNotifiable((BluetoothGattCharacteristic) result))
                choice.add("Notify");
            if (RxBle.isCharacteristicIndicatable((BluetoothGattCharacteristic) result))
                choice.add("Indicate");

            if (choice.isEmpty()) {
                toast("no operation for this characteristic");
                return;
            }
            new AlertDialog.Builder(this)
                    .setItems(choice.toArray(new String[choice.size()]),
                            ((dialog, which) -> handleCharacteristics(dialog, which, choice, (BluetoothGattCharacteristic) result)))
                    .setCancelable(true)
                    .create()
                    .show();
        }

        if (result instanceof BluetoothGattDescriptor) {
            new AlertDialog.Builder(this)
                    .setItems(new String[]{"Read", "Write"},
                            ((dialog, which) -> handleDescriptor(dialog, which, (BluetoothGattDescriptor) result)))
                    .setCancelable(true)
                    .create()
                    .show();
        }
    }
}
