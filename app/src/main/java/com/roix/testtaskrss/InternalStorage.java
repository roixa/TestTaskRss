package com.roix.testtaskrss;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by u5 on 9/13/16.
 * this class need to save a list of our serialized items
 */
public class InternalStorage{

    public static final String KEY="test_task_rss_key";

    public static void writeObject(Context context, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(KEY, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(KEY);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}