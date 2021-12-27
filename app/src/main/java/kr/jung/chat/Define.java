package kr.jung.chat;

public class Define {

    public String sUid = "";

    private static Define instance;
    public static Define ins(){
        if(instance == null){
            instance = new Define();
        }
        return instance;
    }
}
