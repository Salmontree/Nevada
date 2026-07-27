package com.ottertree.nevada.data;

public class NevadaException extends Exception {
    private final String errorMsg;
    
    public NevadaException(String message) {
        super(message);
        errorMsg = message;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }
}
