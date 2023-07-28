package controller.console;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.chat.Chat;
import com.comunication.ApiCodes;
import com.comunication.Connection;
import com.comunication.Msg;

import api.ClientAPI;

public class ConsoleConnection extends Thread implements ApiCodes, ConsoleActions {

    enum ConsoleState {
        IN_MAIN_MENU,
        IN_SINGLE,
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
    private boolean inSingle = false;
    private String singleId = "0";
    private String singleNick = null;

    private boolean inChat = false;
    private Chat currentChat;

    // getters
    public Connection getConnection() {
        return c;
    }

    public boolean isInSingle() {
        return inSingle;
    }

    public boolean isInChat() {
        if (currentChat == null) {
            return false;
        }
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

    private void showInChatMenu() {
        System.out.println(MENU_CHAT_1_1);
        System.out.println(MENU_CHAT_1_2);
        System.out.println(MENU_CHAT_EXIT);

        switch (sc.nextLine()) {
            case OP_1:
                break;
            case OP_2:
                showSettingsChatMenu();
                break;
            default:
                break;
        }
    }

    private void selectMainAction(String op) {
        Msg msgOut = null;
        switch (op) {
            // Show all users online
            case OP_1:
                msgOut = ClientAPI.newRequest().showAllOnline(c.getConId());
                break;
            // Start Single-Chat (1v1)
            case OP_2:
                msgOut = ClientAPI.newRequest().askForSingle(c.getConId(), selectSingleById(), c.getNick());
                break;
            // Start Multiple-Chat Menu
            case OP_3:
                chatsOptions();
                selectChatsOptions(sc.nextLine());
                break;
            // Exit program
            case OP_EXIT:
                System.exit(0);
            default:
                if (OP_ALLOW.equals(op)) {
                    msgOut = ClientAPI.newRequest().permissionRespond(true, singleId, c.getConId(), c.getNick());
                } else if (OP_DENY.equals(op)) {
                    inSingle = false;
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
        if (op.equals(OP_SINGLE_EXIT)) {
            exitSingle();
        } else {
            if (inSingle || singleId != null || singleNick != null) {
                sendToSingle(op);
            }
        }
    }

    private void selectChatsOptions(String op) {
        switch (op) {
            // Enter chat
            case OP_1:
                c.writeMessage(ClientAPI.newRequest().selectChat(selectChatById()));
                break;
            // Create chat
            case OP_2:
                // TODO create a way to add new members from the creation of the chat
                System.out.println(IN_SET_CHAT_TITLE);
                String chatTitle = sc.nextLine();
                System.out.println(IN_SET_CHAT_DESC);
                String chatDesc = sc.nextLine();
                c.writeMessage(ClientAPI.newRequest().requestNewChat(c.getConId(), c.getNick(), chatTitle, chatDesc));
                break;
            // Delete chat
            case OP_3:
                break;
            // Show all chats u are a member of
            case OP_4:
                c.writeMessage(ClientAPI.newRequest().showAllChat(c.getConId()));
                break;
            default:
                inChat = false;
                currentChat = null;
                break;
        }
    }

    private void selectInChatAction(String op) {
        switch (op) {
            case OP_1:
                System.out.println("OP_1");
                break;
            case OP_2:
                System.out.println("OP_2");
                break;
        }
    }

    private void exitSingle() {
        c.writeMessage(ClientAPI.newRequest().exitSingle(c.getConId(), singleId));
        inSingle = false;
        singleId = null;
        singleNick = null;
    }

    private String selectSingleById() {
        int userID;
        do {
            System.out.println(IN_SELECT_USER);
            c.writeMessage(ClientAPI.newRequest().showAllOnline(c.getConId()));
            try {
                userID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(userID);
        } while (true);
    }

    private String selectChatById() {
        int chatID;
        do {
            System.out.println(IN_SELECT_CHAT);
            c.writeMessage(ClientAPI.newRequest().showAllChat(c.getConId()));
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
        switch (reqRespond.getAction()) {
            case REQ_SHOW_ALL_CON:
                System.out.println(reqRespond.showParameters());
                break;
            case REQ_SHOW_ALL_CHAT:
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
                inSingle = true;
                singleNick = reqRespond.getParameter(0);
                singleId = reqRespond.getReceptor();
                System.out.println(singleNick + _ACCEPTS_THE_INVITATION);
                System.out.println("----Chatting with [" + singleId + "]" + singleNick + "----");
                break;
            case REQ_EXIT_SINGLE:
                System.out.println(
                        singleNick + "[" + singleId + "]" + _LEFT_SINGLE_CHAT);
                inSingle = false;
                singleNick = null;
                singleId = null;

                break;

            case REQ_INIT_CHAT:
                Chat c = Chat.instanceChat(reqRespond);
                inChat = true;
                currentChat = c;
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
        if (inSingle) {
            showSingleMenu();
            selectSingleAction(sc.nextLine());
        } else if (inChat) {
            showInChatMenu();
            selectInChatAction(MENU_OP_ERROR);
        } else {
            showMainMenu(c.getNick());
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
