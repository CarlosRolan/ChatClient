package controller.chat;

public class Chat {

    private long chatID;
    private String chatName;
    private String chatDescription;
    private boolean isAdmin;
    private String adminNick;
    private final int[] permissions = new int[2];

    public Chat(long id,String chatName, String nick, boolean admin) {
        isAdmin = admin;
        adminNick = nick;
    }

    
}
