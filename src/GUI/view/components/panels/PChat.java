package GUI.view.components.panels;

import com.chat.Chat;

import GUI.GUI;
import controller.manager.FileManager;

public class PChat extends PConv {

    private Chat mChat;

    protected PChat(Chat chat, IConvListener listener) {

        mChat = chat;
        String convId = chat.getChatId();
        String convTitle = chat.getTitle();
        String convSubTitle = chat.getDescription();

        boolean isAdmin = chat.isMemberAdmin(GUI.getInstance().getSession().getConId());

        iConvListener = listener;
        mIsChat = true;
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

}
