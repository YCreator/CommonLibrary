package com.frame.core.mvvm.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * @see {@link BindingCollectionAdapters}
 */
public class BindingRecyclerViewAdapters {
    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView, ItemBinding<T> itemBinding, List<T> items, BindingRecyclerViewAdapter<T> adapter, BindingRecyclerViewAdapter.ItemIds<? super T> itemIds, BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }
}
