package controller.console;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.Msg;
import com.RequestCodes;

import api.ClientAPI;
import controller.ClientEnviroment;
import controller.Connection;

public class ConsoleConnection extends Thread implements RequestCodes, ConsoleActions, ClientEnviroment {

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

    public boolean inSingleChat = false;
    private Connection c = null;
    private String singleId = "0";
    private String singleNick = null;

    public Connection getConnection() {
        return c;
    }

    public boolean isInSingleChat() {
        return inSingleChat;
    }

    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        c = new Connection(sc.nextLine());
    }

    private void clearConsole() {
        System.out.print(CLEAR_CONSOLE);
        System.out.print(COLOR_RESET);
        System.out.flush();
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
        System.out.println(ACTION_EXIT_SINGLE);
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
                    System.out.println("~~Chatting with " + singleNick + "~~");
                    msgOut = ClientAPI.newRequest().permissionRespond(true, singleId, c.getConId(), c.getNick());
                } else if (OP_NO.equals(op)) {
                    inSingleChat = false;
                    msgOut = ClientAPI.newRequest().permissionRespond(false, singleId, c.getConId(), c.getNick());
                }
                break;
        }
        c.writeMessage(msgOut);
    }

    private void selectSingleAction(String op) {
        if (op.equals(".exit")) {
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

    private void sendToSingle(String txt) {
        c.writeMessage(ClientAPI.newRequest().sendSingleMsg(c.getConId(), singleId, txt));
    }

    private String selectSingle() {
        int userID;
        do {
            System.out.println(ACTION_SELECT_USER_BY_ID);
            try {
                userID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(ACTION_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(userID);
        } while (true);

    }

    private void handleRequest(Msg reqRespond) {
        // System.out.println("\n" + reqRespond.toString());
        switch (reqRespond.getAction()) {
            case REQ_SHOW_ALL_ONLINE:
                System.out.println(reqRespond.showParameters());
                break;
            case REQ_ASKED_FOR_PERMISSION:
                singleNick = reqRespond.getParameter(0);
                singleId = reqRespond.getEmisor();
                clearConsole();
                System.out.println("--" + singleNick + " wants to chat with you--");
                System.out.println(MENU_ALLOW_SINGLE);
                System.out.println(MENU_DENY_SINGLE);
                break;
            case REQ_WAITING_FOR_PERMISSION:
                System.out.println(reqRespond.getParameter(0) + " pending to accept");
                break;
            case REQ_START_SINGLE:
                inSingleChat = true;
                singleNick = reqRespond.getParameter(0);
                singleId = reqRespond.getReceptor();
                System.out.println(singleNick + " accepts the invitation");
                break;
            case REQ_EXIT_SINGLE:
                System.out.println(
                        singleNick + "[" + singleId + "]" + " has left the chat, press ENTER to back to the MAIN MENU");
                inSingleChat = false;
                singleNick = null;
                singleId = null;
        }
    }

    private void handleMessage(Msg responMessage) {
        System.out.print(COLOR_GREEN);
        switch (responMessage.getAction()) {
            case MSG_SINGLE_MSG:
                System.out.println("\t\t[" + getCurrentTime() + "]" + singleNick + ": " + responMessage.getBody());
                break;
        }
    }

    private void handleError(Msg respondError) {
        System.out.print(COLOR_RED);
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

    private void listenServer() {
        Msg respond = c.readMessage();
        if (respond != null) {
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
        }
        System.out.print(COLOR_RESET);
    }

    // PUBLIC METHODS
    public void printIntro() {
        System.out.println(INTRO);
    }

    public void startSesion() {
        if (inSingleChat) {
            showSingleMenu();
            selectSingleAction(sc.nextLine());
        } else {
            System.out.println(LISTENNIG + PORT);
            showMainMenu();
            selectMainAction(sc.nextLine());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                listenServer();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("SERVER IS OUT");
                System.out.println("Disconecting...");
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }

}
