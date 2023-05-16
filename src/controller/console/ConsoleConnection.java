package controller.console;

import java.util.Scanner;

import controller.Client;
import controller.Message;
import controller.RequestAPI;
import controller.StatusCodes;
import controller.chats.Chat;
import controller.chats.Member;

public class ConsoleConnection extends Thread implements RequestAPI, ConsoleActions, StatusCodes {
    // Singleton
    private static ConsoleConnection instance;

    private static Scanner sc = new Scanner(System.in);

    public static ConsoleConnection getInstance() {
        if (instance == null) {
            instance = new ConsoleConnection();
        }
        return instance;
    }
    // --------

    private String chatTitle = null;
    private boolean chatting = false;
    private boolean inChatGroup = false;
    private Chat currentChat = null;
    public Client client = null;

    public boolean isChatting() {
        return chatting;
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
        System.out.println(MENU_OP_4);
        System.out.println(MENU_OP_EXIT);
    }

    private void showChatMenu() {
        System.out.println("~~~~" + currentChat.getChatTitle() + "~~~~");
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_2);
        System.out.println(MENU_CHAT_3);
        System.out.println(MENU_CHAT_4);
        System.out.println(MENU_CHAT_5);
        System.out.println(MENU_CHAT_EXIT);
    }

    public void startSesion() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (inChatGroup) {
            showChatMenu();
        } else if (!chatting) {
            showMainMenu();
        }

        String op = sc.nextLine();

        if (chatting) {
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
                chatting = true;
                startSingle();

                break;
            case "3":
                newChat();
                break;
            case "4":
                selectChat();
                break;
            case "a":
                // We ALLOW the requester
                chatting = true;
                client.writeMessage(new Message(ALLOW, client.getNick(), chatTitle,
                        client.getNick() + " ALLOWS " + chatTitle));
                break;
            case "b":
                // We DENY the requester
                client.writeMessage(new Message(DENY, client.getNick(), chatTitle));
                break;
            default:
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
                System.out.println("Select the nick of an user");
                Member newMember = Member.newMember(sc.nextLine(), false);
                addMember(currentChat, newMember);
                break;
            // Delete member
            case "3":

                break;
            // Manage Permissions
            case "4":

                break;
            // Show members
            case "5":

                break;
            default:
                chatting = false;
                inChatGroup = false;
                break;
        }
    }

    public void listenServer() {
        Message responMessage = client.readMessage();
        handleResponse(responMessage);
    }

    private void handleResponse(Message responMessage) {
        if (responMessage != null) {
            switch (responMessage.getAction()) {
                case SHOW_ALL_ONLINE:
                    System.out.println(responMessage.getText());
                    break;
                case SHOW_ALL_CHATS:
                    System.out.println(responMessage.getText());
                    break;
                case ASKED_FOR_PERMISSION:
                    askingForSingle(responMessage);
                    break;
                case WAITING_FOR_PERMISSION:
                    System.out.println("Waiting for " + responMessage.getEmisor() + " to accept the CHAT");
                    chatTitle = responMessage.getEmisor();
                    break;
                case CLIENT_NOT_FOUND:
                    chatting = false;
                    System.out.println(responMessage.getAction());
                    break;
                case SELF_REFERENCE:
                    chatting = false;
                    System.out.println(responMessage.getAction());
                    break;
                case START_SINGLE:
                    chatting = true;
                    System.out.println("Write .exit to exit the CHAT");
                    System.out.println("====Chatting with [" + chatTitle + "]====");
                    break;
                case DENY:
                    chatting = false;
                    System.out.println(responMessage.getText());
                    System.out.println("Press enter to go back to the MENU");
                    break;
                case SEND_DIRECT_MSG:
                    readDirect(responMessage);
                    break;

                case CHAT_REGISTERED:
                    registerChat(responMessage);
                    break;

                case START_CHAT:

                    long chatId = Long.valueOf(responMessage.getEmisor());
                    currentChat = client.getChatbyID(chatId);
                    chatTitle = currentChat.getChatTitle();
                    inChatGroup = true;
                    break;
                case FROM_CHAT:
                    System.out.println("[" + responMessage.getEmisor() + "]:" + responMessage.getText());
                    break;

                case ADD_MEMBER:
                    addMember(client.getChatbyID(MAX_PRIORITY), null);
                    break;

                default:
                    System.out.println("FROM {" + responMessage.toString() + "}");
                    break;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            listenServer();
        }
    }

    private void sendMsgToChat(String text) {
        client.writeMessage(new Message(TO_CHAT, String.valueOf(currentChat.getChatID()), text));
    }

    private void showAllUsers() {
        client.writeMessage(new Message(SHOW_ALL_ONLINE, client.getNick()));
    }

    private void showAllChats() {
        client.writeMessage((new Message(SHOW_ALL_CHATS, client.getNick())));
    }

    // CHAT
    private void newChat() {
        System.out.println(">Write a TITLE for the new chat");
        String chatTitle = sc.nextLine();
        System.out.println(">Write a DESCRIPTION for the new chat");
        String chatDesc = sc.nextLine();
        client.writeMessage(new Message(RequestAPI.NEW_CHAT, client.getNick(), chatTitle, chatDesc));
    }

    private void registerChat(Message msg) {
        long chatId = Long.valueOf(msg.getEmisor());
        System.out.println("PARSED CHAT ID:" + chatId);
        Chat chat = new Chat(chatId, msg.getReceptor(),
                msg.getText(), Member.newMember(client, true));
        System.out.println(chat.toString());
        client.addChat(chat);
        currentChat = client.getChatbyID(chatId);
        chatTitle = currentChat.getChatTitle();
        inChatGroup = true;
    }

    private void addMember(Chat chat, Member newMember) {
        chat.addMember(newMember);
        currentChat = chat;
        client.writeMessage(new Message(ADD_MEMBER, String.valueOf(chat.getChatID()), newMember.getMemberName()));
    }

    private void selectChat() {
        showAllChats();

        System.out.println("<Write the chat id [XXX]");
        String chatID = sc.nextLine();

        while (chatID.matches(".*[a-b].*")) {
            System.out.println("Los ID de chats son numeros enteros sólo");
            chatID = sc.nextLine();
        }

        client.writeMessage(new Message(START_CHAT, chatID));

    }

    // SINGLE AND DIRECT COMUNICATION PEER TO PEER
    public void startSingle() {
        System.out.println(ACTION_SELECT_USER);
        System.out.println(MENU_OP_2_1);
        System.out.println(MENU_OP_2_2);
        String op = sc.nextLine();

        if (op.equals("a")) {
            System.out.println(ACTION_SELECT_USER_BY_ID);
            String userID = sc.nextLine();

            while (userID.matches(".*[a-b].*")) {
                System.out.println("Los ID de usuario son numeros enteros sólo");
                userID = sc.nextLine();
            }

            client.writeMessage(new Message(SINGLE_REQUESTED, client.getNick(), userID, BY_ID));
        } else if (op.equals("b")) {
            System.out.println(ACTION_SELECT_USER_BY_NICKNAME);
            String userNick = sc.nextLine();
            client.writeMessage(new Message(SINGLE_REQUESTED, client.getNick(), userNick, BY_NICK));
        } else {
            System.out.println(MENU_OP_ERROR);
        }
    }

    public void askingForSingle(Message msg) {
        System.out.println(msg.getEmisor() + " wants to CHAT with you");
        System.out.println(MENU_ALLOW_CHAT);
        System.out.println(MENU_DENY_CHAT);
        chatTitle = msg.getEmisor();
    }

    private void readDirect(Message responMessage) {
        String emisorNick = responMessage.getEmisor();

        if (responMessage.getText().equals(EXIT_SINGLE)) {
            chatting = false;
            System.out.println("==[" + emisorNick + "]: has LEFT the chat");
            System.out.println("Press enter to go back to the MENU");
        } else {
            System.out.println("==[" + emisorNick + "]:\"" + responMessage.getText() + "\"");
        }
    }

    public void sendDirectMsg(String text) {
        if (!text.equals(".exit")) {
            client.writeMessage(new Message(SEND_DIRECT_MSG, client.getNick(), chatTitle, text));
            System.out.println("==\t\t[You]:\"" + text + "\"");
        } else {
            chatting = false;
            client.writeMessage(new Message(SEND_DIRECT_MSG, client.getNick(), chatTitle, EXIT_SINGLE));
        }

    }

}
