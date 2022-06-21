package com.juliusbaer.itasia.crm.reconstruct;

@SuppressWarnings("serial")
public class CRMException extends RuntimeException {

    public CRMException(String message) {
        super(message);
    }

    public CRMException(String message, Throwable cause) {
        super(message, cause);
    }
}
