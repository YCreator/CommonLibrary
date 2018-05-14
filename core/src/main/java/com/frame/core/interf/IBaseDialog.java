package com.frame.core.interf;

import android.os.Bundle;

/**
 * Created by yzd on 2018/5/14 0014.
 */

public interface IBaseDialog {

    void initView();

    void initListener();

    void process(Bundle savedInstanceState);
}
