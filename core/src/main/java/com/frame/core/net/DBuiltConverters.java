package com.frame.core.net;

import com.frame.core.util.GsonUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by yzd on 2017/9/16 0016.
 */

public final class DBuiltConverters extends DConverter.Factory {

    @Override
    public DConverter<?, String> stringConverter(Type type, Annotation[] annotations) {
        if (type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == Boolean.class) {
            return ToStringConverter.TOSTRING;
        } else {
            return ToJsonStringConverter.TOJSONSTRING;
        }
    }

    private static final class ToStringConverter implements DConverter<Object, String> {

        static final ToStringConverter TOSTRING = new ToStringConverter();

        @Override
        public String convert(Object value) throws IOException {
            return value.toString();
        }
    }

    private static final class ToJsonStringConverter implements DConverter<Object, String> {

        static final ToJsonStringConverter TOJSONSTRING = new ToJsonStringConverter();

        @Override
        public String convert(Object value) throws IOException {
            return GsonUtil.toJson(value);
        }
    }

    static final class ToMapStringConverter implements DConverter<Object, String> {

        static final ToMapStringConverter TOMAPSTRING = new ToMapStringConverter();

        @Override
        public String convert(Object value) throws IOException {
            if (value instanceof String
                    || value instanceof Integer
                    || value instanceof Long
                    || value instanceof Float
                    || value instanceof Double
                    || value instanceof Boolean) {
                return value.toString();
            } else {
                return GsonUtil.toJson(value);
            }
        }
    }
}
