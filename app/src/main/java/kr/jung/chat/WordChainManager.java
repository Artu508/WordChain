package kr.jung.chat;

import android.content.res.Resources;

import com.google.android.gms.dynamic.ObjectWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class WordChainManager {
    private static final HashMap<String, String> preSoundMap = new HashMap<>();
    private static final List<String> words = new ArrayList<>();
    private static final List<String> routeWords = new ArrayList<>();
    private static final List<String> leadWords = new ArrayList<>();
    private static final List<String> neoWords = new ArrayList<>();

    public static List<String> getWords(Resources resource) {
        if(words.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("word_list.txt"));
                String data;
                while((data = sc.nextLine()) != null)
                    words.add(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return words;
    }

    public static String getPhoneticCharacter(Resources resource, char c) {
        if(preSoundMap.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("presounds.txt"));
                String data;
                StringBuilder builder = new StringBuilder();
                while ((data = sc.nextLine()) != null)
                    builder.append(data).append('\n');
                JSONObject jsonObject = new JSONObject(builder.toString());
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    preSoundMap.put(key, jsonObject.get(key) + "");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return preSoundMap.get(c) != null ? preSoundMap.get(c) : " ";
    }
}
