package console;

public interface ConsoleActions {

    final String ACTION_SET_NICK = "Write a nickname to identify yourself";
    final String ACTION_SELECT_USER = "Select an avaliable user to start chatting";
    final String ACTION_SELECT_USER_BY_ID = "Select an ID of the user you want to chat with";
    final String ACTION_SELECT_USER_BY_NICKNAME = "Select an NICK of the user you want to chat with";

   
    final String MENU_OP_1 = "1.Show online users";
    final String MENU_OP_2 = "2.Chat with \"userNick\"";
        final String MENU_OP_2_1 = "\ta.Select by ID";
        final String MENU_OP_2_2 = "\tb.Select by nick";
            final String MENU_ALLOW_CHAT = "\ta.Allow";
            final String MENU_DENY_CHAT = "\tb.Deny";
    final String MENU_OP_3 = "3.Talk to SERVER";

    final String MENU_CHAT_1 = "\t1.Send msg";
    final String MENU_CHAT_EXIT = "\t0.Exit chat";

    final String MENU_OP_ERROR = "Option incorrect";
}
