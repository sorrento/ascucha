package com.example.android.mediasession;

import android.app.Application;

import com.example.android.mediasession.book.BookContability;
import com.example.android.mediasession.book.BookSummary;
import com.example.android.mediasession.book.Chapter;
import com.example.android.mediasession.book.palabraDiccionario;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Milenko on 03/08/2016.
 */
public class ascucha extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Chapter.class);
        ParseObject.registerSubclass(BookSummary.class);
        ParseObject.registerSubclass(BookContability.class);
        ParseObject.registerSubclass(palabraDiccionario.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(Constants.APP_ID)
                .clientKey(Constants.CLIENT_KEY)
                        .enableLocalDataStore()
                        .server("https://parseapi.back4app.com")
                        .build()
        );

    }
}
