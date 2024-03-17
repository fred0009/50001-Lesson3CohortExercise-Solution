package com.example.norman_lee.comicapp.utils;

public class Container<T> {
    T value;

    public Container() {
        this.value = null;
    }

    public void set(T t) {
        this.value = t;
    }

    public T get() {
        return this.value;
    }
}