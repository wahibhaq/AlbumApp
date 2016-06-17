package com.deliveryhero.deliveryherotest.utils;

/**
 * Created by Hamza_MACB105 on 16/06/16.
 */
public class ApiFailEvent {

    private final String failText;

    public ApiFailEvent(String failText) {
        this.failText = failText;
    }

    public String getFailText() {
        return failText;
    }
}
