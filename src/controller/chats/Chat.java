package controller.chats;

import java.util.ArrayList;

import controller.ClientOld;

public class Chat {

    private ArrayList<Member> members = new ArrayList<>();
    private long chatID;
    private String chatName;
    private String chatDesc;

    public long getChatID() {
        return chatID;
    }
    public String getChatName() {
        return chatName;
    }
    public String getChatDesc() {
        return chatDesc;
    }
    
    

    public Chat(long id, String name, String desc, ArrayList<Member> members) {
        chatID = id;
        chatName = name;
        chatDesc = desc;
        this.members = members;
    }

    public Chat(long id, String name, String desc, Member creator) {
        chatID = id;
        chatName = name;
        chatDesc = desc;
        members.add(creator); 
        
        
    }

    public ArrayList<Member> getMembersList() {
        return members;
    }
    
}
