package org.hazehorizon.restaurantPicker.common.rest;

import java.util.Arrays;
import java.util.List;

public class ResponseWrapper<T> {
    public static final String SUCCESS = "SUCCESS";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String GENERAL_ERROR = "GENERAL_ERROR";

    private String code;
    private List<String> messages;
    private T data;

    public ResponseWrapper()
    {
    }

    public ResponseWrapper(String code, String... messages)
    {
        this.code = code;
        this.messages = Arrays.asList(messages);
    }

    public ResponseWrapper(T data)
    {
        this.code = SUCCESS;
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

	public String getCode() {
		return code;
	}

	public List<String> getMessages() {
		return messages;
	}
}
