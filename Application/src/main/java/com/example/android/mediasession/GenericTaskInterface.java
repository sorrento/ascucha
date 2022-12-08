package com.example.android.mediasession;

public interface GenericTaskInterface {
    void onDone();

    void onError(String text, Exception e);
}
