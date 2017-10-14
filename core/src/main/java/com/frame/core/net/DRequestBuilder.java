package com.frame.core.net;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

/**
 * Created by yzd on 2017/9/18 0018.
 */

public final class DRequestBuilder {

    private static final char[] HEX_DIGITS =
            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static final String PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#";

    private final String method;
    private final HttpUrl baseUrl;
    private final Request.Builder requestBuilder;
    private MediaType contentType;
    private final boolean hasBody;

    private String relativeUrl;
    private HttpUrl.Builder urlBuilder;
    private FormBody.Builder formBuilder;
    private RequestBody body;

    DRequestBuilder(String method, HttpUrl baseUrl, boolean hasBody, boolean isFormEncoded, String relativeUrl) {
        this.method = method;
        this.baseUrl = baseUrl;
        this.requestBuilder = new Request.Builder();
        this.hasBody = hasBody;
        this.relativeUrl = relativeUrl;
        if (isFormEncoded) {
            formBuilder = new FormBody.Builder();
        }
    }

    void setRelativeUrl(Object relativeUrl) {
        if (relativeUrl == null) throw new NullPointerException("@Url parameter is null.");
        this.relativeUrl = relativeUrl.toString();
    }

    void addHeader(String name, String value) {
        if ("Content-Type".equalsIgnoreCase(name)) {
            MediaType type = MediaType.parse(value);
            if (type == null) {
                throw new IllegalArgumentException("Malformed content type: " + value);
            }
            contentType = type;
        } else {
            requestBuilder.addHeader(name, value);
        }
    }

    void addPathParam(String name, String value, boolean encoded) {
        if (relativeUrl == null) {
            // The relative URL is cleared when the first query parameter is set.
            throw new AssertionError();
        }
        relativeUrl = relativeUrl.replace("{" + name + "}", canonicalizeForPath(value, encoded));
    }

    private static String canonicalizeForPath(String input, boolean alreadyEncoded) {
        int codePoint;
        for (int i = 0, limit = input.length(); i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (codePoint < 0x20 || codePoint >= 0x7f
                    || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(codePoint) != -1
                    || (!alreadyEncoded && (codePoint == '/' || codePoint == '%'))) {
                // Slow path: the character at i requires encoding!
                Buffer out = new Buffer();
                out.writeUtf8(input, 0, i);
                canonicalizeForPath(out, input, i, limit, alreadyEncoded);
                return out.readUtf8();
            }
        }

        // Fast path: no characters required encoding.
        return input;
    }

    private static void canonicalizeForPath(Buffer out, String input, int pos, int limit,
                                            boolean alreadyEncoded) {
        Buffer utf8Buffer = null; // Lazily allocated.
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (alreadyEncoded
                    && (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r')) {
                // Skip this character.
            } else if (codePoint < 0x20 || codePoint >= 0x7f
                    || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(codePoint) != -1
                    || (!alreadyEncoded && (codePoint == '/' || codePoint == '%'))) {
                // Percent encode this character.
                if (utf8Buffer == null) {
                    utf8Buffer = new Buffer();
                }
                utf8Buffer.writeUtf8CodePoint(codePoint);
                while (!utf8Buffer.exhausted()) {
                    int b = utf8Buffer.readByte() & 0xff;
                    out.writeByte('%');
                    out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
                    out.writeByte(HEX_DIGITS[b & 0xf]);
                }
            } else {
                // This character doesn't need encoding. Just copy it over.
                out.writeUtf8CodePoint(codePoint);
            }
        }
    }

    void addFormField(String name, String value, boolean encoded) {
        if (encoded) {
            formBuilder.addEncoded(name, value);
        } else {
            formBuilder.add(name, value);
        }
    }

    void addQueryParam(String name, String value, boolean encoded) {
        if (relativeUrl != null) {
            // Do a one-time combination of the built relative URL and the base URL.
            urlBuilder = baseUrl.newBuilder(relativeUrl);
            if (urlBuilder == null) {
                throw new IllegalArgumentException(
                        "Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
            }
            relativeUrl = null;
        }

        if (encoded) {
            urlBuilder.addEncodedQueryParameter(name, value);
        } else {
            urlBuilder.addQueryParameter(name, value);
        }
    }

    Request build() {
        HttpUrl url;
        HttpUrl.Builder urlBuilder = this.urlBuilder;
        if (this.urlBuilder != null) {
            url = urlBuilder.build();
        } else {
            url = baseUrl.resolve(relativeUrl);
            if (url == null) throw new IllegalArgumentException("Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
        }

        RequestBody body = this.body;
        if (formBuilder != null) {
            body = formBuilder.build();
        } else if (hasBody) {
            body = RequestBody.create(null, new byte[0]);
        }

        MediaType contentType = this.contentType;
        if (contentType != null) {
            if (body != null) {
                body = new ContentTypeOverridingRequestBody(body, contentType);
            } else {
                requestBuilder.addHeader("Content-Type", contentType.toString());
            }
        }

        return requestBuilder
                .url(url)
                .method(method, body)
                .build();
    }

    private static class ContentTypeOverridingRequestBody extends RequestBody {
        private final RequestBody delegate;
        private final MediaType contentType;

        ContentTypeOverridingRequestBody(RequestBody delegate, MediaType contentType) {
            this.delegate = delegate;
            this.contentType = contentType;
        }

        @Override public MediaType contentType() {
            return contentType;
        }

        @Override public long contentLength() throws IOException {
            return delegate.contentLength();
        }

        @Override public void writeTo(BufferedSink sink) throws IOException {
            delegate.writeTo(sink);
        }
    }
}
