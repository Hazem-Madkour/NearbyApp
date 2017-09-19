package com.cognitev.nearbyapp.Interfaces;

public abstract class ILoadObject<T> {

    public void onInitialize() {
    }

    public void anotherCall(String callTitle) {
    }

    public abstract void onSuccess(T loadedObject);

    public abstract void onFail(String errorMessage);

    public void onFinish() {

    }
}
