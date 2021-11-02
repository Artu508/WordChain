package kr.jung.chat;

import android.content.res.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordChainManager {
    private static List<String> words = new ArrayList<>();
    private static List<String> routeWords = new ArrayList<>();
    private static List<String> leadWords = new ArrayList<>();
    private static List<String> neoWords = new ArrayList<>();

    public static List<String> getWords(Resources resource) {
        if(words == null) {
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
}
