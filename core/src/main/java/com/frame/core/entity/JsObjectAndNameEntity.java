package com.frame.core.entity;

import android.webkit.JavascriptInterface;

/**
 * Created by yzd on 2018/4/21 0021.
 */

public class JsObjectAndNameEntity {
    private String name;
    private Object obJs;

    public JsObjectAndNameEntity(String name, Object obJs) {
        this.name = name;
        this.obJs = obJs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JavascriptInterface
    public Object getObJs() {
        return obJs;
    }

    public void setObJs(Object obJs) {
        this.obJs = obJs;
    }
}
