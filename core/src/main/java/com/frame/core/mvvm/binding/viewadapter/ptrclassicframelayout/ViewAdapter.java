package com.frame.core.mvvm.binding.viewadapter.ptrclassicframelayout;

import android.databinding.BindingAdapter;

import com.frame.core.mvvm.binding.command.BindingCommand;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by yzd on 2018/3/8 0008.
 */

public class ViewAdapter {

    @BindingAdapter(value={"onRefreshCommand"}, requireAll = false)
    public static void onRefreshCommand(PtrClassicFrameLayout layout, BindingCommand onRefreshCommand) {
            layout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    if (onRefreshCommand != null) {
                        onRefreshCommand.execute();
                    }
                }
            });
    }
}
