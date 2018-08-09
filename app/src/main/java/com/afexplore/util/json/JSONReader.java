package com.afexplore.util.json;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Reads JSON content from JSON files located in the assets folder.
 * */
public class JSONReader {

    /**
     * Reads JSON from file and returns the JSON Data Model associated.
     * The returned object is generic, enabling this method to read any JSON file and map it to any
     * DataModel object (any POJO class).
     * */
    public static <T> T loadJSONObjectFromAsset(Context context, String jsonFileNameString, Type typeOfT) {

        // Generic type variable to return any DataModel object
        T t = null;

        // If the type of the generic Type is not defined, return null
        if (jsonFileNameString == null || typeOfT == null) {
            return null;
        }

        try {

            // Open the file through an InputStream
            InputStream inputStream = context.getAssets().open(jsonFileNameString);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Convert the content of the file into an object of any type
            Gson gson = new Gson();
            t = gson.fromJson(bufferedReader, typeOfT);

            // Close IO objects
            inputStream.close();
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * Reads JSON from file and returns the JSON String associated.
     * */
    public static String loadJSONStringFromAsset(Context context, String jsonFileNameString) {

        String jsonString = null;

        try {

            InputStream inputStream = context.getAssets().open(jsonFileNameString);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            jsonString = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }
}
