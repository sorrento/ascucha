package com.example.android.mediasession;

import android.content.Context;
import android.content.Intent;

import com.example.android.mediasession.book.Book;
import com.example.android.mediasession.book.BookCallIdback;
import com.example.android.mediasession.book.ParseHelper;
import com.example.android.mediasession.ui.MainActivity;


public class Lector {
    private final       Preferences           prefs;
    private             ReaderEvents          readerEvents;
    private             Voice                 voice;
    private             String                tag           = "LEC";
    private Book book;
    private             String                mPathMp3;

    public Lector(Context context, final Preferences myPrefs) {

        prefs = myPrefs;
//        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        readerEvents = new ReaderEvents() {
            @Override
            public void bookAndVoiceReady() {
                if (prefs.getFirstTime()) {
                    welcomeMessage();
                    prefs.setFirstTime(false);
                }

                String newLanguage = book.getLanguage();
                if (!prefs.getLanguage().equals(newLanguage)) {
                    voice.setLanguage(newLanguage);
                    prefs.setLanguage(newLanguage);
                }

//                localBroadcastManager.sendBroadcast(new Intent(MainActivity.Oreja.ACTION_OBTIENE_SUMMARY));

                // si es retomar el libro
                if (book.getCurrentChapterId() != 1)
                    voice.predefinedPhrases(TipoFrase.DONDE_ME_QUEDE_RETOMAR, true);

                speakCurrentChapter();
            }

            @Override
            public void voiceStartedSpeakChapter() {
//                localBroadcastManager.sendBroadcast(new Intent(MainActivity.Oreja.ACTION_STARTED_READING_CHAPTER));
            }

            @Override
            public void voiceInterrupted() {
//                localBroadcastManager.sendBroadcast(new Intent(MainActivity.Oreja.ACTION_STOPPED_READING_CHAP));
            }

            @Override
            public void voiceEndedReadingChapter() {
                //book.speakNextChapter();
                voice.speakChapter(book.getNextChapter(), false);
            }

            @Override
            public void bookEnded() {
                voice.predefinedPhrases(TipoFrase.FINALIZADO_LIBRO_ENTERO, true);
                prefs.addEnded(book.getBookId());
                accionCambiaDeLibro(false);
            }

            @Override
            public void txt2fileBunchProcessed() {
            }

            @Override
            public void txt2fileOneFileWritten(int i) {
//                Intent intent = new Intent(MainActivity.Oreja.ACTION_MP3FILEWRITTEN);
//                intent.putExtra("chapter", i);
//                intent.putExtra("total", book.getBookSummary().getNChapters());
//                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void error(String text, Exception e) {
                speakDeveloper(text);
                myLog.error(text, e);
            }
        };

        book = new Book(myPrefs.getReadingBookId(), myPrefs.getReadingChapterId(), myPrefs.isLocalStorage(),
                myPrefs, readerEvents);
        voice = new Voice(prefs.getLanguage(), context, readerEvents);
    }

    public void speakCurrentChapter() {
        voice.speakChapter(book.getCurrentChapter(), false);
    }

    private void welcomeMessage() {
        voice.predefinedPhrases(TipoFrase.WELCOME, true);
    }

    public void accionStopReading() {
        boolean speakingChapter = voice.isSpeakingChapter();

        myLog.add("me mandan a stopreading. estámhablando chapter?" + speakingChapter, tag);
        if (speakingChapter) {
            voice.shutUp();
        }
    }

    private void speakDeveloper(String s) {
        voice.speakDeveloperMsg(s);
    }

    public void leeDesdePrincipio() {
        voice.shutUp();
        voice.predefinedPhrases(TipoFrase.TE_GUSTA_VAMOS_PRINCIPIO, true);

        book.setCurrentChapterId(1);
    }

    public void shutUp() {
        voice.shutUp();
    }

    public void shutdownVoice() {
        voice.shutdown();
    }

    public boolean isReading() {
        return voice.isSpeakingChapter();
    }

    public void accionSaltaACapitulo(int bookId, final int chapId) {
        voice.predefinedPhrases(TipoFrase.VAMOS_ALLA, true);

        // Es mismo libro?
        if (bookId == book.getBookId()) {
            book.setCurrentChapterId(chapId);
            // Es otro libro
        } else {
            book = new Book(bookId, chapId, false, prefs, readerEvents);
        }
    }

    public void accionCambiaDeLibro(boolean prematuro) {
        if (prematuro) voice.predefinedPhrases(TipoFrase.NO_TE_GUSTA_VEAMOS_OTRO, true);
        ParseHelper.getRandomAllowedBookId(prefs.getSkipeables(), new BookCallIdback() {
            @Override
            public void onDone(int bookId, int nDisponibles) {
                prefs.setDisponibles(nDisponibles);
                book = new Book(bookId, -1, false, prefs, readerEvents);
            }
        });
    }

    public void definePalabrasDelChapter() {
        String text = getCurrentChapterText();

//        Definator definator = new Definator(text, "ES", new ArrayCallback() {
//            @Override
//            public void onDone(List<String> arr, List<Double> bestScores) {
//                myLog.add("llegaron definiciones:" + arr, tag);
//                for (String s : arr) {
//                    voice.speakDefinition(s);
//                }
//            }
//        });
    }


    ////////////////////// OLD

    public String getStorageType() {
        return prefs.isLocalStorage() ? "LOCAL" : "WEB";
    }

    public void setLikedCurrentBook(boolean b) {
        if (!b) {
            prefs.addHated(book.getBookId());
        }
    }

    public Book getBook() {
        return book;
    }

    private String getCurrentChapterText() {
        return book.getCurrentChapter().getText();
    }

}
