package com.example.android.mediasession.book;

public interface CSEvents {
    void serviceReady();

    void bookEnded();

    void error(String text, Exception e);
}
