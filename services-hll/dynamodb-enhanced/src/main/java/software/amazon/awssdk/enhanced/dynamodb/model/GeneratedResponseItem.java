package software.amazon.awssdk.enhanced.dynamodb.model;

import java.util.Collection;
import java.util.Map;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.converter.ItemAttributeValueConverter;
import software.amazon.awssdk.enhanced.dynamodb.internal.model.DefaultGeneratedResponseItem;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * A user-friendly {@code Map<String, AttributeValue>} that represents a response item from DynamoDB.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public interface GeneratedResponseItem extends AttributeAware<AttributeValue>,
                                               ConverterAware,
                                               ToCopyableBuilder<GeneratedResponseItem.Builder, GeneratedResponseItem> {
    /**
     * Create a builder for configuring and creating a {@link GeneratedResponseItem}.
     */
    static Builder builder() {
        return DefaultGeneratedResponseItem.builder();
    }

    /**
     * Convert this response item into a {@link ResponseItem} using the converters attached to this item, only.
     *
     * This will not use the default converter chain or any converters associated with the client unless they were explicitly
     * added via {@link Builder#addConverter(ItemAttributeValueConverter)} (or similar methods).
     */
    ResponseItem toResponseItem();

    /**
     * A builder for configuring and creating a {@link GeneratedResponseItem}.
     */
    interface Builder extends AttributeAware.Builder<AttributeValue>,
                              ConverterAware.Builder ,
                              CopyableBuilder<Builder, GeneratedResponseItem> {
        @Override
        Builder putAttributes(Map<String, AttributeValue> attributeValues);

        @Override
        Builder putAttribute(String attributeKey, AttributeValue attributeValue);

        @Override
        Builder removeAttribute(String attributeKey);

        @Override
        Builder clearAttributes();

        @Override
        Builder addConverters(Collection<? extends ItemAttributeValueConverter> converters);

        @Override
        Builder addConverter(ItemAttributeValueConverter converter);

        @Override
        Builder clearConverters();

        @Override
        GeneratedResponseItem build();
    }
}