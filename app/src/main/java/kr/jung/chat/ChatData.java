package kr.jung.chat;

public class ChatData {

    /*
    * DTO Data Transform Objects
    */

    private String msg;
    private String uid;
    private String type;

    public ChatData(String msg, String uid) {
        this(msg, uid, "chat");
    }

    public ChatData(String msg, String uid, String type) {
        this.msg = msg;
        this.uid = uid;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }
}
