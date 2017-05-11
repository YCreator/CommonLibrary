package com.lib.commonlibrary;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;

/**
 * Created by yzd on 2017/5/8 0008.
 */

public interface Api {

    @POST("V2/CTGoods")
    Observable<JsonDataEntity<List<GoodsBean>>> getGoods(@FieldMap Map<String, String> params);

}
