package com.example.aaron.sos;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.WeakHashMap;

/**
 * Created by Aaron on 2014/9/5.
 */
public class Util {

    private Util mUtil = null;

    public static Model load(Context context){
        Model model = null;
        BufferedReader reader = null;
        try{
            InputStream in = context.openFileInput("set.json");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) !=null){
                jsonString.append(line);
            }
            Gson gson = new Gson();
            model = gson.fromJson(jsonString.toString(),Model.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void save(Model model,Context context) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(model);

        Writer writer = null;

        try {
            OutputStream out = context.openFileOutput("set.json",Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  finally {
            if(null != writer){
                writer.close();
            }
        }

    }
}
