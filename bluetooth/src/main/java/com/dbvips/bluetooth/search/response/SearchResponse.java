package com.dbvips.bluetooth.search.response;

import com.dbvips.bluetooth.search.SearchResult;

/**
 * Created by dingjikerbo on 2016/9/1.
 */
public interface SearchResponse {

    void onSearchStarted();

    void onDeviceFounded(SearchResult device);

    void onSearchStopped();

    void onSearchCanceled();
}
