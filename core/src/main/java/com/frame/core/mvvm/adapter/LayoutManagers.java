package com.frame.core.mvvm.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.frame.core.adapter.FullyGridLayoutManager;
import com.frame.core.adapter.FullyLinearLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A collection of factories to create RecyclerView LayoutManagers so that you can easily set them
 * in your layout.
 */
public class LayoutManagers {
    protected LayoutManagers() {
    }

    public interface LayoutManagerFactory {
        RecyclerView.LayoutManager create(RecyclerView recyclerView);
    }

    /**
     * A {@link LinearLayoutManager}.
     */
    public static LayoutManagerFactory linear() {
        return recyclerView -> new LinearLayoutManager(recyclerView.getContext());
    }

    /**
     * A {@link LinearLayoutManager} with the given orientation and reverseLayout.
     */
    public static LayoutManagerFactory linear(@Orientation final int orientation, final boolean reverseLayout) {
        return recyclerView -> new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout);
    }

    /**
     *
     * A {@link LinearLayoutManager}.
     */
    public static LayoutManagerFactory linearFull() {
        return recyclerView -> new FullyLinearLayoutManager(recyclerView.getContext());
    }

    /**
     * A {@link GridLayoutManager} with the given spanCount.
     */
    public static LayoutManagerFactory grid(final int spanCount) {
        return recyclerView -> new GridLayoutManager(recyclerView.getContext(), spanCount);
    }

    /**
     * A {@link GridLayoutManager} with the given spanCount, spanSizeLookup
     */
    public static LayoutManagerFactory grid(final int spanCount, GridLayoutManager.SpanSizeLookup sizeLookup) {
        return recyclerView -> {
            GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(), spanCount);
            manager.setSpanSizeLookup(sizeLookup);
            return manager;
        };
    }

    /**
     * A {@link GridLayoutManager} with the given spanCount, orientation and reverseLayout.
     **/
    public static LayoutManagerFactory grid(final int spanCount, @Orientation final int orientation, final boolean reverseLayout) {
        return recyclerView -> new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverseLayout);
    }

    /**
     * A {@link GridLayoutManager} with the given spanCount, orientation and reverseLayout, spanSizeLookup
     */
    public static LayoutManagerFactory grid(int spanCount, @Orientation final int orientation, final boolean reverseLayout, GridLayoutManager.SpanSizeLookup sizeLookup) {
        return recyclerView -> {
            GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverseLayout);
            manager.setSpanSizeLookup(sizeLookup);
            return manager;
        };
    }

    /**
     *
     * @param spanCount
     * @return
     */
    public static LayoutManagerFactory gridFull(int spanCount) {
        return recyclerView -> new FullyGridLayoutManager(recyclerView.getContext(), spanCount);
    }

    /**
     * A {@link StaggeredGridLayoutManager} with the given spanCount and orientation.
     */
    public static LayoutManagerFactory staggeredGrid(final int spanCount, @Orientation final int orientation) {
        return recyclerView -> new StaggeredGridLayoutManager(spanCount, orientation);
    }

    @IntDef({LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }
}
