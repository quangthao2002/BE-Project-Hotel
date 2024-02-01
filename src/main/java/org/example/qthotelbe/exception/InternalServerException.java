package org.example.qthotelbe.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);// goi lai ham khoi tao cua lop cha
    }
}
