package com.example.android.mediasession.book;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.android.mediasession.Constants;
import com.example.android.mediasession.service.contentcatalogs.MusicLibrary;
import com.example.android.mediasession.text;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Milenko on 04/08/2016.
 */

@ParseClassName("libros")
public class Chapter extends ParseObject {

    private int mMaxChap;

    public Chapter() {
    }

    public String getAuthor() {
        return getString("autor");
    }

    public String getBookName() {
        return getString("titulo");
    }

    private int getBookId() {
        return getInt("nLibro");
    }

    public int getChapterId() {
        return getInt("nCapitulo");
    }

//    public String getLanguage() {
//        String language = getString("language");
//        if (language == null || language.equals("")) language = "ES";
//
//        return language;
//    }

    public boolean isSong() {
        return getBoolean("isSong");
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "autor='" + getAuthor() + '\'' +
                ", titulo='" + getBookName() + '\'' +
                ", nLibro=" + getBookId() +
                ", nCapitulo=" + getChapterId() +
//                ", texto='" + texto + '\'' +
                '}';
    }

    public String getText() {
        String texto;

        if (Constants.debugMode) texto = text.shortenText(getString("texto"), 150);
        else texto = getString("texto");
//        Log.d("tag2", texto);

        return texto;
    }

    /**
     * Text prepared to be read, for instance, removing "guiones"
     */
    public String getProcessedText() {
        return text.processForReading(getText());
    }

    private String shortDescription() {
        final int i = 40;

        return shortestDescription() + " - [" +
                text.shortenText(getText(), i).replaceAll("\n", " ") + "]";
    }

    private String shortestDescription() {
        return getBookName() + "(" + getChapterId() + "|" + getBookId() + ")";
    }

    public String getUtterance() {
        return Constants.PREFIX_OF_CHAPTER_UTTERANCE + shortDescription();
    }

    /**
     * Corta el texto al máximo que acepta el reproductor de tts
     *
     * @return
     */
    public String getTextTrimmed() {
        String s = getText();
        String s1;

        int endIndex = TextToSpeech.getMaxSpeechInputLength() - 10;

        s1 = s.length() > endIndex ? s.substring(0, endIndex) : s;
        return s1;
    }

    public void createMetadata() {
        MusicLibrary.createMediaMetadataCompat(this);
    }

    public long getDuration() {
        long n = countWordsUsingSplit(this.getText());
        return n;
    }

    private static int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    public String getTitle() {
        int len = 25;
        String s = this.getText();
        String max = String.valueOf(mMaxChap);

        return this.getChapterId() + "/" + max + " | " + s.substring(0, len) + "...";
    }

    public void setMaxChap(int chapterId) {
        mMaxChap = chapterId;
    }
}
