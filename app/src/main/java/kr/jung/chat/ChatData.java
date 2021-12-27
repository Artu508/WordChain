package kr.jung.chat;

public class ChatData {

    /*
    * DTO Data Transform Objects
    */

    private String msg;
    private String nickname;
    private String type;

    public ChatData(String msg, String nickname) {
        this(msg, nickname, "chat");
    }

    public ChatData(String msg, String nickname, String type) {
        this.msg = msg;
        this.nickname = nickname;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }
}
