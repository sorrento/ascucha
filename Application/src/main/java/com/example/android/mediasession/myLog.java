package com.example.android.mediasession;


import android.content.Context;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Milenko on 16/07/2015.
 */
public class myLog {
    private static boolean initialized = false;
    private static String  currentDateandTime;
    private static String  folder      = "/CUELOG/";

    public static void initialize() {

        if (initialized) return;
        initialized = true;

        WriteUnhandledErrors();
        currentDateandTime = currentDate();

        File folderm = new File(Environment.getExternalStorageDirectory() + folder);
        if (!folderm.exists()) folderm.mkdir();

    }

    public static void add(String text, String TAG) {
        try {
            Log.d(TAG, text);

//            File logFile = new File(Environment.getExternalStorageDirectory(), folder + currentDateandTime + "_" + TAG + ".txt");
//            if (!logFile.exists()) {
//                logFile.createNewFile();
//            }
            SimpleDateFormat sdf                = new SimpleDateFormat("HH:mm:ss (dd)| ");
            String           currentDateandTime = sdf.format(new Date());


//            int pid = android.os.Process.myPid();
//            int tid = android.os.Process.myTid();

            //TODO not sure if it put the tid of log...
            //BufferedWriter for performance, true to set append to file flag
//            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
////            buf.append(currentDateandTime + pid + "|" + tid + ":" + text);
//            buf.append(currentDateandTime + ":" + text);
//            buf.newLine();
//            buf.flush();
//            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Add the text to a file which has TAG in the name. It also prints in this tag.
     *
     * @param text
     * @param TAG
     */
    public static void add(String text, String TAG, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        add(text, TAG);
    }

    /***
     * Send unhandled errors to a text file in the phone
     */
    private static void WriteUnhandledErrors() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                PrintWriter pw;
                try {
                    pw = new PrintWriter(
                            new FileWriter(Environment.getExternalStorageDirectory() + folder + "/rt.txt", true));
                    pw.append("*******" + currentDate() + "\n");
                    ex.printStackTrace(pw);
                    pw.flush();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public static void error(String text, Exception e) {
        if (e == null) {
            add(text, "ERROR");
        } else {
            add(text + " | " + e.getLocalizedMessage(), "ERROR");
        }
    }

    public static void printTTSinfo(TextToSpeech t1) {
        int                           i       = 0;
        List<TextToSpeech.EngineInfo> engines = t1.getEngines();

        for (TextToSpeech.EngineInfo engineinfo : engines) {
            i++;
            add("Engine:" + i + engineinfo.name, "TTS");
        }

        Set<Voice> ss = null;
        i = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            i++;
            ss = t1.getVoices();
            for (Voice voice : ss) {
                add(voice.toString(), "TTS");
            }
            add("Speaking with:" + t1.getVoice().getName(), "TTS");
        }
    }
}

