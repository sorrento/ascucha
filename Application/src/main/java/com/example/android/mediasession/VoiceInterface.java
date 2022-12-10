package com.example.android.mediasession;

import com.example.android.mediasession.book.Chapter;
interface VoiceInterface {

    void setLanguage(String newLan);

    void speakChapter(Chapter chapter, boolean interrumpir);

    void shutUp();

    void predefinedPhrases(TipoFrase tipoFrase, boolean interrupt);

}
