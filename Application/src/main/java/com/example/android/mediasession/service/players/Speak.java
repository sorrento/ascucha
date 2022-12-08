package com.example.android.mediasession.service.players;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import androidx.annotation.NonNull;

import com.example.android.mediasession.Constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class Speak {

    static public void setSpeakLanguage(TextToSpeech t, @NonNull String lan) {
//        tag = "spk";
//        myLog.add("Setting Language:" + lan, tag);

        switch (lan) {
            case "ES":
                Locale spanish = new Locale("es", "ES");
                t.setLanguage(spanish);
                break;

            case "EN":
                t.setLanguage(Locale.US);

                //Select voice
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Set<Voice> voices = t.getVoices();
                    for (Voice voice : voices) {
                        // if (voice.getName().equals("en-US-SMTl01")) {
                        if (voice.getQuality() == 400 && voice.getLocale() == Locale.US) {
//                            myLog.add("voice set to: " + voice.toString(), tag);
                            t.setVoice(voice);
                            break;
                        }
                    }
                }
                break;
            //TODO manage unknown language
        }
    }

    public static void speak(String s, boolean interrupting, String utterance, TextToSpeech t1) {

        // Parche por si el chapter es muy largo
        int endIndex = TextToSpeech.getMaxSpeechInputLength() - 10;
        if (s.length() > endIndex) {
//        myLog.add("El texto es demasiado largo: " + s.length() + " > " + endIndex, tag);
            String s1 = s.substring(0, endIndex);
            String s2 = s.substring(endIndex + 1);
            speak(s1, false, Constants.PREFIX_OF_CHAPTER_UTTERANCE + "cortado 1", t1);
            speak(s2, false, Constants.PREFIX_OF_CHAPTER_UTTERANCE +
                    "cortado 2", t1);
            return;
        }

        int queueMode = interrupting ? TextToSpeech.QUEUE_FLUSH : TextToSpeech.QUEUE_ADD;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t1.speak(s, queueMode, null, utterance);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
            t1.speak(s, queueMode, map);
        }
    }
}
