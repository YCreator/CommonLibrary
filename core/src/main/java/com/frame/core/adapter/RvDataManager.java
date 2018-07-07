package com.frame.core.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.core.util.TLog;
import com.frame.core.util.PageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzd on 2017/7/26 0026.
 * recycleview翻页管理
 */

public class RvDataManager<T> {

    private RecyclerView recyclerView;
    private PageUtil page;
    private LoadMoreItem loadMoreItem;
    private BaseRvAdapter<T> adapter;
    private OnNotifyPageListener notifyDataStatus;
    private CreateAdapterItem<T> rvAdapterItem;
    private LinearLayoutManager manager;
    private int canUpPosition;
    private boolean autoBindView = true;

    private RvDataManager() {

    }

    private RvDataManager(Builder<T> builder) {
        recyclerView = builder.recyclerView;
        page = builder.page;
        loadMoreItem = new LoadMoreItem();
        notifyDataStatus = builder.notifyDataStatus;
        rvAdapterItem = builder.item;
        if (builder.spanSize != 0) {
            setSpanSizeAndScrollListener(builder.spanSize);
        }
        canUpPosition = builder.canUpPosition;
        autoBindView = builder.autoBindView;

    }

    private void setSpanSizeAndScrollListener(int spanSize) {
        if (spanSize < 2) {
            manager = new LinearLayoutManager(recyclerView.getContext());
        } else {
            manager = new GridLayoutManager(recyclerView.getContext(), spanSize);
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getData().size() == position ? ((GridLayoutManager) manager).getSpanCount() : 1;
                }
            });
        }
        recyclerView.setLayoutManager(manager);
        setScrollListener();
    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //控制ImageLoader的加载
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (notifyDataStatus != null) {
                            int lastPosition = manager.findLastCompletelyVisibleItemPosition();
                            if (!loadMoreItem.isLoading() && loadMoreItem.isCanLoading() && adapter != null && lastPosition == adapter.getData().size() && page.status()) {
                                loadMoreItem.loadingMore();
                                notifyDataStatus.requestNextPageData();
                            }
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (notifyDataStatus != null && canUpPosition != 0) {
                    int lastPosition = manager.findLastCompletelyVisibleItemPosition();
                    notifyDataStatus.showUpToTopFunction(lastPosition >= canUpPosition);
                }
            }
        });
    }

    public void setOnNotifyDataListener(OnNotifyPageListener notifyDataStatus) {
        this.notifyDataStatus = notifyDataStatus;
    }

    public LoadMoreItem getLoadMoreItem() {
        return loadMoreItem;
    }

    public void setAdapter(BaseRvAdapter<T> adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public BaseRvAdapter<T> getAdapter() {
        return adapter;
    }

    public List<T> getData() {
        if (adapter != null) {
            return adapter.getData();
        }
        return new ArrayList<>();
    }

    public void bindData(List<T> data) {
//        LogUtils.e("bindData:"+page);
        if (page.getIntCurrentPage() == 1) {
            TLog.i("data", page.getIntCurrentPage(), data.size());
            if (data.size() > 0) {
                loadMoreItem.setIndexLoadMoreState(true);
                if (adapter != null) {
                    adapter.setData(data);
                } else {
                    newAdapter(data);
                }
                if (notifyDataStatus != null) {
                    notifyDataStatus.emptyDataListListener(false);
                }
            } else {
                loadMoreItem.setIndexLoadMoreState(false);
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
                loadMoreItem.setLoadMoreState(true);
                if (adapter != null) {
                    adapter.addAll(data);
//                    LogUtils.e("添加了");
                }
            } else {
                page.rollBackPage();
                loadMoreItem.setLoadMoreState(false);
            }
        }
    }

    private void newAdapter(List<T> list) {
        adapter = new BaseRvAdapter<T>(list, autoBindView) {

            @Override
            public int getItemCount() {
                return super.getItemCount() + 1;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == getItemCount() - 1) {
                    return -1;
                }
                return super.getItemViewType(position);
            }

            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public AdapterItem<T> onCreateItem(int viewType) {
                if (viewType == -1) {
                    return loadMoreItem;
                }
                return rvAdapterItem.getAdapterItem();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class Builder<T> {

        private RecyclerView recyclerView;              //列表控件
        private PageUtil page;                          //翻页对象
        private OnNotifyPageListener notifyDataStatus;      //通知回调
        private CreateAdapterItem<T> item;                  //每项实现方式
        private int spanSize;                           //列数
        private int canUpPosition;                      //显示置顶的位置
        private boolean autoBindView = true;

        public Builder<T> setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public Builder<T> setPage(PageUtil page) {
            this.page = page;
            return this;
        }

        public Builder<T> setOnNotifyPageListener(OnNotifyPageListener notifyDataStatus) {
            this.notifyDataStatus = notifyDataStatus;
            return this;
        }

        public Builder<T> setRvAdapterItem(CreateAdapterItem<T> item) {
            this.item = item;
            return this;
        }

        public Builder<T> setSpansize(int spansize) {
            this.spanSize = spansize;
            return this;
        }

        public Builder<T> setCanUpPosition(int position) {
            this.canUpPosition = position;
            return this;
        }

        public Builder<T> setAutoBindView(boolean isAutoBindView) {
            this.autoBindView = isAutoBindView;
            return this;
        }

        public RvDataManager<T> build() {
            return new RvDataManager<>(this);
        }

    }

}
