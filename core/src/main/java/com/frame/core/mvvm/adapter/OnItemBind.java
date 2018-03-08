package com.frame.core.mvvm.adapter;

/**
 * Callback for setting up a {@link ItemBinding} for an item in the collection.
 *
 * @param <T> the item type
 */
public interface OnItemBind<T> {
    /**
     * Called on each item in the collection, allowing you to modify the given {@linkItemBinding}.
     * Note that you should not do complex processing in this method as it's called many times.
     */
    void onItemBind(ItemBinding itemBinding, int position, T item);
}
