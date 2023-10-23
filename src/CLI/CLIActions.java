package CLI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface CLIActions {

    // CONFIG CONSOLE
    final String DATE_FORMAT = "HH:mm:ss";

    enum ConsoleColor {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        DEFAULT("\u001B[0m");

        private String colorCode;

        public String getColor() {
            return colorCode;
        }

        private ConsoleColor(String ansiiCode) {
            colorCode = ansiiCode;
        }
    }

    // Console Intro
    final String INTRO = "==WELLCOME==\nThis Chat was created by Carlos Rolán.\nThis app is handled by a server and all connections are supervised by it.";

    // CONSOLE ACTIONS
    final String CLI_CLEAR_CONSOLE = "\033[H\033[2J";

    /* USER REQUEST FOR INPUT */
    final String IN_SET_NICK = ">Write a nickname to identify yourself";
    final String IN_SELECT_USER = ">Select an ID of the user you want to Chat with [id]";

    final String IN_SELECT_CHAT = ">Select an ID of the Chat you want enter";

    final String IN_SELECT_SINGLE = ">Please select a number from the list";

    final String IN_SET_CHAT_TITLE = ">Enter the Chat TITLE";
    final String IN_SET_CHAT_DESC = ">Enter the Chat DESCRIPTION (Enter to leave blank)";

    final String IN_ENTER_CHAT = ">Chat created, press ENTER to continue";

    /* INPUT OPTIONS */
    final String OP_1 = "1";
    final String OP_2 = "2";
    final String OP_3 = "3";
    final String OP_4 = "4";
    final String OP_5 = "5";
    final String OP_6 = "6";
    final String OP_ALLOW = "a";
    final String OP_DENY = "b";
    final String OP_EXIT = "0";
    final String OP_SINGLE_EXIT = ".exit";

    /* MAIN MENU */
    final String MENU_MAIN_1 = "1.Show online users";
    final String MENU_MAIN_2 = "2.Start single conversation";
    final String MENU_MAIN_3 = "3.Chats";
    final String MENU_MAIN_EXIT = "0.Exit";

    /* SINGLE MENU */
    final String MENU_SINGLE_SEND_MSG = ">Write a msg to";
    final String MENU_SINGLE_EXIT = ">To exit write '" + OP_SINGLE_EXIT + "'";
    final String MENU_SINGLE_ALLOW = "\ta.Allow";
    final String MENU_SINGLE_DENY = "\tb.Deny";

    /* CHAT RELATED MENUS */
    final String MENU_CHATS_1 = "1.Enter Chat";
    final String MENU_CHATS_2 = "2.Create Chat";
    final String MENU_CHATS_3 = "3.Delete Chat";
    final String MENU_CHATS_4 = "4.Show all Chats";
    final String MENU_CHATS_EXIT = "0.Exit Chats Menu";

    final String MENU_CHAT_1 = "*\t1.Send msg to Chat";
    final String MENU_CHAT_2 = "*\t2.Manage Chat";
    final String MENU_CHAT_3 = "*\t3.Show Members";
    final String MENU_CHAT_4 = "*\t4.Add Member";
    final String MENU_CHAT_5 = "*\t5.Delete Memeber";
    final String MENU_CHAT_6 = "*\t6.Manage Permissions";
    final String MENU_CHAT_EXIT = "\t0.Exit Chat";

    final String MENU_WAITING = "Waiting";

    /* FEDDBACK */
    final String _PENDING_TO_ACCEPT = " pending to accept";
    final String _ACCEPTS_THE_INVITATION = " accepts the invitation";
    final String _LEFT_SINGLE_CHAT = " has left the Chat, press ENTER to back to the MAIN MENU";

    final String FEED_NO_CHATS = "You are not member of any Chat. Create a Chat first";
    final String OP_ERROR = "Option incorrect";

    final String FEED_CLIENT_NOT_FOUND = "There is no CLIENT with that ID";
    final String FEED_CHAT_NOT_FOUND = "There is no Chat with that ID";
    final String FEED_SELF_REFERENCE = "You cannot talk with your self";

    /* MENUs */
    default void showMainMenu(String nick) {
        System.out.println("==========|" + nick + "|==========");
        System.out.println(MENU_MAIN_1);
        System.out.println(MENU_MAIN_2);
        System.out.println(MENU_MAIN_3);
        System.out.println(MENU_MAIN_EXIT);
    }

    default void showSingleMenu() {
        System.out.println(MENU_SINGLE_SEND_MSG);
        System.out.println(MENU_SINGLE_EXIT);
    }

    default void showChatsMenu() {
        System.out.println("===CHATS===");
        System.out.println(MENU_CHATS_1);
        System.out.println(MENU_CHATS_2);
        System.out.println(MENU_CHATS_3);
        System.out.println(MENU_CHATS_4);
        System.out.println(MENU_CHATS_EXIT);
    }

    default void showInChatMenu(String chatTitle, boolean isAdmin) {
        System.out.println("--------" + chatTitle + "--------");
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_3);
        if (isAdmin) {
            System.out.println(MENU_CHAT_4);
            System.out.println(MENU_CHAT_5);
            System.out.println(MENU_CHAT_6);
        }
        System.out.println(MENU_CHAT_EXIT);
    }

    /* UTILS */
    default String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
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

    default void clearConsole() {
        System.out.println(CLI_CLEAR_CONSOLE);
    }
}
