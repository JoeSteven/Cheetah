package joey.cheetah.sample.ble;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cheetah.core.mvp.AbsActivity;
import cheetah.core.mvp.AbsPresenter;
import cheetah.core.permission.PermissionListener;
import cheetah.core.permission.PermissionUtil;
import cheetah.core.utils.CLog;
import cheetah.core.utils.Global;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import joey.cheetah.R;

/**
 * Description:
 * author:Joey
 * date:2018/8/2
 */
public class BleDemoActivity extends AbsActivity {

    private BluetoothAdapter mAdapter;
    private boolean hasPermission = true;
    private boolean rxBle = true;

    @BindView(R.id.list_ble)
    RecyclerView mLvBle;
    private BleAdapter mListAdapter;
    private BluetoothLeScanner mScanner;
    private ScanCallback mCallBack;
    private BluetoothGatt mGatt;
    private RxBleClient mRxBleClient;
    private Disposable rxScan;
    private Disposable mRxConnect;

    @Override
    protected int initLayout() {
        return R.layout.activity_ble_demo;
    }

    @Override
    protected void initView() {
        mListAdapter = new BleAdapter();
        mLvBle.setAdapter(mListAdapter);
        mLvBle.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        if (rxBle){
            mRxBleClient = RxBleClient.create(Global.context());
//            return;
        }
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        PermissionUtil.requestPermission(this, new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        hasPermission = true;
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        hasPermission = false;
                    }
                },
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void openBle() {
//        if (rxBle) {
//            return;
//        }
        if (!hasPermission) {
            toast("don't have permission!");
            return;
        }
        if (!mAdapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, 0);
            mAdapter.enable();
            toast("open ble");
        }

    }


    private void connectBle(RxBleDevice device) {
        mRxConnect = device.establishConnection(false)
                .flatMap((Function<RxBleConnection, ObservableSource<RxBleDeviceServices>>)
                        rxBleConnection -> rxBleConnection.discoverServices().toObservable())
                .subscribe(rxBleDeviceServices -> {
            for (BluetoothGattService service: rxBleDeviceServices.getBluetoothGattServices()) {
                log("on service discoverd:" + service.getUuid().toString());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void scanBle() {
        if (rxBle) {
            rxScan = mRxBleClient.scanBleDevices(new ScanSettings.Builder()
                    .build())
                    .subscribe(scanResult -> {mListAdapter.addData(scanResult.getBleDevice());});
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScanner = mAdapter.getBluetoothLeScanner();
            mCallBack = new ScanCallback() {

                @Override
                public void onScanResult(int callbackType, ScanResult result) {
//                    mListAdapter.addData(result.getDevice());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (ScanResult result : results) {
//                        mListAdapter.addData(result.getDevice());
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    toast("scan failed");
                }
            };
            mScanner.startScan(mCallBack);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mAdapter.startLeScan((device, rssi, scanRecord) -> {
            });
        }
        toast("start scan!");
    }

    private void stopScan() {
        if (rxBle) {
            rxScan.dispose();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScanner.stopScan(mCallBack);
            toast("stop scan!");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void connectBle(BluetoothDevice device) {
        toast("connect device = " + device.getName() + " address:" + device.getAddress());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mGatt = device.connectGatt(this, false, new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                   log("status changed:" + status+ " new state:" + newState);
                   if (newState == 2) {
                       mGatt.discoverServices();
                   }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    for (BluetoothGattService service: gatt.getServices()) {
                        log("on service discoverd:" + service.getUuid().toString());
                        for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                            gatt.readCharacteristic(c);
                        }
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    log("on service discoverd:" + characteristic.getUuid());
                    byte[] data = characteristic.getValue();
                    log(Arrays.toString(data));
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                }
            });

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void disConnect() {
        if (rxBle) {
            mRxConnect.dispose();
            return;
        }
        mGatt.disconnect();
    }

    private void closeBle() {
        mAdapter.disable();
        toast("close ble!");
    }

    @OnClick({R.id.btOpen, R.id.btClose, R.id.btScan, R.id.btStop, R.id.btDisconnect})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btOpen:
                openBle();
                break;
            case R.id.btScan:
                scanBle();
                break;
            case R.id.btStop:
                stopScan();
                break;
            case R.id.btDisconnect:
                disConnect();
                break;
            case R.id.btClose:
                closeBle();
                break;
        }
    }

    @Override
    protected AbsPresenter initPresenter() {
        return null;
    }

    class BleAdapter extends RecyclerView.Adapter<BleAdapter.BleHolder> {
        private List<RxBleDevice> devices;

        public void addData(RxBleDevice device) {
            if (devices == null) {
                devices = new ArrayList<>();
            }
            if (!devices.contains(device)) {
                devices.add(device);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public BleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BleHolder(LayoutInflater.from(BleDemoActivity.this).inflate(R.layout.item_ble, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BleHolder holder, int position) {
            holder.setDevice(devices.get(position));
        }

        @Override
        public int getItemCount() {
            return devices == null ? 0 : devices.size();
        }

        class BleHolder extends RecyclerView.ViewHolder {
            private final TextView tvName;
            private final TextView tvAddr;
            private final TextView tvType;

            public BleHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvAddr = itemView.findViewById(R.id.tvAddress);
                tvType = itemView.findViewById(R.id.tvType);
            }

            public void setDevice(RxBleDevice device) {
                tvAddr.setText(device.getMacAddress());
                tvName.setText(device.getName());
                tvType.setText("type:" + device.getBluetoothDevice().getType());
                itemView.setOnClickListener(v -> connectBle(device));
            }
        }

    }


    public void log(String msg) {

        CLog.d("ble_demo",msg);
    }

}
