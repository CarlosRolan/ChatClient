package GUI.components.panels;

import com.chat.Chat;

import GUI.GUI;
import controller.manager.FileManager;

public class PChat extends PConv {

    private Chat mChat;
    protected boolean mAdmin = false;

    public Chat getChatInstance() {
        return mChat;
    }

    public void setAdminRights(boolean admin) {
        mAdmin = true;
    }

    public boolean hasAdminRights() {
        return mAdmin;
    }

    PChat(Chat chat, IConvListener listener) {
        mChat = chat;
        String convId = chat.getChatId();
        String convTitle = chat.getTitle();
        String convSubTitle = chat.getDescription();

        boolean isAdmin = chat.isMemberAdmin(GUI.getInstance().getSession().getConId());

        iConvListener = listener;
        mAdmin = isAdmin;
        mId = convId;
        mTitle = convTitle;
        mSubTitle = convSubTitle;
        mOpen = false;
        if (!FileManager.getInstance().initConvHistory(convTitle, true)) {
            history = FileManager.getInstance().loadConvHistory(convTitle, true);
            loadHistory();
        }
        initComponents();

    }

    @Override
    protected void actionSend(String line) {
        super.actionSend(line);
        iConvListener.onMsgSent(mId, mTitle, mSubTitle, line, true);
    }

    public void editChatTitle(String newTitle) {
        // Changin in the client-side
        mChat.setTitle(newTitle);
        // changing in the server-side
        setTitle(newTitle);
        GUI.getInstance().updateChat(mChat);
    }

    public void editChatDesc(String newDesc) {
        // Changing in the client-side
        mChat.setDescription(newDesc);
        // Changing in the server-side
        GUI.getInstance().updateChat(mChat);
    }

    public void deleteChat() {
    }

}
