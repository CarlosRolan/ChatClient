package CLI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    // Console Intro
    final String INTRO = "==WELLCOME==\nThis ChatGroup was created by Carlos Rolán.\nThis app is handled by a server and all connections are supervised by it.";

    // CONSOLE ACTIONS
    final String CLI_CLEAR_CONSOLE = "\033[H\033[2J";

    // CONSOLE OPTIONS
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

    // User Input Actions
    final String IN_SET_NICK = ">Write a nickname to identify yourself";
    final String IN_SELECT_USER = ">Select an ID of the user you want to ChatGroup with [id]";

    final String IN_SELECT_CHAT = ">Select an ID of the ChatGroup you want enter";

    final String IN_SELECT_SINGLE = ">Please select a number from the list";

    final String IN_SET_CHAT_TITLE = ">Enter the ChatGroup TITLE";
    final String IN_SET_CHAT_DESC = ">Enter the ChatGroup DESCRIPTION (Enter to leave blank)";

    final String IN_ENTER_CHAT = "ChatGroup created, press ENTER to continue";

    // MAIN MENU
    final String MENU_MAIN_1 = "1.Show online users";
    final String MENU_MAIN_2 = "2.Start single conversation";
    final String MENU_MAIN_2_1 = "a.Select by ID";
    final String MENU_MAIN_3 = "3.ChatGroups";
    final String MENU_MAIN_EXIT = "0.Exit";

    // SINGLE MENU
    final String MENU_SINGLE_SEND_MSG = ">Write a msg to";
    final String MENU_SINGLE_EXIT = ">To exit write '" + OP_SINGLE_EXIT + "'";
    final String MENU_SINGLE_ALLOW = "\ta.Allow";
    final String MENU_SINGLE_DENY = "\tb.Deny";

    // ChatGroup MENU
    final String MENU_CHAT_1 = "\t1.Enter ChatGroup";
    final String MENU_CHAT_2 = "\t2.Create ChatGroup";
    final String MENU_CHAT_3 = "\t3.Delete ChatGroup";
    final String MENU_CHAT_4 = "\t4.Show all ChatGroups";

    final String MENU_CHAT_1_1 = "\t1.Send msg to ChatGroup";
    final String MENU_CHAT_1_2 = "\t2.Manage ChatGroup";
    final String MENU_CHAT_1_3 = "\t3.Show Members";
    final String MENU_CHAT_1_2_1 = "\t1.Add Member";
    final String MENU_CHAT_1_2_2 = "\t2.Delete Memeber";
    final String MENU_CHAT_EXIT = "\t0.Exit ChatGroup";
    final String MENU_BACK_TO_CHAT = "\t0.Exit ChatGroup Settings";

    final String MENU_OP_ERROR = ">Option incorrect";

    // CONFIG CONSOLE
    final String DATE_FORMAT = "HH:mm:ss";

    // FEEDBACK TO USER
    final String _PENDING_TO_ACCEPT = " pending to accept";
    final String _ACCEPTS_THE_INVITATION = " accepts the invitation";
    final String _LEFT_SINGLE_CHAT = " has left the ChatGroup, press ENTER to back to the MAIN MENU";

    final String NO_CHATS = "You are not member of any ChatGroup. Create a ChatGroup first";

    default void clearConsole() {
        System.out.print(CLI_CLEAR_CONSOLE);
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
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_2);
        System.out.println(MENU_CHAT_3);
        System.out.println(MENU_CHAT_4);
        System.out.println(MENU_CHAT_EXIT);
    }

    default void showInChatMenu() {
        System.out.println(MENU_CHAT_1_1);
        System.out.println(MENU_CHAT_1_2);
        System.out.println(MENU_CHAT_1_3);
        System.out.println(MENU_CHAT_EXIT);
    }

    default void showChatMenuSettings() {
        System.out.println(MENU_CHAT_1_2_1);
        System.out.println(MENU_CHAT_1_2_2);
        System.out.println(MENU_BACK_TO_CHAT);
    }

    default String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
