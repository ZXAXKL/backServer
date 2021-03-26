package com.graduation.common.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class FastJsonMessageConverter extends AbstractMessageConverter {

    //message转换为Json
    public static final String DEFAULT_CHARSET = "UTF-8";

    private volatile String defaultCharset = DEFAULT_CHARSET;

    public FastJsonMessageConverter() {
        super();
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = (defaultCharset != null) ? defaultCharset : DEFAULT_CHARSET;
    }

    @Override
    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes = null;
        try {
            String jsonString = JSONObject.toJSONString(objectToConvert);
            bytes = jsonString.getBytes(this.defaultCharset);
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert Message content", e);
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(this.defaultCharset);
        messageProperties.setContentLength(bytes.length);
        return new Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(Message message)	throws MessageConversionException {
        return JSONObject.parse(new String(message.getBody(), StandardCharsets.UTF_8));
    }
}
