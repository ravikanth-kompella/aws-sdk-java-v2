/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.core.runtime.transform;

import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.http.SdkHttpFullRequest;

/**
 * Augments a {@link Marshaller} to add contents for an async streamed request.
 *
 * @param <T> Type of POJO being marshalled.
 */
@SdkProtectedApi
public final class AsyncStreamingRequestMarshaller<T> implements Marshaller<T> {

    private final Marshaller<T> delegateMarshaller;
    private final AsyncRequestBody asyncRequestBody;
    private final boolean requiresLength;
    private final boolean transferEncoding;
    private final boolean useHttp2;

    private AsyncStreamingRequestMarshaller(Builder builder) {
        this.delegateMarshaller = builder.delegateMarshaller;
        this.asyncRequestBody = builder.asyncRequestBody;
        this.requiresLength = builder.requiresLength;
        this.transferEncoding = builder.transferEncoding;
        this.useHttp2 = builder.useHttp2;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SdkHttpFullRequest marshall(T in) {
        SdkHttpFullRequest.Builder marshalled = delegateMarshaller.marshall(in).toBuilder();

        StreamingMarshallerUtil.addHeaders(marshalled, asyncRequestBody.contentLength(), requiresLength,
                                           transferEncoding, useHttp2);

        return marshalled.build();
    }

    /**
     * Builder class to build {@link AsyncStreamingRequestMarshaller} object.
     */
    public static final class Builder {
        private Marshaller delegateMarshaller;
        private AsyncRequestBody asyncRequestBody;
        private boolean requiresLength = Boolean.FALSE;
        private boolean transferEncoding = Boolean.FALSE;
        private boolean useHttp2 = Boolean.FALSE;

        private Builder() {
        }

        /**
         * @param delegateMarshaller POJO marshaller (for path/query/header members)
         * @return This object for method chaining
         */
        public Builder delegateMarshaller(Marshaller delegateMarshaller) {
            this.delegateMarshaller = delegateMarshaller;
            return this;
        }

        /**
         * @param asyncRequestBody {@link AsyncRequestBody} representing the HTTP payload
         * @return This object for method chaining
         */
        public Builder asyncRequestBody(AsyncRequestBody asyncRequestBody) {
            this.asyncRequestBody = asyncRequestBody;
            return this;
        }

        /**
         * @param requiresLength boolean value indicating if Content-Length header is required in the request
         * @return This object for method chaining
         */
        public Builder requiresLength(boolean requiresLength) {
            this.requiresLength = requiresLength;
            return this;
        }

        /**
         * @param transferEncoding boolean value indicating if Transfer-Encoding: chunked header is required in the request
         * @return This object for method chaining
         */
        public Builder transferEncoding(boolean transferEncoding) {
            this.transferEncoding = transferEncoding;
            return this;
        }

        /**
         * @param useHttp2 boolean value indicating if request uses HTTP 2 protocol
         * @return This object for method chaining
         */
        public Builder useHttp2(boolean useHttp2) {
            this.useHttp2 = useHttp2;
            return this;
        }

        public <T> AsyncStreamingRequestMarshaller<T> build() {
            return new AsyncStreamingRequestMarshaller<>(this);
        }
    }
}
