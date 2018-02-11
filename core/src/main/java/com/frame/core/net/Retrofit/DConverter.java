package com.frame.core.net.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by yzd on 2017/9/16 0016.
 */

public interface DConverter<F, T> {

    T convert(F value) throws IOException;

    abstract class Factory {

        public DConverter<?, String> stringConverter(Type type, Annotation[] annotations) {
            return null;
        }
    }
}
