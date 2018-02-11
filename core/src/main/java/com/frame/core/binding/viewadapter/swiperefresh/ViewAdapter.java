package com.frame.core.binding.viewadapter.swiperefresh;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.frame.core.binding.command.BindingCommand;


/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final BindingCommand onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (onRefreshCommand != null) {
                onRefreshCommand.execute();
            }
        });
    }

}
