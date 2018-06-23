package com.lib.commonlibrary;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.core.adapter.BaseRvAdapter;
import com.frame.core.bluetooth.common.PropertyType;
import com.frame.core.bluetooth.model.BluetoothLeDevice;
import com.frame.core.adapter.AdapterItem;
import com.frame.core.util.utils.LogUtils;
import com.frame.core.util.utils.StringUtils;
import com.frame.core.util.utils.ToastUtils;
import com.jcx.hnn.debug.R;
import com.jcx.hnn.debug.bt.BTManager;
import com.jcx.hnn.debug.bt.BleUtil;
import com.jcx.hnn.debug.bt.BluetoothService;
import com.jcx.hnn.debug.bt.CallbackDataEvent;
import com.jcx.hnn.debug.bt.ConnectEvent;
import com.jcx.hnn.debug.bt.IOCommand;
import com.jcx.hnn.debug.bt.NotifyDataEvent;
import com.jcx.hnn.debug.bt.ScanEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    TextView tv;

    BluetoothLeDevice bluetoothLeDevice;

    private BluetoothService chatService;

    private BaseRvAdapter<BluetoothLeDevice> adapter = new BaseRvAdapter<BluetoothLeDevice>(null) {
        @NonNull
        @Override
        public AdapterItem<BluetoothLeDevice> onCreateItem(int viewType) {
            return new BTItem(null);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EventBus.getDefault().register(this);

        RecyclerView recyclerView = this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        /*BTHelper.start().getSearch()
                .setAdapterItem(recyclerView, new BTItem(callback))
                .search(10000, false);*/
        //Bluetooth.getInstance().startScan(periodScanCallback);
        BTManager.getInstance().initBroadcast(this);
        BTManager.getInstance().customSearch();

        EditText editText = this.findViewById(R.id.et);
        tv = this.findViewById(R.id.tv);
        //this.findViewById(R.id.btn1).setOnClickListener(v -> BTHelper.start().getSearch().search(10000, false));
        //this.findViewById(R.id.btn1).setOnClickListener(v -> Bluetooth.getInstance().stopScan(periodScanCallback));

        //this.findViewById(R.id.btn2).setOnClickListener(v -> BTHelper.start().disConnect());
        this.findViewById(R.id.btn2).setOnClickListener(v -> {
            if (bluetoothLeDevice != null)
                BTManager.getInstance().disconnect(bluetoothLeDevice);
        });

        this.findViewById(R.id.btn3).setOnClickListener(v -> {
            String s = editText.getText().toString();
            if (chatService != null && !StringUtils.isEmpty(s)) {
                chatService.write(s.getBytes());
            }
            /*if (StringUtils.isEmpty(s) || bluetoothLeDevice == null) return;
            BTManager.getInstance().write(bluetoothLeDevice, s.getBytes());*/
        });
        /*this.findViewById(R.id.btn3).setOnClickListener(v -> {
            String s = editText.getText().toString();
            if (StringUtils.isEmpty(s)) return;
            BTHelper.start().connectAndWrite(BTHelper.start().getCurrentMac(), callback)
                    .write(s.getBytes());
        });*/
        chatService = BleUtil.instance()
                .setCommand(new IOCommand())
                .setup(new BleUtil.Callback() {
                    @Override
                    public void connectStatus(int status) {
                        switch (status) {
                            case BluetoothService.STATE_CONNECTED:
                                LogUtils.d("Ble:Status", "已连接" + BleUtil.instance().getConnectedDeviceName());
                                break;
                            case BluetoothService.STATE_CONNECTING:
                                LogUtils.d("Ble:Status", "连接中");
                                break;
                            case BluetoothService.STATE_LISTEN:
                            case BluetoothService.STATE_NONE:
                                LogUtils.d("Ble:Status", "未连接");
                                break;
                        }
                    }

                    @Override
                    public void writeListener(String data) {
                        LogUtils.d("Ble:Write:Me", data);
                    }

                    @Override
                    public void readListener(String data) {
                        if (tv != null) {
                            tv.setText(data);
                        }
                        LogUtils.d("Ble:Read:" + BleUtil.instance().getConnectedDeviceName(), data);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BTManager.getInstance().uninitBroadcast(this);
        EventBus.getDefault().unregister(this);
        BleUtil.instance().onStop();
    }

    @Subscribe
    public void search(ScanEvent event) {
        Button button = this.findViewById(R.id.btn1);
        button.setText(event.isScanFinish() ? "搜索" : "停止");
        button.setOnClickListener(v -> {
            if (event.isScanFinish()) {
                BTManager.getInstance().customSearch();
            } else {
                BTManager.getInstance().customStopSearch();
                search(event.setScanFinish(true));
            }
        });
        if (event.isScanFinish() || event.isScanTimeout()) return;
        if (adapter != null && event.getBluetoothLeDeviceStore() != null) {
            adapter.setData(event.getBluetoothLeDeviceStore().getDeviceList());
        }
    }

    @Subscribe
    public void connectEvent(ConnectEvent event) {
        if (event.isSuccess()) {
            List<BluetoothGattService> list = event.getDeviceMirror().getBluetoothGatt().getServices();
            for (BluetoothGattService service : list) {
                Log.d("BTManager:service", service.getUuid().toString());
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    Log.d("BTManager:char", service.getUuid().toString());
                }
            }
            BluetoothGattService service = null;
            BluetoothGattCharacteristic characteristic = null;
            if (list.size() > 0) {
                service = list.get(0);
                if (service.getCharacteristics().size() > 0) {
                    characteristic = service.getCharacteristics().get(0);
                }
            }
            LogUtils.i("BTManager:connectEvent", service != null && characteristic != null);
            if (service != null && characteristic != null)
                BTManager.getInstance().bindChannel(bluetoothLeDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
            ToastUtils.showShort("连接成功");
        } else if (event.isDisconnected()) {
            ToastUtils.showShort("已连接");
        } else {
            ToastUtils.showShort("链接失败");
        }
    }

    @Subscribe
    public void connect(BluetoothLeDevice bluetoothLeDevice) {
        this.bluetoothLeDevice = bluetoothLeDevice;
        chatService.connect(bluetoothLeDevice.getDevice(), true);
        /*ParcelUuid[] uuids = bluetoothLeDevice.getDevice().getUuids();
        Log.d("BTManager", bluetoothLeDevice.getAddress());
        for (ParcelUuid uuid : uuids) {
            Log.d("BTManager:connect", uuid.getUuid().toString());
        }
        BTManager.getInstance().connect(bluetoothLeDevice);*/
    }

    @Subscribe
    public void notify(NotifyDataEvent event) {
        if (tv != null) {
            tv.setText("byte:" + Arrays.toString(event.getData()) + "\n" + "string:" + new String(event.getData()));
        }
    }

    @Subscribe
    public void sendData(CallbackDataEvent event) {
        ToastUtils.showLong("byte:" + Arrays.toString(event.getData()) + "\n" + "string:" + new String(event.getData()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BleUtil.instance().onStart();
    }

}
