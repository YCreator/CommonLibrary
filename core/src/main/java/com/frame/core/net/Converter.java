package com.frame.core.net;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by yzd on 2017/9/16 0016.
 */

public interface Converter<F, T> {

    T convert(F value) throws IOException;

    abstract class Factory {

        public Converter<?, String> stringConverter(Type type, Annotation[] annotations) {
            return null;
        }
    }
}
