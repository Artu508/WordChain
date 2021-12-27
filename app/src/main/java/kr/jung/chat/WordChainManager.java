package kr.jung.chat;

import static android.content.ContentValues.TAG;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.dynamic.ObjectWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class WordChainManager {
    private static final HashMap<Character, Character> preSoundMap = new HashMap<>();
    private static final List<String> words = new ArrayList<>();
    private static final List<String> routeWords = new ArrayList<>();
    private static final List<String> leadWords = new ArrayList<>();
    private static final List<String> neoWords = new ArrayList<>();

    private final List<String> usedWords = new LinkedList<>();
    private final Resources publicResources;

    public WordChainManager(Resources resources) {
        publicResources = resources;
    }

    public ChainState chainWord(String word) {
        if(isUsed(word)) return ChainState.USED_WORD;
        if(!doWordExists(publicResources, word)) return ChainState.NONEXISTENT_WORD;
        if(usedWords.isEmpty()) {
            if(isLeadWord(publicResources, word)) return ChainState.FIRST_LEAD;
            if(isNeoWord(publicResources, word)) return ChainState.FIRST_NEO;
        }
        else {
            String lastWord = usedWords.get(usedWords.size() - 1);
            char[] chars = lastWord.toCharArray();
            char lastChar = chars[chars.length - 1];
            char p = getPhoneticCharacter(publicResources, lastChar);
            if(word.toCharArray().length > 0 && word.toCharArray()[0] != lastChar && word.toCharArray()[0] != p)
                return ChainState.UNMATCHED_CHAIN;
        }

        usedWords.add(word);
        return ChainState.OK;
    }

    public boolean isUsed(String word) {
        return usedWords.contains(word);
    }

    public static void loadRequiredData(Resources resources) {
        getNeoWords(resources);
        getLeadWords(resources);
        getRouteWords(resources);
        getWords(resources);
        getPhoneticCharacter(resources, (char) 0);
    }

    public static boolean doWordExists(Resources resources, String word) {
        return WordChainManager.getWords(resources).contains(word);
    }

    public static boolean isRouteWord(Resources resources, String word) {
        return WordChainManager.getRouteWords(resources).contains(word);
    }

    public static boolean isLeadWord(Resources resources, String word) {
        return WordChainManager.getLeadWords(resources).contains(word);
    }

    public static boolean isNeoWord(Resources resources, String word) {
        return WordChainManager.getNeoWords(resources).contains(word);
    }

    public static List<String> getWords(Resources resource) {
        if(words.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("word_list.txt"));
                while(sc.hasNextLine())
                    words.add(sc.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return words;
    }

    public static List<String> getRouteWords(Resources resource) {
        if(routeWords.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("route_list.txt"));
                while(sc.hasNextLine())
                    routeWords.add(sc.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return routeWords;
    }

    public static List<String> getLeadWords(Resources resource) {
        if(leadWords.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("lead_list.txt"));
                while(sc.hasNextLine())
                    leadWords.add(sc.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return leadWords;
    }

    public static List<String> getNeoWords(Resources resource) {
        if(neoWords.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("neo_list.txt"));
                while(sc.hasNextLine())
                    neoWords.add(sc.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return neoWords;
    }

    public static char getPhoneticCharacter(Resources resource, char c) {
        if(preSoundMap.isEmpty()) {
            try {
                Scanner sc = new Scanner(resource.getAssets().open("presounds.json"));
                StringBuilder builder = new StringBuilder();
                while (sc.hasNextLine()) {
                    builder.append(sc.nextLine()).append('\n');
                }
                JSONObject jsonObject = new JSONObject(builder.toString());
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    char key = it.next().charAt(0);
                    preSoundMap.put(key, (jsonObject.get(key + "") + "").charAt(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return preSoundMap.get(c) != null ? preSoundMap.get(c) : c;
    }

    public static enum ChainState {
        NONEXISTENT_WORD, UNMATCHED_CHAIN, USED_WORD, FIRST_NEO, FIRST_LEAD, OK
    }
}
