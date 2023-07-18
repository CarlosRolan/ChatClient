package controller.console;

public interface ConsoleActions {

    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_BLACK = "\u001B[30m";
    final String ANSI_RED = "\u001B[31m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";
    final String ANSI_BLUE = "\u001B[34m";
    final String ANSI_PURPLE = "\u001B[35m";
    final String ANSI_CYAN = "\u001B[36m";

    final String OP_1 = "1";
    final String OP_2 = "2";
    final String OP_3 = "3";
    final String OP_4 = "4";
    final String OP_5 = "5";
    final String OP_6 = "6";

    final String OP_YES = "a";
    final String OP_NO = "b";

    final String OP_EXIT = "0";

    // Console Intro
    final String INTRO = "==WELLCOME==\nThis chat was created by Carlos Rolán.\nThis app is handled by a server and all connections are supervised by it.";

    // User Input Actions
    final String ACTION_SET_NICK = ">Write a nickname to identify yourself";
    final String ACTION_SELECT_USER_BY_ID = ">Select an ID of the user you want to chat with [id]";

    final String ACTION_CHAT_SELECT = ">Select an avaliable CHAT to start chatting";
    final String ACTION_CHAT_SELECT_USER = ">Select an ID of the CHAT you want to chat with";

    final String ACTION_SEND_MSG_TO_SINGLE = ">Write a msg to";
    final String ACTION_EXIT_SINGLE = "write '.exit' to exit the chat";

    // Console OCHAT
    final String MENU_OP_1 = "1.Show online users";
    final String MENU_OP_2 = "2.Start single conversation";
    final String MENU_OP_2_1 = "a.Select by ID";
    final String MENU_ALLOW_SINGLE = "\ta.Allow";
    final String MENU_DENY_SINGLE = "\tb.Deny";
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
