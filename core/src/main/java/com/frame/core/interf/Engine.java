package com.frame.core.interf;

import java.io.IOException;
import java.util.Map;

/**
 * 网络引擎接口
 * Created by yzd on 2016/4/2.
 */
public interface Engine {

    String API_URL = "http://api.59iwh.com/%s";

    String METHOD_GET = "GET";

    String METHOD_POST = "POST";

    String CHARSET_NAME = "UTF-8";

    String CONTENT_TYPE_LABEL = "Content-Type";

    String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    String post(String method, Map<String, String> paramsMap) throws IOException;

    String post(String method, String[] paramKeys, String[] paramValues) throws IOException;

}
