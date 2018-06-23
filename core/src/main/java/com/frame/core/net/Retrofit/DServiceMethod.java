package com.frame.core.net.Retrofit;

import com.frame.core.net.Retrofit.http.DBody;
import com.frame.core.net.Retrofit.http.DField;
import com.frame.core.net.Retrofit.http.DFieldMap;
import com.frame.core.net.Retrofit.http.DFormUrlEncoded;
import com.frame.core.net.Retrofit.http.DGET;
import com.frame.core.net.Retrofit.http.DHEAD;
import com.frame.core.net.Retrofit.http.DHeader;
import com.frame.core.net.Retrofit.http.DMultipart;
import com.frame.core.net.Retrofit.http.DPOST;
import com.frame.core.net.Retrofit.http.DPart;
import com.frame.core.net.Retrofit.http.DPath;
import com.frame.core.net.Retrofit.http.DQuery;
import com.frame.core.net.Retrofit.http.DQueryMap;
import com.frame.core.net.Retrofit.http.DUrl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by yzd on 2017/9/15 0015.
 */

public final class DServiceMethod {

    private final String httpMethod;
    private final String relativeUrl;
    private final boolean hasBody;
    private final boolean isFormEncoded;
    private final HttpUrl baseUrl;
    private final DParameterHandler<?>[] DParameterHandlers;

    DServiceMethod(Builder builder) {
        this.baseUrl = builder.requestHelper.mBaseUrl;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.DParameterHandlers = builder.DParameterHandlers;
        this.hasBody = builder.hasBody;
        this.isFormEncoded = builder.isFormEncoded;
    }

    public Request toRequest(Object... args) throws IOException {
        DRequestBuilder DRequestBuilder = new DRequestBuilder(httpMethod, baseUrl, hasBody, isFormEncoded, relativeUrl);
        @SuppressWarnings("unchecked")
        DParameterHandler<Object>[] handlers = (DParameterHandler<Object>[]) DParameterHandlers;
        int argumentCount = handlers.length;
        for (int p = 0; p < argumentCount; p++) {
            handlers[p].apply(DRequestBuilder, args[p]);
        }
        return DRequestBuilder.build();
    }

    static final class Builder {
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;
        final RequestHelper requestHelper;

        boolean gotField;
        boolean gotPart;
        boolean gotBody;
        boolean gotPath;
        boolean gotQuery;
        boolean gotUrl;
        String httpMethod;
        String relativeUrl;
        boolean hasBody;
        boolean isFormEncoded;
        boolean isMultipart;
        DParameterHandler<?>[] DParameterHandlers;


        public Builder(Method method, RequestHelper requestHelper) {
            this.method = method;
            this.requestHelper = requestHelper;
            methodAnnotations = method.getAnnotations();
            parameterAnnotationsArray = method.getParameterAnnotations();
            parameterTypes = method.getParameterTypes();
        }

        public DServiceMethod build() {
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            if (httpMethod == null)
                throw new IllegalArgumentException("HTTP method annotation is required (e.g., @GET, @POST, etc.).");
            if (!hasBody) {
                if (isMultipart)
                    throw new IllegalArgumentException("Multipart can only be specified on HTTP methods with request body (e.g., @POST).");
                if (isFormEncoded)
                    throw new IllegalArgumentException("FormUrlEncoded can only be specified on HTTP methods with "
                            + "request body (e.g., @POST).");
            }

            int parameterCount = parameterAnnotationsArray.length;
            DParameterHandlers = new DParameterHandler[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                Type parameterType = parameterTypes[i];
                Annotation[] parameterAnnotations = parameterAnnotationsArray[i];
                DParameterHandlers[i] = parseParameter(parameterType, parameterAnnotations);
            }

            if (relativeUrl == null && !gotUrl)
                throw new IllegalArgumentException("Missing either @%s URL or @Url parameter.");
            if (!isFormEncoded && !isMultipart && !hasBody && gotBody)
                throw new IllegalArgumentException("Non-body HTTP method cannot contain @Body.");
            if (isFormEncoded && !gotField)
                throw new IllegalArgumentException("Form-encoded method must contain at least one @Field.");
            if (isMultipart && !gotPart)
                throw new IllegalArgumentException("Multipart method must contain at least one @Part.");

            return new DServiceMethod(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof DPOST) {
                parseHttpMethodAndPath("POST", ((DPOST) annotation).value(), true);
            } else if (annotation instanceof DGET) {
                parseHttpMethodAndPath("GET", ((DGET) annotation).value(), false);
            } else if (annotation instanceof DHEAD) {
                parseHttpMethodAndPath("HEAD", ((DHEAD) annotation).value(), false);
            } else if (annotation instanceof DFormUrlEncoded) {
                if (isMultipart)
                    throw new IllegalArgumentException("Only one encoding annotation is allowed.");
                isFormEncoded = true;
            } else if (annotation instanceof DMultipart) {
                if (isFormEncoded)
                    throw new IllegalArgumentException("Only one encoding annotation is allowed.");
                isMultipart = true;
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            if (this.httpMethod != null)
                throw new IllegalArgumentException(String.format("Only one HTTP method is allowed. Found: %s and %s.", this.httpMethod, httpMethod));

            this.httpMethod = httpMethod;
            this.relativeUrl = value;
            this.hasBody = hasBody;

        }

        private DParameterHandler<?> parseParameter(Type parameterType, Annotation[] annotations) {
            DParameterHandler<?> result = null;
            for (Annotation annotation : annotations) {
                DParameterHandler<?> annotationAction = parseParameterAnnotation(parameterType, annotations, annotation);
                if (annotationAction == null) {
                    continue;
                }
                if (result != null) {
                    throw new IllegalArgumentException("Multiple Retrofit annotations found, only one allowed.");
                }
                result = annotationAction;
            }
            if (result == null) {
                throw new IllegalArgumentException("No annotation found.");
            }
            return result;
        }

        private DParameterHandler<?> parseParameterAnnotation(Type type, Annotation[] annotations, Annotation annotation) {
            if (annotation instanceof DField) {
                if (!isFormEncoded)
                    throw new IllegalArgumentException("@DField parameters can only be used with form encoding.");
                DField field = (DField) annotation;
                String name = field.value();
                boolean encoded = field.encoded();
                gotField = true;
                DConverter<?, String> dConverter = requestHelper.converterFactory.stringConverter(type, annotations);
                return new DParameterHandler.Field<>(name, dConverter, encoded);
            } else if (annotation instanceof DFieldMap) {
                if (!isFormEncoded)
                    throw new IllegalArgumentException("@DField parameters can only be used with form encoding.");
                Class<?> rawParameterType = DUtils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType))
                    throw new IllegalArgumentException("@DFieldMap parameter type must be Map.");
                gotField = true;
                DConverter<?, String> dConverter = DBuiltConverters.ToMapStringConverter.TOMAPSTRING;
                return new DParameterHandler.FieldMap<>(dConverter, ((DFieldMap) annotation).encoded());
            } else if (annotation instanceof DQuery) {
                DQuery query = (DQuery) annotation;
                String name = query.value();
                boolean encoded = query.encoded();
                gotQuery = true;
                DConverter<?, String> dConverter = requestHelper.converterFactory.stringConverter(type, annotations);
                return new DParameterHandler.Query<>(name, dConverter, encoded);
            } else if (annotation instanceof DQueryMap) {
                Class<?> rawParameterType = DUtils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType))
                    throw new IllegalArgumentException("@DQueryMap parameter type must be Map.");
                gotQuery = true;
                DConverter<?, String> dConverter = DBuiltConverters.ToMapStringConverter.TOMAPSTRING;
                return new DParameterHandler.QueryMap<>(dConverter, ((DQueryMap) annotation).encoded());
            } else if (annotation instanceof DUrl) {
                if (gotUrl)
                    throw new IllegalArgumentException("Multiple @DUrl method annotations found.");
                if (gotPath)
                    throw new IllegalArgumentException("@DPath parameters may not be used with @DUrl.");
                if (gotQuery)
                    throw new IllegalArgumentException("A @DUrl parameter must not come after a @DQuery");
                if (relativeUrl != null)
                    throw new IllegalArgumentException(String.format("@DUrl cannot be used with @D%s URL", httpMethod));

                gotUrl = true;
                if (type == HttpUrl.class
                        || type == String.class
                        || type == URI.class
                        || (type instanceof Class && "android.net.Uri".equals(((Class<?>) type).getName()))) {
                    return new DParameterHandler.RelativeUrl();
                } else {
                    throw new IllegalArgumentException("@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.");
                }
            } else if (annotation instanceof DHeader) {
                DHeader header = (DHeader) annotation;
                DConverter<?, String> dConverter = requestHelper.converterFactory.stringConverter(type, annotations);
                return new DParameterHandler.Header<>(header.value(), dConverter);
            } else if (annotation instanceof DPath) {
                if (gotQuery)
                    throw new IllegalArgumentException("A @DPath parameter must not come after a @DQuery.");
                if (gotUrl)
                    throw new IllegalArgumentException("@DPath parameters may not be used with @DUrl.");
                if (relativeUrl == null)
                    throw new IllegalArgumentException(String.format("@DPath can only be used with relative url on @D%s", httpMethod));

                DPath path = (DPath) annotation;
                String name = path.value();
                boolean encoded = path.encoded();
                gotPath = true;
                DConverter<?, String> dConverter = requestHelper.converterFactory.stringConverter(type, annotations);
                return new DParameterHandler.Path<>(name, dConverter, encoded);
            } else if (annotation instanceof DPart) {
                if (!isMultipart)
                    throw new IllegalArgumentException("@DPart parameters can only be used with multipart encoding.");
                DPart part = (DPart) annotation;
                gotPart = true;
            } else if (annotation instanceof DBody) {
                gotBody = true;
            }
            return null;
        }

    }
}
