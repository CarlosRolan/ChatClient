package controller.console;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.ApiCodes;
import com.Connection;
import com.Msg;

import api.ClientAPI;

public class ConsoleConnection extends Thread implements ApiCodes, ConsoleActions {

    enum ConnectionState {
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

    private boolean inSingleChat = false;
    private Connection c = null;
    private String singleId = "0";
    private String singleNick = null;

    // getters
    public Connection getConnection() {
        return c;
    }

    public boolean isInSingleChat() {
        return inSingleChat;
    }

    // constructor
    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        c = new Connection(sc.nextLine());
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private void showMainMenu() {
        System.out.println("==========|" + c.getNick() + "|==========");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
        System.out.println(MENU_OP_EXIT);
    }

    private void showSingleMenu() {
        System.out.println(ACTION_SEND_MSG_TO_SINGLE);
        System.out.println(_EXIT_SINGLE);
    }

    private void selectMainAction(String op) {
        Msg msgOut = null;
        switch (op) {
            case OP_1:
                msgOut = ClientAPI.newRequest().showAllOnline();
                break;
            case OP_2:
                msgOut = ClientAPI.newRequest().askForSingle(c.getConId(), selectSingle(), c.getNick());
                break;
            case OP_3:
                break;
            case OP_EXIT:
                System.exit(0);
            default:
                if (OP_YES.equals(op)) {
                    msgOut = ClientAPI.newRequest().permissionRespond(true, singleId, c.getConId(), c.getNick());
                } else if (OP_NO.equals(op)) {
                    inSingleChat = false;
                    msgOut = ClientAPI.newRequest().permissionRespond(false, singleId, c.getConId(), c.getNick());
                }
                break;
        }

        if (msgOut != null) {
            c.writeMessage(msgOut);
        }
    }

    private void selectSingleAction(String op) {
        if (op.equals(ACTION_EXIT_SINGLE)) {
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

    private void exitSingle() {
        c.writeMessage(ClientAPI.newRequest().exitSingle(c.getConId(), singleId));
    }

    private String selectSingle() {
        int userID;
        do {
            System.out.println(ACTION_SELECT_USER_BY_ID);
            c.writeMessage(ClientAPI.newRequest().showAllOnline());
            try {
                userID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(ACTION_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(userID);
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
                System.out.println(MENU_ALLOW_SINGLE);
                System.out.println(MENU_DENY_SINGLE);
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
        // System.out.println(respondError.toString());
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
