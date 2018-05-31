package com.lib.commonlibrary.viewAdapter.tkrefreshlayout;


import android.databinding.BindingAdapter;

import com.frame.core.mvvm.binding.command.BindingCommand;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;


/**
 * Created by goldze on 2017/6/16.
 * SmartRefreshLayout列表刷新的绑定适配器
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(TwinklingRefreshLayout layout, final BindingCommand onRefreshCommand, final BindingCommand onLoadMoreCommand) {
        layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
            }
        });
    }

    @BindingAdapter(value = {"header", "footer"}, requireAll = false)
    public static void setHeaderAndBottomerCommand(TwinklingRefreshLayout layout, IHeaderView headerView, IBottomView footerView) {
        if (headerView != null)
            layout.setHeaderView(headerView);
        if (footerView != null)
            layout.setBottomView(footerView);
    }
}
