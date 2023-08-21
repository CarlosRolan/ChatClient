package CLI;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.chat.Chat;
import com.comunication.ApiCodes;
import com.comunication.Connection;
import com.comunication.MSG;
import com.comunication.PKG;
import com.comunication.handlers.IMSGHandler;
import com.comunication.handlers.IPKGHandler;

import api.ClientAPI;
import controller.ClientConnection;

public class CLIConnection extends Thread implements ApiCodes, CLIActions {

    // TODO MANAGE CHAT PERMISSION
    // TODO DELETE MEMBER
    // TODO DELETE CHAT

    // Singleton
    private static CLIConnection instance;

    private Scanner sc = new Scanner(System.in);

    public static CLIConnection getInstance() {
        System.out.println(INTRO);
        if (instance == null) {
            instance = new CLIConnection();
        }
        return instance;
    }
    // --------

    private final ClientConnection pClientCon;

    /* STATE VARS */
    private String pSingleID = "0";
    private String pSincleNick = null;
    private Chat pCurrentChat = null;

    /* GETTERS */
    public Connection getConnection() {
        return pClientCon;
    }

    public Chat getCurrentChat() {
        return pCurrentChat;
    }

    /* CONSTRUCTOR */
    private CLIConnection() {
        System.out.println(IN_SET_NICK);
        pClientCon = new ClientConnection(sc.nextLine(), IMSGHandler, IPKGhandler);
    }

    public void startConsoleSesion() {
        switch (CLIState.get()) {
            case MAIN:
                pCurrentChat = null;
                pSincleNick = null;
                pSingleID = "0";
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
                showInChatMenu(pCurrentChat.getTitle());
                selectInChatAction();
                break;

            default:
                break;
        }
    }

    public void selectMainAction() {
        MSG msgOut = null;
        String op = sc.nextLine();
        switch (op) {
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
                CLIState.get().change(CLIState.CHAT_MENU);
                break;
            // Exit program
            case OP_EXIT:
                System.exit(0);
            default:
                selectAllowOrDeny(op);
                break;
        }
        pClientCon.write(msgOut);
    }

    private void selectSingleAction() {
        String op = sc.nextLine();
        if (op.equals(OP_SINGLE_EXIT)) {
            exitSingle();
        } else {
            if (CLIState.get() == CLIState.SINGLE || pSingleID != null || pSincleNick != null) {
                pClientCon.sendToSingleReq(pSingleID, op);
            }
        }
    }

    private void selectAllowOrDeny(String op) {
        MSG msgOut = null;
        if (OP_ALLOW.equals(op)) {
            msgOut = ClientAPI.newRequest().permissionRespond(true, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
            CLIState.get().change(CLIState.SINGLE);
        } else if (OP_DENY.equals(op)) {
            CLIState.get().change(CLIState.MAIN);
            msgOut = ClientAPI.newRequest().permissionRespond(false, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
        } else {
            // state = ConsoleState.MAIN;
            // OJO
        }

        if (msgOut != null) {
            pClientCon.write(msgOut);
        }
    }

    private void selectChatsAction() {
        switch (sc.nextLine()) {
            // Enter Chat
            case OP_1:
                pClientCon.write(
                        ClientAPI.newRequest().selectChat(selectChatById(), pClientCon.getConId()));
                break;
            // Create Chat
            case OP_2:
                // TODO create a way to add new members from the creation of the Chat
                System.out.println(IN_SET_CHAT_TITLE);
                String chatTitle = sc.nextLine();
                System.out.println(IN_SET_CHAT_DESC);
                String chatDesc = sc.nextLine();
                pClientCon.createChatReq(chatTitle, chatDesc);
                break;
            // Delete Chat
            case OP_3:
                break;
            // Show all Chats u are a member of
            case OP_4:
                pClientCon.showAllChatsReq();
                break;
            // Exit Chats Menu - Back to Main
            case OP_EXIT:
                CLIState.get().change(CLIState.MAIN);
                pCurrentChat = null;
                break;
            default:
                CLIState.get().change(CLIState.CHAT);
                pCurrentChat = null;
                break;
        }
    }

    private void selectInChatAction() {

        switch (sc.nextLine()) {
            // Send MSG to Chat
            case OP_1:
                pClientCon.sendToChatReq(pCurrentChat, sc.nextLine());
                break;
            // Manage Chat
            case OP_2:
                CLIState.get().change(CLIState.CHAT_SETTINGS);
                break;
            // Show all member
            case OP_3:
                pClientCon.showAllMemberforChatReq(pCurrentChat.getChatId());
                break;
            // Add Member
            case OP_4:
                pClientCon.addMemberToChatReq(pCurrentChat, selectUser());
                break;
            // Delete Members
            case OP_5:
                // deletemember(selectUser());
                break;
            // Manage Permissions
            case OP_6:
                break;
            // Exit Chat - Back to Main
            case OP_EXIT:
                CLIState.get().change(CLIState.MAIN);
                break;
        }
    }

    private void exitSingle() {
        pClientCon.exitSingleReq(pSingleID);
        CLIState.get().change(CLIState.MAIN);
        pSingleID = null;
        pSincleNick = null;
    }

    private String selectUser() {
        int userID;
        do {
            System.out.println(IN_SELECT_USER);
            pClientCon.write(ClientAPI.newRequest().showAllOnline(pClientCon.getConId()));
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
        int chatId;
        do {
            System.out.println(IN_SELECT_CHAT);
            pClientCon.write(ClientAPI.newRequest().showAllYourChats(pClientCon.getConId()));
            try {
                chatId = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(chatId);
        } while (true);
    }

    /* ON DIFERENT THREAAAD */
    /* IMPLEMENTATIONS */
    // MSG
    private final IMSGHandler IMSGHandler = new IMSGHandler() {

        @Override
        public void handleRequest(MSG respondReq) {
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
                    break;

                case REQ_START_SINGLE:
                    // TODO diferienciate between the requester and the allower
                    CLIState.get().change(CLIState.SINGLE);
                    pSincleNick = respondReq.getParameter(0);
                    pSingleID = respondReq.getReceptor();
                    System.out.println(pSincleNick + _ACCEPTS_THE_INVITATION);
                    System.out.println("----Chatting with [" + pSingleID + "]" + pSincleNick + "----");
                    break;

                case REQ_EXIT_SINGLE:
                    System.out.println(
                            pSincleNick + "[" + pSingleID + "]" + _LEFT_SINGLE_CHAT);
                    CLIState.get().change(CLIState.MAIN);
                    pSincleNick = null;
                    pSingleID = null;
                    break;

                case REQ_INIT_CHAT:
                    pCurrentChat = Chat.instanceChat(respondReq);
                    CLIState.get().change(CLIState.CHAT);
                    if (pCurrentChat == null)
                        CLIState.get().change(CLIState.MAIN);
                    break;

                default:
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void handleMessage(MSG respondMSG) {
            changeConsoleColor(ConsoleColor.GREEN);
            switch (respondMSG.getAction()) {
                case MSG_TO_SINGLE:
                    System.out.println("\t\t[" + getCurrentTime() + "]" + pSincleNick + ": " + respondMSG.getBody());
                    break;
                case MSG_TO_CHAT:
                    if (CLIState.get() == CLIState.CHAT) {
                        System.out.println("********[" + getCurrentTime() + "]" + respondMSG.getParameter(0) + ": "
                                + respondMSG.getBody());
                    }
                    break;
                default:
                    System.out.println();
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void handleError(MSG respondError) {
            changeConsoleColor(ConsoleColor.RED);
            switch (respondError.getAction()) {

                case ERROR_CLIENT_NOT_FOUND:
                    System.out.println(FEED_CLIENT_NOT_FOUND);
                    break;

                case ERROR_CHAT_NOT_FOUND:
                    System.out.println(FEED_CHAT_NOT_FOUND);
                    break;

                case ERROR_SELF_REFERENCE:
                    System.out.println(FEED_SELF_REFERENCE);
                    break;

                default:
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }
    };

    // PKG
    private final IPKGHandler IPKGhandler = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleMixed'");
        }

        @Override
        public void handleCollection(PKG collection) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleCollection'");
        }

    };

    @Override
    public void run() {
        while (true) {
            pClientCon.listen();
        }
    }
}
