package com.frame.core.adapter;

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.ListView;

import com.frame.core.interf.AdapterItem;
import com.frame.core.util.TLog;
import com.frame.core.util.PageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzd on 2017/7/26 0026.
 * listview数据翻页管理
 */

public class LvDataManager<T> {

    private LvLoadItem loadItem;
    private ListView listView;
    private PageUtil page;
    private BaseLvAdapter<T> adapter;
    private OnNotifyPageListener notifyDataStatus;
    private CreateAdapterItem<T> rvAdapterItem;
    private int canUpPosition;

    private LvDataManager() {
    }

    private LvDataManager(Builder<T> builder) {
        listView = builder.listView;
        page = builder.page;
        loadItem = new LvLoadItem(listView.getContext());
        listView.addFooterView(loadItem.getItemView());
        notifyDataStatus = builder.listener;
        rvAdapterItem = builder.adapterItem;
        canUpPosition = builder.canUpPosition;
        setScrollListener();
    }

    public void setAdapter(BaseLvAdapter<T> adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    private void setScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        if (notifyDataStatus != null) {
                            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                                if (!loadItem.isLoading() && loadItem.isCanLoading()) {
                                    loadItem.loadingMore();
                                    notifyDataStatus.requestNextPageData();
                                }
                            }
                        }
                        break;
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                TLog.i("lvDataManager", totalItemCount, canUpPosition);
                if (canUpPosition != 0 && notifyDataStatus != null) {
                    notifyDataStatus.showUpToTopFunction(firstVisibleItem >= canUpPosition);
                }
            }
        });
    }

    public void bindData(List<T> data) {
        if (page.getIntCurrentPage() == 1) {
            if (data.size() > 0) {
                loadItem.setIndexLoadMoreState(true);
                if (adapter != null) {
                    adapter.setData(data);
                } else {
                    newAdapter(data);
                }
                if (notifyDataStatus != null) {
                    notifyDataStatus.emptyDataListListener(false);
                }
            } else {
                loadItem.setIndexLoadMoreState(false);
                if (adapter != null) {
                    adapter.itemsClear();
                }
                if (notifyDataStatus != null) {
                    notifyDataStatus.emptyDataListListener(true);
                }
            }
        } else if (page.getIntCurrentPage() > 1) {
            if (data.size() > 0) {
                page.recordCurrentPage();
                loadItem.setLoadMoreState(true);
                if (adapter != null) {
                    adapter.addAll(data);
                }
            } else {
                page.rollBackPage();
                loadItem.setLoadMoreState(false);
            }
        }
    }

    public List<T> getData() {
        if (adapter != null) {
            return adapter.getData();
        }
        return new ArrayList<>();
    }

    public BaseLvAdapter<T> getAdapter() {
        return adapter;
    }

    private void newAdapter(List<T> data) {
        adapter = new BaseLvAdapter<T>(data) {
            @NonNull
            @Override
            public AdapterItem<T> onCreateItem(int viewType) {
                return rvAdapterItem.getAdapterItem();
            }
        };
        listView.setAdapter(adapter);
    }

    public static class Builder<T> {
        private ListView listView;
        private PageUtil page;
        private CreateAdapterItem<T> adapterItem;
        private OnNotifyPageListener listener;
        private int canUpPosition;

        public Builder<T> setListView(ListView listView) {
            this.listView = listView;
            return this;
        }

        public Builder<T> setPage(PageUtil page) {
            this.page = page;
            return this;
        }

        public Builder<T> setLvAdapterItem(CreateAdapterItem<T> adapterItem) {
            this.adapterItem = adapterItem;
            return this;
        }

        public Builder<T> setOnNotifyPageListener(OnNotifyPageListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder<T> setCanUpPosition(int position) {
            this.canUpPosition = position;
            return this;
        }

        public LvDataManager<T> build() {
            return new LvDataManager<>(this);
        }
    }
}
