package controller.chats;

import java.util.ArrayList;

public class Chat {

    private ArrayList<Member> members = new ArrayList<>();
    private long chatID;
    private String chatTitle;
    private String chatDesc;

    public long getChatID() {
        return chatID;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public String getChatDesc() {
        return chatDesc;
    }

    public Chat(long id, String title, String desc, ArrayList<Member> members) {
        chatID = id;
        chatTitle = title;
        chatDesc = desc;
        this.members = members;
    }

    public Chat(long id, String title, String desc, Member creator) {
        chatID = id;
        chatTitle = title;
        chatDesc = desc;
        members.add(creator);
    }

    public ArrayList<Member> getAllMembers() {
        return members;
    }

    public String listmembers() {
        String memberList = "";
        for (Member iter : members) {
            memberList += iter.toString() + "\n";
        }

        return memberList;
    }

    public void addMember(Member newMember) {
        this.members.add(newMember);
    }

    public void removeMember(Member deletedMember) {
        this.members.remove(deletedMember);
    }

    @Override
    public String toString() {
        return "[" + chatID + "]" + chatTitle + "\n" + chatDesc + "\n" + listmembers();
    }

}
