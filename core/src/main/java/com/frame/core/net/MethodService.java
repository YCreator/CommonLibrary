package com.frame.core.net;

import com.frame.core.interf.http.Field;
import com.frame.core.interf.http.FieldMap;
import com.frame.core.interf.http.GET;
import com.frame.core.interf.http.POST;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by yzd on 2017/9/15 0015.
 */

public final class MethodService {

    private Request.Builder builder;
    private final String httpMethod;
    private final String relativeUrl;
    private final boolean hasBody;
    private final ParameterHandler<?>[] parameterHandlers;

    MethodService(Builder builder) {
        this.builder = new Request.Builder();
        this.builder.url(builder.requestHelper.mBaseUrl.resolve(builder.relativeUrl));
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHandlers;
        this.hasBody = builder.hasBody;
    }

    public Request toRequest(Object... args) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        @SuppressWarnings("unchecked")
        ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;
        int argumentCount = handlers.length;
        for (int p = 0; p < argumentCount; p++) {
            handlers[p].apply(formBuilder, args[p]);
        }
        builder.method(httpMethod, formBuilder.build());
        return builder.build();
    }

    static final class Builder {
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;
        final RequestHelper requestHelper;

        String httpMethod;
        String relativeUrl;
        boolean hasBody;
        ParameterHandler<?>[] parameterHandlers;


        public Builder(Method method, RequestHelper requestHelper) {
            this.method = method;
            this.requestHelper = requestHelper;
            methodAnnotations = method.getAnnotations();
            parameterAnnotationsArray = method.getParameterAnnotations();
            parameterTypes = method.getParameterTypes();
        }

        public MethodService build() {
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            int parameterCount = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                Type parameterType = parameterTypes[i];
                Annotation[] parameterAnnotations = parameterAnnotationsArray[i];
                parameterHandlers[i] = parseParameter(parameterType, parameterAnnotations);
            }
            return new MethodService(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
            } else if (annotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            this.httpMethod = httpMethod;
            this.relativeUrl = value;
            this.hasBody = hasBody;
        }

        private ParameterHandler<?> parseParameter(Type parameterType, Annotation[] annotations) {
            ParameterHandler<?> result = null;
            for (Annotation annotation : annotations) {
                ParameterHandler<?> annotationAction = parseParameterAnnotation(parameterType, annotations, annotation);
                if (annotationAction == null) {
                    continue;
                }
                result = annotationAction;
            }
            return result;
        }

        private ParameterHandler<?> parseParameterAnnotation(Type type, Annotation[] annotations, Annotation annotation) {
            if (annotation instanceof Field) {
                Converter<?, String> converter = requestHelper.converterFactory.stringConverter(type, annotations);
                return new ParameterHandler.Field<>(((Field) annotation).value(), converter, ((Field) annotation).encoded());
            } else if (annotation instanceof FieldMap) {
                Converter<?, String> converter = BuiltConverters.ToMapStringConverter.TOMAPSTRING;
                return new ParameterHandler.FieldMap<>(converter, ((FieldMap) annotation).encoded());
            }
            return null;
        }

    }
}
