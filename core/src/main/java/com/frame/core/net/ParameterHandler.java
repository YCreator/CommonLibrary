package com.frame.core.net;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;

/**
 * Created by yzd on 2017/9/16 0016.
 */

public abstract class ParameterHandler<T> {

    abstract void apply(FormBody.Builder builder, T value) throws IOException;

    static final class Field<T> extends ParameterHandler<T> {

        private final String name;
        private final Converter<T, String> valueConverter;
        private final boolean encoded;

        public Field(String name, Converter<T, String> valueConverter, boolean encoded) {
            this.name = name;
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(FormBody.Builder builder, T value) throws IOException {
            if (encoded) {
                builder.addEncoded(name, valueConverter.convert(value));
            } else {
                builder.add(name, valueConverter.convert(value));
            }
        }
    }

    static final class FieldMap<T> extends ParameterHandler<Map<String, T>> {

        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        public FieldMap(Converter<T, String> valueConverter, boolean encoded) {
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(FormBody.Builder builder, Map<String, T> value) throws IOException {
            for (String key : value.keySet()) {
                T v = value.get(key);
                if (encoded) {
                    builder.addEncoded(key, valueConverter.convert(v));
                } else {
                    builder.add(key, valueConverter.convert(v));
                }
            }
        }
    }
}
