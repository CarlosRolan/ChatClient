package CLI;

import java.io.IOException;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.api.Codes;
import com.chat.Chat;
import com.controller.Connection;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;
import com.data.MSG;
import com.data.PKG;

import api.ClientAPI;
import controller.connection.ClientConnection;
import controller.manager.FileManager;

public class CLI extends Thread implements Codes, CLIActions {

    // TODO MANAGE CHAT PERMISSION
    // TODO DELETE MEMBER
    // TODO DELETE CHAT

    // Singleton
    private static CLI instance;

    private Scanner sc = new Scanner(System.in);

    public static CLI getInstance() {
        System.out.println(INTRO);
        if (instance == null) {
            instance = new CLI();
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
    private CLI() {
        System.out.println(IN_SET_NICK);
        pClientCon = new ClientConnection(sc.nextLine(), IMSGHandler, IPKGhandler);
    }

    public void startConsoleSesion() throws SocketException, IOException {
        switch (ConsoleState.get()) {
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
                showInChatMenu(pCurrentChat.getTitle(), pCurrentChat.getMember(pClientCon.getConId()).isAdmin());
                selectInChatAction();
                break;

            default:
                break;
        }
    }

    /* ACTION SELECTORS */
    public void selectMainAction() throws SocketException, IOException {
        MSG msgOut = null;
        String op = sc.nextLine();
        switch (op) {
            // Show all users online
            case OP_1:
                msgOut = ClientAPI.newRequest().showAllOnlineReq(pClientCon.getConId());
                break;
            // Start Single-Chat (1v1)
            case OP_2:
                msgOut = ClientAPI.newRequest().askForSingleReq(pClientCon.getConId(), selectUser(),
                        pClientCon.getNick());
                break;
            // Start Chats Menu
            case OP_3:
                ConsoleState.get().change(ConsoleState.CHAT_MENU);
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

    private void selectSingleAction() throws SocketException, IOException {
        String op = sc.nextLine();
        if (op.equals(OP_SINGLE_EXIT)) {
            exitSingle();
        } else {
            if (ConsoleState.get() == ConsoleState.SINGLE || pSingleID != null || pSincleNick != null) {
                pClientCon.sendToSingle(pSingleID, pSincleNick, op);
            }
        }
    }

    private void selectAllowOrDeny(String op) throws SocketException, IOException {
        MSG msgOut = null;
        if (OP_ALLOW.equals(op)) {
            msgOut = ClientAPI.newRequest().permissionRespondReq(true, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
            ConsoleState.get().change(ConsoleState.SINGLE);
        } else if (OP_DENY.equals(op)) {
            ConsoleState.get().change(ConsoleState.MAIN);
            msgOut = ClientAPI.newRequest().permissionRespondReq(false, pSingleID, pClientCon.getConId(),
                    pClientCon.getNick());
        } else {
            // state = ConsoleState.MAIN;
            // OJO
        }

        if (msgOut != null) {
            pClientCon.write(msgOut);
        }
    }

    private void selectChatsAction() throws SocketException, IOException {
        switch (sc.nextLine()) {
            // Enter Chat
            case OP_1:
                pClientCon.write(
                        ClientAPI.newRequest().selectChatReq(selectChatById(), pClientCon.getConId(),
                                pClientCon.getNumChats()));
                break;
            // Create Chat
            case OP_2:
                // TODO create a way to add new members from the creation of the Chat
                System.out.println(IN_SET_CHAT_TITLE);
                String chatTitle = sc.nextLine();
                System.out.println(IN_SET_CHAT_DESC);
                String chatDesc = sc.nextLine();
                pClientCon.createNewChat(chatTitle, chatDesc, null);
                break;
            // Delete Chat
            case OP_3:
                break;
            // Show all Chats u are a member of
            case OP_4:
                pClientCon.showAllChats();
                break;
            // Exit Chats Menu - Back to Main
            case OP_EXIT:
                ConsoleState.get().change(ConsoleState.MAIN);
                pCurrentChat = null;
                break;
            default:
                ConsoleState.get().change(ConsoleState.CHAT);
                pCurrentChat = null;
                break;
        }
    }

    private void selectInChatAction() throws SocketException, IOException {

        switch (sc.nextLine()) {
            // Send MSG to Chat
            case OP_1:
                //pClientCon.sendToChat(pCurrentChat, sc.nextLine());
                break;
            // Manage Chat
            case OP_2:
                ConsoleState.get().change(ConsoleState.CHAT_SETTINGS);
                break;
            // Show all member
            case OP_3:
                pClientCon.showAllMemberforChat(pCurrentChat.getChatId());
                break;
            // Add Member
            case OP_4:
                pClientCon.addMemberToChat(pCurrentChat, selectUser());
                break;
            // Delete Members
            case OP_5:
                if (pCurrentChat.getMember(pClientCon.getConId()).isAdmin()) {
                    pClientCon.deleteChat(pCurrentChat);
                }
                break;
            // Manage Permissions
            case OP_6:
                break;
            // Exit Chat - Back to Main
            case OP_EXIT:
                ConsoleState.get().change(ConsoleState.MAIN);
                break;
        }
    }

    private String selectUser() throws SocketException, IOException {
        int userID;
        do {
            System.out.println(IN_SELECT_USER);
            pClientCon.write(ClientAPI.newRequest().showAllOnlineReq(pClientCon.getConId()));
            try {
                userID = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(userID);
        } while (true);
    }

    private String selectChatById() throws SocketException, IOException {
        int chatId;
        do {
            System.out.println(IN_SELECT_CHAT);
            pClientCon.write(ClientAPI.newRequest().showAllYourChatsReq(pClientCon.getConId()));
            try {
                chatId = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(IN_SELECT_SINGLE);
                continue;
            }
            return String.valueOf(chatId);
        } while (true);
    }

    private void exitSingle() throws SocketException, IOException {
        pClientCon.exitSingle(pSingleID);
        ConsoleState.get().change(ConsoleState.MAIN);
        pSingleID = null;
        pSincleNick = null;
    }

    /* LISTENING THREAD */
    @Override
    public void run() {
        while (true) {
            try {
                pClientCon.listen();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

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
                    ConsoleState.get().change(ConsoleState.SINGLE);
                    pSincleNick = respondReq.getParameter(0);
                    pSingleID = respondReq.getReceptor();
                    System.out.println(pSincleNick + _ACCEPTS_THE_INVITATION);
                    System.out.println("----Chatting with [" + pSingleID + "]" + pSincleNick + "----");
                    break;

                case REQ_EXIT_SINGLE:
                    System.out.println(pSincleNick + "[" + pSingleID + "]" + _LEFT_SINGLE_CHAT);
                    ConsoleState.get().change(ConsoleState.MAIN);
                    pSincleNick = null;
                    pSingleID = null;
                    break;

                // TODO make difeerent when created new chat and wehn added
                case REQ_INIT_CHAT:
                    pClientCon.oneMoreChat();
                    pCurrentChat = Chat.instanceChat(respondReq);
                    ConsoleState.get().change(ConsoleState.CHAT);
                    FileManager.getInstance().saveChatRaw(pCurrentChat);
                    if (pCurrentChat == null)
                        ConsoleState.get().change(ConsoleState.MAIN);
                    break;

                default:
                    System.out.println(WARN_UNREGISTERED_MSG_REQUEST_ACTION);
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void handleMessage(MSG respondMSG) {
            changeConsoleColor(ConsoleColor.GREEN);
            switch (respondMSG.getAction()) {

                case MSG_FROM_SINGLE:
                    System.out.println("\t\t[" + getCurrentTime() + "]" + pSincleNick + ": " + respondMSG.getBody());
                    break;

                case MSG_FROM_CHAT:
                    if (ConsoleState.get() == ConsoleState.CHAT) {
                        System.out.println("********[" + getCurrentTime() + "]" + respondMSG.getParameter(0) + ": "
                                + respondMSG.getBody());
                    }
                    break;

                default:
                    System.out.println(WARN_UNREGISTERED_MSG_MESSAGE_ACTION);
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
                    System.out.println(WARN_UREGISTERED_MSG_ERROR_ACTION);
                    break;
            }
            changeConsoleColor(ConsoleColor.DEFAULT);
        }

        @Override
        public void unHandledMSG(MSG arg0) {
            System.out.println("MSG TYPE UNHANDLED");
        }

    };

    // PKG
    private final IPKGHandler IPKGhandler = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
            switch (mixed.getPKGName()) {
                default:
                    System.out.println(WARN_UNREGISTERED_PKG_MIXED_ACTION);
                    break;
            }
        }

        @Override
        public void handleCollection(PKG collection) {
            switch (collection.getPKGName()) {
                default:
                    System.out.println(WARN_UNREGISTERED_PKG_COLLECTION_ACTION);
                    break;

            }
        }

        @Override
        public void unHandledPKG(PKG arg0) {
            System.out.println("PKG TYPE UNHANDLED");
        }

    };

}
