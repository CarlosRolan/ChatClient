package controller.console;

public interface ConsoleActions {

    enum ConsoleColor {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        DEFAULT("\u001B[0m");

        public String getColor() {
            return colorCode;
        }

        private String colorCode;

        private ConsoleColor(String colorAnsiiCode) {
            colorCode = colorAnsiiCode;
        }
    }

    default void clearConsole() {
        System.out.print(CLEAR_CONSOLE);
        System.out.print(ConsoleColor.DEFAULT.getColor());
        System.out.flush();
    }

    default void changeConsoleColor(ConsoleColor color) {
        String ansiCode = null;
        switch (color) {
            case BLACK:
                ansiCode = ConsoleColor.BLACK.getColor();
                break;
            case BLUE:
                ansiCode = ConsoleColor.BLUE.getColor();
                break;
            case CYAN:
                ansiCode = ConsoleColor.CYAN.getColor();
                break;
            case PURPLE:
                ansiCode = ConsoleColor.PURPLE.getColor();
                break;
            case GREEN:
                ansiCode = ConsoleColor.GREEN.getColor();
                break;
            case RED:
                ansiCode = ConsoleColor.RED.getColor();
                break;
            default:
                ansiCode = ConsoleColor.DEFAULT.getColor();
                break;
        }
        System.out.print(ansiCode);
    }

    final String CLEAR_CONSOLE = "\033[H\033[2J";

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
    final String ACTION_EXIT_SINGLE = ">.exit";
    final String ACTION_SELECT_SINGLE = ">Please select a number from the list";

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

    final String DATE_FORMAT = "HH:mm:ss";

    final String _PENDING_TO_ACCEPT = " pending to accept";
    final String _ACCEPTS_THE_INVITATION = " accepts the invitation";
    final String _LEFT_SINGLE_CHAT = " has left the chat, press ENTER to back to the MAIN MENU";
}
