package com.lib.commonlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.core.rx.bluetooth.callback.BaseResultCallback;
import com.frame.core.util.utils.StringUtils;
import com.jcx.hnn.debug.R;
import com.jcx.hnn.debug.bt.BTHelper;

import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RecyclerView recyclerView = this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        BTHelper.start().getSearch()
                .setAdapterItem(recyclerView, new BTItem(callback))
                .search(10000, false);

        EditText editText = this.findViewById(R.id.et);
        tv = this.findViewById(R.id.tv);
        this.findViewById(R.id.btn1).setOnClickListener(v -> BTHelper.start().getSearch().search(10000, false));

        this.findViewById(R.id.btn2).setOnClickListener(v -> BTHelper.start().disConnect());

        this.findViewById(R.id.btn3).setOnClickListener(v -> {
            String s = editText.getText().toString();
            if (StringUtils.isEmpty(s)) return;
            BTHelper.start().connectAndWrite(BTHelper.start().getCurrentMac(), callback)
                    .write(s.getBytes());
        });
    }

    private BaseResultCallback<byte[]> callback = new BaseResultCallback<byte[]>() {
        @Override
        public void onSuccess(byte[] data) {
            if (tv != null) {
                tv.setText("byte:" + Arrays.toString(data) + "\n" + "string:" + new String(data));
            }
        }

        @Override
        public void onFail(String msg) {
            if (tv != null) {
                tv.setText("错误：" + msg);
            }
        }
    };
}
