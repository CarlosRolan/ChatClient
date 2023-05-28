package controller.console;

import java.util.Scanner;

import controller.Client;
import controller.Message;
import controller.Message.MsgType;
import controller.RequestAPI;
import controller.chats.Chat;
import controller.chats.Member;

public class ConsoleConnection extends Thread implements RequestAPI, ConsoleActions {
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

    private String singleNick = null;
    private boolean inSingleChat = false;
    private boolean inChatGroup = false;
    private Chat currentChatGroup = null;
    public Client client = null;

    public boolean isInSingleChat() {
        return inSingleChat;
    }

    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        client = new Client(sc.nextLine());
    }

    private ConsoleConnection(String nick) {
        client = new Client(nick);
    }

    private void showMainMenu() {
        System.out.println("==========|" + client.getNick() + "|==========");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
        System.out.println(MENU_OP_EXIT);
    }

    private void showChatsMenu() {
        System.out.println("~~~~" + currentChatGroup.getChatTitle() + "~~~~");
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_2);
        System.out.println(MENU_CHAT_3);
        System.out.println(MENU_CHAT_4);
        System.out.println(MENU_CHAT_5);
        System.out.println(MENU_CHAT_EXIT);
    }

    public void printIntro() {
        System.out.println(INTRO);
    }

    public void startSesion() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (inChatGroup) {
            showChatsMenu();
        } else if (!inSingleChat) {
            showMainMenu();
        }

        String op = sc.nextLine();

        if (inSingleChat) {
            sendDirectMsg(op);
        } else if (inChatGroup) {
            handleChat(op);
        } else {
            selectMainAction(op);
        }
    }

    public void selectMainAction(String op) {
        switch (op) {
            case "1":
                showAllUsers();
                break;
            case "2":
                inSingleChat = true;
                startSingle();
                break;
            case "3":
                chatMenu();
                break;
            case "a":
                // We ALLOW the requester
                inSingleChat = true;
                client.writeMessage(new Message(MsgType.REQUEST, ALLOW, client.getNick(), singleNick,
                        client.getNick() + " ALLOWS " + singleNick));
                break;
            case "b":
                // We DENY the requester
                client.writeMessage(new Message(MsgType.REQUEST, DENY, client.getNick(), singleNick));
                break;
            default:
                break;
        }
    }
    
    private void chatMenu() {
        System.out.println("1.New Chat");
        System.out.println("2.Delete chat");
        System.out.println("3.Select chat--");
        showAllChats();
        String op = sc.nextLine();
        switch (op) {
            case "1":
                newChat();
                break;
            case "2":
                break;
            case "3":
                selectChat();
                break;
        }

    }

    public void handleChat(String op) {
        switch (op) {
            // Send msg
            case "1":
                System.out.println(">Send a msg to chat");
                sendMsgToChat(sc.nextLine());
                break;
            // Add member
            case "2":
                showAllUsers();
                System.out.println("Select the ID of an user");
                addMember(currentChatGroup, sc.nextLine());
                break;
            // Delete member
            case "3":

                break;
            // Manage Permissions
            case "4":

                break;
            // Show members
            case "5":
                client.writeMessage(
                        new Message(MsgType.REQUEST, SHOW_ALL_MEMBERS, client.getNick(),
                                String.valueOf(currentChatGroup.getChatID())));

                break;
            default:
                inSingleChat = false;
                inChatGroup = false;
                break;
        }
    }

    public void listenServer() {
        Message respond = client.readMessage();
        if (respond != null) {
            switch (respond.typeOf()) {
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
    }

    private void handleRequest(Message reqRespond) {
        switch (reqRespond.getAction()) {
            case ASKED_FOR_PERMISSION:
                askingForSingle(reqRespond);
                break;
            case WAITING_FOR_PERMISSION:
                System.out.println("Waiting for " + reqRespond.getEmisor() + " to accept the CHAT");
                singleNick = reqRespond.getEmisor();
                break;
            case SHOW_ALL_ONLINE:
                for (int i = 0; i < reqRespond.getParameters().length; i++) {
                    System.out.println("\t" + reqRespond.getParameter(i));
                }
                break;
            case SHOW_ALL_CHATS:
                for (int i = 0; i < reqRespond.getParameters().length; i++) {
                    System.out.println(i + "--" + reqRespond.getParameter(i));
                }
                break;
            case SHOW_ALL_MEMBERS:
                for (int i = 0; i < reqRespond.getParameters().length; i++) {
                    System.out.println(i + "--" + reqRespond.getParameter(i));
                }
                break;
            case START_SINGLE:
                inSingleChat = true;
                System.out.println("Write .exit to exit the CHAT");
                System.out.println("====Chatting with [" + singleNick + "]====");
                break;
            case START_CHAT:
                Chat selectedChat = new Chat(reqRespond);
                client.addChat(selectedChat);
                currentChatGroup = selectedChat;
                inChatGroup = true;
                break;
            case DENY:
                inSingleChat = false;
                System.out.println(reqRespond.getText());
                System.out.println("Press enter to go back to the MENU");
                break;
            case CHAT_REGISTERED:
                registerChat(reqRespond);
                break;
            case ADD_MEMBER:
                Member newMember = Member.newMember(reqRespond.getReceptor(), false);
                client.getChatbyID(Long.valueOf(reqRespond.getEmisor())).addMember(newMember);
                break;
        }
    }

    private void handleMessage(Message responMessage) {
        switch (responMessage.getAction()) {
            case SEND_DIRECT_MSG:
                readDirect(responMessage);
                break;
            case FROM_CHAT:
                System.out.println("[" + responMessage.getEmisor() + "]:" + responMessage.getText());
                break;
            default:
                System.out.println("FROM {" + responMessage.toString() + "}");
                break;
        }
    }

    private void handleError(Message respondError) {
        switch (respondError.getAction()) {
            case CLIENT_NOT_FOUND:
                inSingleChat = false;
                System.out.println(respondError.getAction());
                break;
            case CHAT_NOT_FOUND:
                inSingleChat = false;
                System.out.println(CHAT_NOT_FOUND);
                break;
            case SELF_REFERENCE:
                inSingleChat = false;
                System.out.println(respondError.getAction());
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            listenServer();
        }
    }

    private void sendMsgToChat(String text) {
        client.writeMessage(new Message(MsgType.MESSAGE, TO_CHAT, String.valueOf(currentChatGroup.getChatID()), text));
    }

    private void showAllUsers() {
        client.writeMessage(new Message(MsgType.REQUEST, SHOW_ALL_ONLINE, client.getNick()));
    }

    private void showAllChats() {
        client.writeMessage((new Message(MsgType.REQUEST, SHOW_ALL_CHATS, client.getNick())));
    }

    // CHAT
    private void newChat() {
        System.out.println(">Write a TITLE for the new chat");
        String chatTitle = sc.nextLine();
        System.out.println(">Write a DESCRIPTION for the new chat");
        String chatDesc = sc.nextLine();
        client.writeMessage(new Message(MsgType.REQUEST, RequestAPI.NEW_CHAT, client.getNick(), chatTitle, chatDesc));
    }

    private void registerChat(Message msg) {
        long chatId = Long.valueOf(msg.getEmisor());
        System.out.println("PARSED CHAT ID:" + chatId);
        Chat chat = new Chat(chatId, msg.getReceptor(),
                msg.getText(), Member.newMember(client.getNick(), true));
        System.out.println(chat.toString());
        client.addChat(chat);
        currentChatGroup = chat;
        inChatGroup = true;
    }

    private void addMember(Chat chat, String memberID) {
        client.writeMessage(
                new Message(MsgType.REQUEST, ADD_MEMBER, String.valueOf(chat.getChatID()), memberID));
    }

    private void selectChat() {
        System.out.println("Select a CHAT by his ID");

        String chatID = sc.nextLine();

        while (chatID.matches(".*[a-b].*")) {
            System.out.println("Los ID de chats son numeros enteros sólo");
            chatID = sc.nextLine();
        }
        client.writeMessage(new Message(MsgType.REQUEST, START_CHAT, chatID));
    }



    // SINGLE AND DIRECT COMUNICATION PEER TO PEER
    public void startSingle() {

        showAllUsers();

        System.out.println(ACTION_SELECT_USER_BY_ID);
        String userID = sc.nextLine();

        while (userID.matches(".*[a-b].*")) {
            System.out.println("Los ID de usuario son numeros enteros sólo");
            userID = sc.nextLine();
        }

        client.writeMessage(new Message(MsgType.REQUEST, SINGLE_REQUESTED, client.getNick(), userID, BY_ID));
        System.out.println(MENU_OP_ERROR);

    }

    public void askingForSingle(Message msg) {
        System.out.println(msg.getEmisor() + " wants to CHAT with you");
        System.out.println(MENU_ALLOW_CHAT);
        System.out.println(MENU_DENY_CHAT);
        singleNick = msg.getEmisor();
    }

    private void readDirect(Message responMessage) {
        String emisorNick = responMessage.getEmisor();

        if (responMessage.getText().equals(EXIT_SINGLE)) {
            inSingleChat = false;
            System.out.println("==[" + emisorNick + "]: has LEFT the chat");
            System.out.println("Press enter to go back to the MENU");
        } else {
            System.out.println("==[" + emisorNick + "]:\"" + responMessage.getText() + "\"");
        }
    }

    public void sendDirectMsg(String text) {
        if (!text.equals(".exit")) {
            client.writeMessage(new Message(MsgType.MESSAGE, SEND_DIRECT_MSG, client.getNick(), singleNick, text));
            System.out.println("==\t\t[You]:\"" + text + "\"");
        } else {
            inSingleChat = false;
            client.writeMessage(
                    new Message(MsgType.MESSAGE, SEND_DIRECT_MSG, client.getNick(), singleNick, EXIT_SINGLE));
        }

    }

}
