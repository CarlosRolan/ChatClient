package controller.console;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.ApiCodes;
import com.Connection;
import com.Msg;
import com.chat.Chat;

import api.ClientAPI;

public class ConsoleConnection extends Thread implements ApiCodes, ConsoleActions {

    enum ConsoleState {
        IN_SINGLE_CHAT,
        IN_CHAT,
    }

    // Singleton
    private static ConsoleConnection instance;

    private static Scanner sc = new Scanner(System.in);

    public static ConsoleConnection getInstance() {
        System.out.println(INTRO);
        if (instance == null) {
            instance = new ConsoleConnection();
        }
        return instance;
    }
    // --------

    private final Connection c;

    // Console state vars
    private boolean inSingleChat = false;
    private String singleId = "0";
    private String singleNick = null;

    private boolean inChat = false;
    private Chat currentChat;

    // getters
    public Connection getConnection() {
        return c;
    }

    public boolean isInSingleChat() {
        return inSingleChat;
    }

    public boolean isInChat() {
        return inChat;
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    // constructor
    private ConsoleConnection() {
        System.out.println(IN_SET_NICK);
        c = new Connection(sc.nextLine());
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private void showMainMenu() {
        System.out.println("==========|" + c.getNick() + "|==========");
        System.out.println(MENU_MAIN_1);
        System.out.println(MENU_MAIN_2);
        System.out.println(MENU_MAIN_3);
        System.out.println(MENU_MAIN_EXIT);
    }

    private void showSingleMenu() {
        System.out.println(MENU_SINGLE_SEND_MSG);
        System.out.println(MENU_SINGLE_EXIT);
    }

    private void showChatMenu() {
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_2);
        System.out.println(MENU_CHAT_3);
        System.out.println(MENU_CHAT_EXIT);
    }

    private void showInChatMenu() {
        System.out.println(MENU_CHAT_1_1);
        System.out.println(MENU_CHAT_1_2);
        System.out.println(MENU_CHAT_EXIT);
    }

    private void showSettingsChatMenu() {
        System.out.println(MENU_CHAT_1_2_1);
        System.out.println(MENU_CHAT_1_2_2);
        System.out.println(MENU_CHAT_1_2_3);
        System.out.println(MENU_CHAT_EXIT);
    }

    private void selectMainAction(String op) {
        Msg msgOut = null;
        switch (op) {
            // Show all users online
            case OP_1:
                msgOut = ClientAPI.newRequest().showAllOnline();
                break;
            // Start single chat (1v1)
            case OP_2:
                msgOut = ClientAPI.newRequest().askForSingle(c.getConId(), selectSingle(), c.getNick());
                break;
            // Start Multiple Chat Menu
            case OP_3:
                showChatMenu();
                selectChatAction(sc.nextLine());
                break;
            // Exit program
            case OP_EXIT:
                System.exit(0);
            default:
                if (OP_ALLOW.equals(op)) {
                    msgOut = ClientAPI.newRequest().permissionRespond(true, singleId, c.getConId(), c.getNick());
                } else if (OP_DENY.equals(op)) {
                    inSingleChat = false;
                    msgOut = ClientAPI.newRequest().permissionRespond(false, singleId, c.getConId(), c.getNick());
                } else {

                }
                break;
        }

        if (msgOut != null) {
            c.writeMessage(msgOut);
        }
    }

    private void selectSingleAction(String op) {
        if (op.equals(MENU_SINGLE_EXIT)) {
            exitSingle();
            inSingleChat = false;
            singleId = null;
            singleNick = null;
        } else {
            if (inSingleChat || singleId != null || singleNick != null) {
                sendToSingle(op);
            }
        }
    }

    private void selectChatAction(String op) {
        switch (op) {
            // Enter chat
            case OP_1:
                showInChatMenu();
                break;
            // Create chat
            case OP_2:
                showSettingsChatMenu();
                break;
            // Delete chat
            case OP_EXIT:
                break;
            default:
                break;
        }
    }

    private void exitSingle() {
        c.writeMessage(ClientAPI.newRequest().exitSingle(c.getConId(), singleId));
    }

    private String selectSingle() {
        int userID;
        do {
            System.out.println(IN_SELECT_USER);
            c.writeMessage(ClientAPI.newRequest().showAllOnline());
            try {
                userID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(userID);
        } while (true);
    }

    private String selectChat() {
        int chatID;

        do {
            System.out.println(IN_SELECT_CHAT);
            c.writeMessage(ClientAPI.newRequest().showAllOnline());
            try {
                chatID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(chatID);
        } while (true);
    }

    private void sendToSingle(String txt) {
        c.writeMessage(ClientAPI.newRequest().sendSingleMsg(c.getConId(), singleId, txt));
    }

    // HADLING SERVER RESPONSE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void handleRequest(Msg reqRespond) {
        // System.out.println("\n" + reqRespond.toString());
        switch (reqRespond.getAction()) {
            case REQ_SHOW_ALL:
                System.out.println(reqRespond.showParameters());
                break;
            case REQ_ASKED_FOR_PERMISSION:
                singleNick = reqRespond.getParameter(0);
                singleId = reqRespond.getEmisor();
                System.out.println("--" + singleNick + " wants to chat with you--");
                System.out.println(MENU_SINGLE_ALLOW);
                System.out.println(MENU_SINGLE_DENY);
                break;
            case REQ_WAITING_FOR_PERMISSION:
                System.out.println(reqRespond.getParameter(0) + _PENDING_TO_ACCEPT);
                break;
            case REQ_START_SINGLE:
                inSingleChat = true;
                singleNick = reqRespond.getParameter(0);
                singleId = reqRespond.getReceptor();
                System.out.println(singleNick + _ACCEPTS_THE_INVITATION);
                System.out.println("Press ENTER to tenter the chat");
                System.out.println("----Chatting with [" + singleId + "]" + singleNick + "----");

                break;
            case REQ_EXIT_SINGLE:
                System.out.println(
                        singleNick + "[" + singleId + "]" + _LEFT_SINGLE_CHAT);
                inSingleChat = false;
                singleNick = null;
                singleId = null;
            default:
                break;
        }

    }

    private void handleMessage(Msg responMessage) {
        changeConsoleColor(ConsoleColor.GREEN);
        switch (responMessage.getAction()) {
            case MSG_SINGLE_MSG:
                System.out.println("\t\t[" + getCurrentTime() + "]" + singleNick + ": " + responMessage.getBody());
                break;
            default:
                break;
        }
    }

    private void handleError(Msg respondError) {
        changeConsoleColor(ConsoleColor.RED);
        switch (respondError.getAction()) {
            case ERROR_CLIENT_NOT_FOUND:
                System.out.println("There is no client with that ID");
                break;
            case ERROR_SELF_REFERENCE:
                System.out.println("You cannot talk with your self");
                break;
            default:
                break;
        }
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void listenServer() throws ClassNotFoundException, IOException {
        Msg respond = c.readMessage();
        switch (respond.PACKAGE_TYPE) {
            case REQUEST:
                handleRequest(respond);
                break;
            case MESSAGE:
                handleMessage(respond);
                break;
            case ERROR:
                handleError(respond);
                break;
        }
        changeConsoleColor(ConsoleColor.DEFAULT);
    }

    // PUBLIC METHODS
    public void startSesion() {
        if (inSingleChat) {
            showSingleMenu();
            selectSingleAction(sc.nextLine());
        } else {
            showMainMenu();
            selectMainAction(sc.nextLine());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                listenServer();
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("YOU ARE OFFLINE");
                break;
            }
        }
    }

}
