package com.example.todo;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveToFile {

    public static final String FILENAME = "listdata.dat";

    public static void writeData(ArrayList<String> item, Context context) {

        try {
            FileOutputStream stream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream object = new ObjectOutputStream(stream);
            object.writeObject(item);
            object.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static ArrayList<String> readData(Context context) {
        ArrayList<String> itemsList = null;

        try {
            FileInputStream stream = context.openFileInput(FILENAME);
            ObjectInputStream object = new ObjectInputStream(stream);
            itemsList = (ArrayList<String>) object.readObject();

        } catch (FileNotFoundException fe) {
            itemsList = new ArrayList<>();
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return itemsList;
    }

}
