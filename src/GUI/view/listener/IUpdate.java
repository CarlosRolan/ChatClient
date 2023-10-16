package GUI.view.listener;

import com.data.MSG;

public interface IUpdate {

    void updateUsers();

    void updateChats();

    void onMessageReceived(MSG msgReceived, boolean isChat);

}
