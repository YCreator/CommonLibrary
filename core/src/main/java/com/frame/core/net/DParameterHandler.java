package com.frame.core.net;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yzd on 2017/9/16 0016.
 */

public abstract class DParameterHandler<T> {

    abstract void apply(DRequestBuilder builder, T value) throws IOException;

    static final class Field<T> extends DParameterHandler<T> {

        private final String name;
        private final DConverter<T, String> valueDConverter;
        private final boolean encoded;

        Field(String name, DConverter<T, String> valueDConverter, boolean encoded) {
            this.name = name;
            this.valueDConverter = valueDConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(DRequestBuilder builder, T value) throws IOException {
            builder.addFormField(name, valueDConverter.convert(value), encoded);
        }
    }

    static final class FieldMap<T> extends DParameterHandler<Map<String, T>> {

        private final boolean encoded;
        private final DConverter<T, String> valueDConverter;

        FieldMap(DConverter<T, String> valueDConverter, boolean encoded) {
            this.valueDConverter = valueDConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(DRequestBuilder builder, Map<String, T> value) throws IOException {
            for (String key : value.keySet()) {
                builder.addFormField(key, valueDConverter.convert(value.get(key)), encoded);
            }
        }
    }

    static final class Query<T> extends DParameterHandler<T> {

        private final String name;
        private final DConverter<T, String> valueDConverter;
        private final boolean encoded;

        Query(String name, DConverter<T, String> valueDConverter, boolean encoded) {
            this.name = name;
            this.valueDConverter = valueDConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(DRequestBuilder builder, T value) throws IOException {
            builder.addQueryParam(name, valueDConverter.convert(value), encoded);
        }
    }

    static final class QueryMap<T> extends DParameterHandler<Map<String, T>> {

        private final boolean encoded;
        private final DConverter<T, String> valueDConverter;

        QueryMap(DConverter<T, String> valueDConverter, boolean encoded) {
            this.encoded = encoded;
            this.valueDConverter = valueDConverter;
        }

        @Override
        void apply(DRequestBuilder builder, Map<String, T> value) throws IOException {
            for (String key : value.keySet()) {
                builder.addQueryParam(key, valueDConverter.convert(value.get(key)), encoded);
            }
        }
    }

    static final class RelativeUrl extends DParameterHandler<Object> {

        @Override
        void apply(DRequestBuilder builder, Object value) throws IOException {
            builder.setRelativeUrl(value);
        }
    }

    static final class Path<T> extends DParameterHandler<T> {

        private final String name;
        private final DConverter<T, String> valueDConverter;
        private final boolean encoded;

        Path(String name, DConverter<T, String> valueDConverter, boolean encoded) {
            this.name = name;
            this.valueDConverter = valueDConverter;
            this.encoded = encoded;
        }

        @Override
        void apply(DRequestBuilder builder, T value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException(
                        "Path parameter \"" + name + "\" value must not be null.");
            }
            builder.addPathParam(name, valueDConverter.convert(value), encoded);
        }
    }

    static final class Header<T> extends DParameterHandler<T> {
        private final String name;
        private final DConverter<T, String> valueConverter;

        Header(String name, DConverter<T, String> valueConverter) {
            this.name = name;
            this.valueConverter = valueConverter;
        }

        @Override
        void apply(DRequestBuilder builder, T value) throws IOException {
            if (value == null) return; // Skip null values.
            builder.addHeader(name, valueConverter.convert(value));
        }
    }
}
