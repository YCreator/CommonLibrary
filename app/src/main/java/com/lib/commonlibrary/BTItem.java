package com.lib.commonlibrary;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.frame.core.interf.AdapterItem;
import com.frame.core.rx.bluetooth.bean.BLEDevice;
import com.frame.core.rx.bluetooth.callback.BaseResultCallback;
import com.jcx.hnn.debug.R;
import com.jcx.hnn.debug.bt.BTHelper;

import butterknife.Bind;

/**
 * Created by yzd on 2018/6/14 0014.
 */

public class BTItem implements AdapterItem<BLEDevice> {

    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn)
    Button btn;

    private BaseResultCallback<byte[]> callback;

    public BTItem(BaseResultCallback<byte[]> callback) {
        this.callback = callback;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_bt;
    }

    @Override
    public void initItemViews(View itemView) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(BLEDevice model, int position) {
        text.setText("bt：" + model.getDeviceName() + "：" + model.getMac());
        btn.setOnClickListener(v -> BTHelper.start().connect(model.getMac(), callback));
    }
}
