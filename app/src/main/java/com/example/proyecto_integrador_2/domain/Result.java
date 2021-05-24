package com.example.proyecto_integrador_2.domain;

public class Result<T> {

    private T value;
    private Throwable error;

    public Result(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public Throwable getError() {
        return error;
    }
}
