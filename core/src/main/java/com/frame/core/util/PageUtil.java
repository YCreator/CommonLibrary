package com.frame.core.util;

/**
 * 页面控制
 * Created by yzd on 2016/5/13.
 */
public class PageUtil {

    private int currentPage;
    private int pageSize;
    private int tempCurrentPage;

    @Override
    public String toString() {
        return "PageUtil{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", tempCurrentPage=" + tempCurrentPage +
                '}';
    }

    public PageUtil() {
        this.currentPage = 1;
        this.pageSize = 10;
        this.tempCurrentPage = this.currentPage;
    }

    public PageUtil(int pageSize) {
        this.currentPage = 1;
        this.pageSize = pageSize;
        this.tempCurrentPage = this.currentPage;
    }

    public PageUtil(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.tempCurrentPage = this.currentPage;
    }

    public int getIntCurrentPage() {
        return currentPage;
    }

    public String getCurrentPage() {
        return String.valueOf(currentPage);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageSize() {
        return String.valueOf(pageSize);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 下一页
     */
    public void nextPage() {
        currentPage ++;
    }

    /**
     * 前一页
     */
    public void nextPageBefore() {
        if (currentPage > 1) {
            currentPage --;
        }
    }

    /**
     * 首页
     */
    public void indexPage() {
        currentPage = 1;
    }

    /**
     * 成功时记录当前页
    */
    public void recordCurrentPage() {
        tempCurrentPage = currentPage;
    }

    /**
     * 失败时回滚当前页
     */
    public void rollBackPage() {
        currentPage = tempCurrentPage;
    }
}
