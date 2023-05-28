package controller.console;

public interface ConsoleActions {

    // Console Intro
    final String INTRO = "==WELLCOME==\nThis chat was created by Carlos Rolán.\nThis app is handled by a server and all connections are supervised by it.";

    // User Input Actions
    final String ACTION_SET_NICK = ">Write a nickname to identify yourself";
    final String ACTION_SELECT_USER = ">Select an avaliable user to start chatting";
    final String ACTION_SELECT_USER_BY_ID = ">Select an ID of the user you want to chat with";
    final String ACTION_SELECT_USER_BY_NICKNAME = ">Select an NICK of the user you want to chat with";

    final String CHAT_SELECT = ">Select an avaliable CHAT to start chatting";
    final String CHAT_SELECT_USER_BY_ID = ">Select an ID of the CHAT you want to chat with";
    final String CHAT_SELECT_USER_BY_NICKNAME = ">Select an TITLE of the CHAT you want to chat with";

    // Console OCHAT
    final String MENU_OP_1 = "1.Show online users";
    final String MENU_OP_2 = "2.Start single conversation";
    final String MENU_OP_2_1 = "\ta.Select by ID";
    final String MENU_ALLOW_CHAT = "\ta.Allow";
    final String MENU_DENY_CHAT = "\tb.Deny";
    final String MENU_OP_3 = "3.Chats";
    final String MENU_OP_EXIT = "0.Exit";

    final String MENU_CHAT_1 = "\t1.Send msg";
    final String MENU_CHAT_2 = "\t2.Add member";
    final String MENU_CHAT_3 = "\t3.Delete member";
    final String MENU_CHAT_4 = "\t4.Manage Permissions";
    final String MENU_CHAT_4_1 = "\ta.Make Admin";
    final String MENU_CHAT_4_2 = "\tb.Make Cooperator";
    final String MENU_CHAT_4_3 = "\tc.Make Regular";
    final String MENU_CHAT_5 = "\t5.Show Members";
    final String MENU_CHAT_EXIT = "\t0.Exit chat";

    final String MENU_OP_ERROR = ">Option incorrect";
}
