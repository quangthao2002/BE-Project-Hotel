package org.example.qthotelbe.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);// goi lai ham khoi tao cua lop cha
    }
}
