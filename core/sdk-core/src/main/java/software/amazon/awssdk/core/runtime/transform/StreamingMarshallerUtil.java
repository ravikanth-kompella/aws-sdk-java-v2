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

import static software.amazon.awssdk.http.Header.CONTENT_LENGTH;
import static software.amazon.awssdk.http.Header.TRANSFER_ENCODING;
import static software.amazon.awssdk.http.HeaderValue.CHUNKED;

import java.util.Optional;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.http.SdkHttpFullRequest;

@SdkInternalApi
final class StreamingMarshallerUtil {

    private StreamingMarshallerUtil() {
    }

    /**
     * This method will run certain validations for content-length and add
     * additional headers (like Transfer-Encoding) if necessary.
     *
     * If requiresLength and transferEncoding is not set to true and Content Length is missing,
     * SDK is not required to calculate the Content-Length and delegate that behavior to the underlying http client.
     *
     * @param marshalled A mutable builder for {@link SdkHttpFullRequest} representing a HTTP request.
     */
    static void addHeaders(SdkHttpFullRequest.Builder marshalled,
                           Optional<Long> contentLength,
                           boolean requiresLength,
                           boolean transferEncoding,
                           boolean useHttp2) {
        if (contentLength.isPresent()) {
            marshalled.putHeader(CONTENT_LENGTH, String.valueOf(contentLength));
            return;
        }

        if (requiresLength) {
            throw SdkClientException.create("This API requires Content-Length header to be set. "
                                            + "Please set the content length on the RequestBody.");
        } else if (transferEncoding && !useHttp2) {
            marshalled.putHeader(TRANSFER_ENCODING, CHUNKED);
        }
    }

}
