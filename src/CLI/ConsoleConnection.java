package CLI;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.chat.Chat;
import com.comunication.ApiCodes;
import com.comunication.Connection;
import com.comunication.Msg;

import api.ClientAPI;
import controller.ClientConnection;
import controller.ClientConnection.Handler;

public class ConsoleConnection extends Thread implements ApiCodes, ConsoleActions {

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

    private final ClientConnection pClientCon;

    // Console state vars
    private String pSingleID = "0";
    private String pSincleNick = null;
    private Chat pCurrentChat = null;

    // getters
    public Connection getConnection() {
        return pClientCon;
    }

    public Chat getCurrentChat() {
        return pCurrentChat;
    }

    // constructor
    private ConsoleConnection() {
        System.out.println(IN_SET_NICK);
        pClientCon = new ClientConnection(sc.nextLine(), handler);
    }

    public void startConsoleSesion() {
        switch (ConsoleState.getState()) {
            case MAIN:
                showMainMenu(pClientCon.getNick());
                selectMainAction();
                break;

            case SINGLE:
                showSingleMenu();
                selectSingleAction();
                break;

            case CHAT_MENU:
                showChatsMenu();
                selectChatsAction();
                break;

            case CHAT:
                showInChatMenu();
                selectInChatAction();
                break;

            case CHAT_SETTINGS:
                showChatMenuSettings();
                selectChatSettingsAction();
                break;

            default:
                break;
        }
    }

    private void selectMainAction() {
        Msg msgOut = null;
        switch (sc.nextLine()) {
            // Show all users online
            case OP_1:
                msgOut = ClientAPI.newRequest().showAllOnline(pClientCon.getConId());
                break;
            // Start Single-Chat (1v1)
            case OP_2:
                msgOut = ClientAPI.newRequest().askForSingle(pClientCon.getConId(), selectUser(), pClientCon.getNick());
                break;
            // Start Chats Menu
            case OP_3:
                ConsoleState.changeState(ConsoleState.CHAT_MENU);
                break;
            // Exit program
            case OP_EXIT:
                System.exit(0);
            default:
                selectAllowOrDeny();
                break;
        }
        pClientCon.writeMessage(msgOut);
    }

    private void selectSingleAction() {
        String op = sc.nextLine();
        if (op.equals(OP_SINGLE_EXIT)) {
            exitSingle();
        } else {
            if (ConsoleState.getState() == ConsoleState.SINGLE || pSingleID != null || pSincleNick != null) {
                pClientCon.sendToSingle(pSingleID, op);
            }
        }
    }

    private void selectAllowOrDeny() {
        Msg msgOut = null;
        String op = sc.nextLine();
        if (OP_ALLOW.equals(op)) {
            msgOut = ClientAPI.newRequest().permissionRespond(true, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
        } else if (OP_DENY.equals(op)) {
            ConsoleState.changeState(ConsoleState.MAIN);
            msgOut = ClientAPI.newRequest().permissionRespond(false, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
        } else {
            // state = ConsoleState.MAIN;
            // OJO
        }

        if (msgOut != null) {
            pClientCon.writeMessage(msgOut);
        }
    }

    private void selectChatsAction() {
        switch (sc.nextLine()) {
            // Enter Chat
            case OP_1:
                pClientCon.writeMessage(
                        ClientAPI.newRequest().selectChat(selectChatById(), pClientCon.getConId()));
                break;
            // Create Chat
            case OP_2:
                // TODO create a way to add new members from the creation of the Chat
                System.out.println(IN_SET_CHAT_TITLE);
                String chatTitle = sc.nextLine();
                System.out.println(IN_SET_CHAT_DESC);
                String chatDesc = sc.nextLine();
                pClientCon.writeMessage(ClientAPI.newRequest().requestNewChat(pClientCon.getConId(),
                        pClientCon.getNick(), chatTitle,
                        chatDesc));
                break;
            // Delete Chat
            case OP_3:
                break;
            // Show all Chats u are a member of
            case OP_4:
                pClientCon.writeMessage(ClientAPI.newRequest().showAllYourChats(pClientCon.getConId()));
                break;
            // Exit Chats Menu - Back to Main
            case OP_EXIT:
                ConsoleState.changeState(ConsoleState.MAIN);
                pCurrentChat = null;
                break;
            default:
                ConsoleState.changeState(ConsoleState.CHAT);
                break;
        }
    }

    private void selectInChatAction() {
        switch (sc.nextLine()) {
            // Send MSG to Chat
            case OP_1:
                pClientCon.sendToChat(pCurrentChat, sc.nextLine());
                break;
            // Manage Chat
            case OP_2:
                ConsoleState.changeState(ConsoleState.CHAT_SETTINGS);
                break;
            // Show all member
            case OP_3:
                pClientCon.writeMessage(ClientAPI.newRequest().showAllMembers(pCurrentChat.getChatId()));
                break;
            // Exit Chat - Back to Main
            case OP_EXIT:
                ConsoleState.changeState(ConsoleState.MAIN);
                break;
        }
    }

    private void selectChatSettingsAction() {
        switch (sc.nextLine()) {
            // Add Member
            case OP_1:
                pClientCon.addMember(pCurrentChat, selectUser());
                break;
            // Delete Members
            case OP_2:
                // deletemember(selectUser());
                break;
            // Exit Chat Settings - Back to Chat
            case OP_EXIT:
                ConsoleState.changeState(ConsoleState.CHAT_MENU);
                break;
        }
    }

    private void exitSingle() {
        pClientCon.writeMessage(ClientAPI.newRequest().exitSingle(pClientCon.getConId(), pSingleID));
        ConsoleState.changeState(ConsoleState.MAIN);
        pSingleID = null;
        pSincleNick = null;
    }

    private String selectUser() {
        int userID;
        do {
            System.out.println(IN_SELECT_USER);
            pClientCon.writeMessage(ClientAPI.newRequest().showAllOnline(pClientCon.getConId()));
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
        int ChatID;
        do {
            System.out.println(IN_SELECT_CHAT);
            pClientCon.writeMessage(ClientAPI.newRequest().showAllYourChats(pClientCon.getConId()));
            try {
                ChatID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(ChatID);
        } while (true);
    }

    /* Handler implementation of the requests respond from the server */
    private final ClientConnection.Handler handler = new Handler() {

        @Override
        public void handleRequest(Msg respondReq) {
            changeConsoleColor(ConsoleColor.BLUE);
            switch (respondReq.getAction()) {

                case REQ_SHOW_ALL_CON:
                    System.out.println(respondReq.showParameters());
                    break;

                case REQ_SHOW_ALL_CHAT:
                    System.out.println(respondReq.showParameters());
                    break;

                case REQ_SHOW_ALL_MEMBERS_OF_CHAT:
                    System.out.println(respondReq.showParameters());
                    break;

                case REQ_ASKED_FOR_PERMISSION:
                    pSincleNick = respondReq.getParameter(0);
                    pSingleID = respondReq.getEmisor();
                    System.out.println("--" + pSincleNick + " wants to Chat with you--");
                    System.out.println(MENU_SINGLE_ALLOW);
                    System.out.println(MENU_SINGLE_DENY);
                    break;

                case REQ_WAITING_FOR_PERMISSION:
                    System.out.println(respondReq.getParameter(0) + _PENDING_TO_ACCEPT);
                    ConsoleState.changeState(ConsoleState.SINGLE);
                    break;

                case REQ_START_SINGLE:
                    ConsoleState.changeState(ConsoleState.SINGLE);
                    pSincleNick = respondReq.getParameter(0);
                    pSingleID = respondReq.getReceptor();
                    System.out.println(pSincleNick + _ACCEPTS_THE_INVITATION);
                    System.out.println("----Chatting with [" + pSingleID + "]" + pSincleNick + "----");
                    break;

                case REQ_EXIT_SINGLE:
                    System.out.println(
                            pSincleNick + "[" + pSingleID + "]" + _LEFT_SINGLE_CHAT);
                    ConsoleState.changeState(ConsoleState.MAIN);
                    pSincleNick = null;
                    pSingleID = null;
                    break;

                case REQ_INIT_CHAT:
                    Chat c = Chat.instanceChat(respondReq);
                    pCurrentChat = c;
                    ConsoleState.changeState(ConsoleState.CHAT);
                    if (c == null)
                        ConsoleState.changeState(ConsoleState.MAIN);

                default:
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void handleMessage(Msg respondMsg) {
            changeConsoleColor(ConsoleColor.GREEN);
            switch (respondMsg.getAction()) {
                case MSG_TO_SINGLE:
                    System.out.println("\t\t[" + getCurrentTime() + "]" + pSincleNick + ": " + respondMsg.getBody());
                    break;
                case MSG_TO_CHAT:
                    if (ConsoleState.getState() == ConsoleState.CHAT) {
                        System.out.println("---Chat:" + pCurrentChat.getTitle() + "----");
                        System.out.println("\t\t[" + getCurrentTime() + "]" + respondMsg.getParameter(0) + ": "
                                + respondMsg.getBody());
                    }
                    break;
                default:
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void handleError(Msg respondError) {
            changeConsoleColor(ConsoleColor.RED);
            switch (respondError.getAction()) {

                case ERROR_CLIENT_NOT_FOUND:
                    System.out.println("There is no CLIENT with that ID");
                    break;

                case ERROR_CHAT_NOT_FOUND:
                    System.out.println("There is no Chat with that ID");
                    break;

                case ERROR_SELF_REFERENCE:
                    System.out.println("You cannot talk with your self");
                    break;

                default:
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }
    };

    @Override
    public void run() {
        while (true) {
            try {
                pClientCon.listenServer();
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("YOU ARE OFFLINE");
                break;
            }
        }
    }
}
