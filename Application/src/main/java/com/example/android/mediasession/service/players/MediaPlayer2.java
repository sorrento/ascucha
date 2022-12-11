package com.example.android.mediasession.service.players;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.example.android.mediasession.Constants;
import com.example.android.mediasession.myLog;

import java.io.IOException;

public class MediaPlayer2 extends MediaPlayer {
    TextToSpeech mTts;
    String mText;
    String tag = "MEDIAPLAY2";
    boolean isSpeakingChapter = true;
    boolean forzadoACallar = true;

    // todo ¿cómo sabe el mediaplayer normal qué canción tocar? ah, set file
    public MediaPlayer2(Context context) {
        Log.d(tag, "en constructor");
        TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d(tag, "on tts init"+ status);
                if (status != TextToSpeech.ERROR) {

                    String mCurrentLanguage = "ES";
                    Speak.setSpeakLanguage(mTts, mCurrentLanguage);
                    mTts.setOnUtteranceProgressListener(new uListener());
                    Log.d(tag, "inicializado el tts");
//                readerEvents.voiceReady();
//                super.
                }else{
                    Log.d(tag, "status de error al inicializar tts");
                }
            }
        };

        mTts = new TextToSpeech(context, onInitListener);
        Log.d(tag, "en constructor fin");
//        super.
    }

    @Override
    public void start() throws IllegalStateException {
//        super.start();
        Log.d(tag, "vamos a hablar");
        mTts.speak(mText, TextToSpeech.QUEUE_FLUSH, null, "utt");
        Log.d(tag, "hemos empezado");
    }

    @Override
    public void pause() throws IllegalStateException {
//        super.pause();
        mTts.stop();
    }

    @Override
    public boolean isPlaying() {
        Log.d(tag, "isPlaying()");
//        boolean playing = super.isPlaying();
        boolean playing;
        try {
//            playing=false;
            playing = mTts.isSpeaking();
        } catch (Exception e) {
            playing = false;
        }

        return playing;
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        Log.d(tag, "on prepare()");
//        super.prepare();
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
//        super.setDataSource(path);
//        usamos este como canal para pasar el texto
        mText = path;
        Log.d(tag, " seteando el texto:" + path);
    }


    class uListener extends UtteranceProgressListener {

        @Override
        public void onStart(String utteranceId) {
            isSpeakingChapter = isUtteranceOfChapter(utteranceId);
            if (isSpeakingChapter) {
//                readerEvents.voiceStartedSpeakChapter();
            }
            myLog.add("onstartspeaking:" + utteranceId, tag);
        }

        @Override
        public void onDone(String utteranceId) {
            myLog.add("on done, terminé de hablar: " + utteranceId + "forzado=" + forzadoACallar,
                    tag);
//            if (forzadoACallar) {
////                readerEvents.voiceInterrupted();
//                forzadoACallar = false;
//            } else {
//                if (isUtteranceOfChapter(utteranceId)) { // Lectura de voz de chapter
////                    readerEvents.voiceEndedReadingChapter();
//                    myLog.add("on done, terminé de leer chapter", tag);
//                } else if (utteranceId.startsWith(UTTSAVEPREFIX)) { //lectura de file a chapter
//
//                }
//            }
        }

        @Override
        public void onError(String utteranceId) {
            // si se ha forzado a callar
            myLog.add("on error de leer. Forzado a callar=" + forzadoACallar, tag);

            if (isUtteranceOfChapter(utteranceId)) {
                //readerEvents.onInterruptionOfReading();
                if (forzadoACallar) {
//                    readerEvents.voiceInterrupted();
                    forzadoACallar = false;
                } else {
//                    speakDeveloperMsg("Error en el uterance, ver el log");
                    myLog.add("***ERROR en utterance: id = " + utteranceId, tag);
                }
            }
        }

        @Override
        public void onStop(String utteranceId, boolean interrupted) {
            super.onStop(utteranceId, interrupted);
//            myLog.add("onstop utterance", tag);
        }

        private boolean isUtteranceOfChapter(String utteranceId) {
            return utteranceId.startsWith(Constants.PREFIX_OF_CHAPTER_UTTERANCE);
        }
    }

}
