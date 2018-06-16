package com.frame.core.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Created by yzd on 2018/6/16 0016.
 */

public final class NothingTransformer implements ObservableTransformer {
    @Override
    public ObservableSource apply(Observable upstream) {
        return upstream;
    }
}
