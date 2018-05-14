package com.lib.imagelib.loader;

import java.io.File;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public interface FileGetter {

    void onSuccess(File file, int width, int height);

    void onFail(Throwable e);
}
