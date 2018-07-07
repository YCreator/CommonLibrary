package com.frame.core.mvvm.binding.viewadapter.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by goldze on 2017/6/16.
 */
public class LineManagers {
    protected LineManagers() {
    }

    public interface LineManagerFactory {
        RecyclerView.ItemDecoration create(RecyclerView recyclerView);
    }

    public static LineManagerFactory both() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.BOTH);
    }

    public static LineManagerFactory both(int db) {
        return recyclerView -> new DividerLine(recyclerView.getContext(), db, DividerLine.LineDrawMode.BOTH);
    }

    public static LineManagerFactory horizontal() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.HORIZONTAL);
    }

    public static LineManagerFactory horizontal(int db) {
        return recyclerView -> new DividerLine(recyclerView.getContext(), db, DividerLine.LineDrawMode.HORIZONTAL);
    }

    public static LineManagerFactory vertical() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.VERTICAL);
    }

    public static LineManagerFactory vertical(int db) {
        return recyclerView -> new DividerLine(recyclerView.getContext(), db, DividerLine.LineDrawMode.VERTICAL);
    }

    public static LineManagerFactory space(int dp) {
        return recyclerView -> new SpaceLine(dp);
    }

    public static LineManagerFactory spaceTop(int dp) {
        return recyclerView -> new SpaceLine(dp, SpaceLine.LineSpaceMode.TOP);
    }

    public static LineManagerFactory spaceRight(int dp) {
        return recyclerView -> new SpaceLine(dp, SpaceLine.LineSpaceMode.RIGHT);
    }

    public static LineManagerFactory spaceBottom(int dp) {
        return recyclerView -> new SpaceLine(dp, SpaceLine.LineSpaceMode.BOTTOM);
    }

    public static LineManagerFactory spaceLeft(int dp) {
        return recyclerView -> new SpaceLine(dp, SpaceLine.LineSpaceMode.LEFT);
    }


}
