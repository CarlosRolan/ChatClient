package controller.console;

import java.util.Scanner;

import controller.ClientOld;
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

    private String chatNick = null;
    private boolean chatting = false;
    private boolean inChatGroup = false;

    private Chat currentChat;

    public ClientOld client = null;

    public boolean isChatting() {
        return chatting;
    }

    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        client = new ClientOld(sc.nextLine());
    }

    private ConsoleConnection(String nick) {
        client = new ClientOld(nick);
    }

    private void showMenu() {
        System.out.println("==========|" + client.getNick() + "|==========");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
        System.out.println(MENU_OP_4);
        System.out.println(MENU_CHAT_EXIT);
    }

    private void showChatMenu() {
        System.out.println("~~~~\t" + currentChat.getChatName() + "~~~~");
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_2);
        System.out.println(MENU_CHAT_3);
        System.out.println(MENU_CHAT_4);
        System.out.println(MENU_CHAT_EXIT);
    }

    public void startSesion() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (inChatGroup) {
            showChatMenu();
        } else if (!chatting) {
            showMenu();
        }

        String op = sc.nextLine();
        if (chatting) {
            sendDirectMsg(op);
        } else if (inChatGroup) {
            handleChat(op);
        } else {
            selectAction(op);
        }

    }

    public void selectAction(String op) {
        switch (op) {
            case "1":
                client.writeMessage(new Message(SHOW_ALL_ONLINE, client.getNick()));
                break;
            case "2":
                chatting = true;
                startSingle();

                break;
            case "3":
                System.out.println(">Write a TITLE for the new chat");
                String chatTitle = sc.nextLine();
                System.out.println("<Write a DESCRIPTION for the new chat");
                String chatDesc = sc.nextLine();
                client.requestNewChat(chatTitle, chatDesc);
                break;
            case "4":
                selectChat();
                break;
            case "a":
                // We ALLOW the requester
                chatting = true;
                client.writeMessage(new Message(ALLOW, client.getNick(), chatNick,
                        client.getNick() + " ALLOWS " + chatNick));
                break;
            case "b":
                // We DENY the requester
                client.writeMessage(new Message(DENY, client.getNick(), chatNick));
                break;
            default:
                chatting = false;
                inChatGroup = false;
                break;
        }
    }

    public void handleChat(String op) {
        switch (op) {
            // Send msg
            case "1":

                break;
            // Add member
            case "2":

                break;
            // Delete member
            case "3":

                break;
            // Manage Permissions
            case "4":

                break;
            default:
                chatting = false;
                inChatGroup = false;
                break;
        }
    }

    public void selectChat() {
        for (Chat iter : client.getAllChats()) {
            System.out.println(iter.getChatName() + " - " + iter.getChatDesc());
            for (Member iterM : iter.getMembersList()) {
                System.out.println(iterM.getMemberInfo());
            }
        }

        String chatName = sc.nextLine();
        client.writeMessage(new Message(START_CHAT, client.getNick(), chatName));
    }

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
        chatNick = msg.getEmisor();
    }

    public void sendDirectMsg(String text) {
        if (!text.equals(".exit")) {
            client.writeMessage(new Message(SEND_DIRECT_MSG, client.getNick(), chatNick, text));
            System.out.println("==\t\t[You]:\"" + text + "\"");
        } else {
            chatting = false;
            client.writeMessage(new Message(SEND_DIRECT_MSG, client.getNick(), chatNick, EXIT_SINGLE));

        }

    }

    public void listenServer() {
        Message responMessage = client.readMessage();
        handleResponse(responMessage);
    }

    private void readSingle(Message responMessage) {
        String emisorNick = responMessage.getEmisor();

        if (responMessage.getText().equals(EXIT_SINGLE)) {
            chatting = false;
            System.out.println("==[" + emisorNick + "]: has LEFT the chat");
            System.out.println("Press enter to go back to the MENU");
        } else {
            System.out.println("==[" + emisorNick + "]:\"" + responMessage.getText() + "\"");
        }

    }

    private void handleResponse(Message responMessage) {
        if (responMessage != null) {
            switch (responMessage.getAction()) {
                case SHOW_ALL_ONLINE:
                    System.out.println(responMessage.getText());
                    break;
                case ASKED_FOR_PERMISSION:
                    askingForSingle(responMessage);
                    break;
                case WAITING_FOR_PERMISSION:
                    System.out.println("Waiting for " + responMessage.getEmisor() + " to accept the CHAT");
                    chatNick = responMessage.getEmisor();
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
                    System.out.println("====Chatting with [" + chatNick + "]====");
                    break;
                case DENY:
                    chatting = false;
                    System.out.println(responMessage.getText());
                    System.out.println("Press enter to go back to the MENU");
                    break;
                case SEND_DIRECT_MSG:
                    readSingle(responMessage);
                    break;

                case CHAT_REQUESTED:
                    inChatGroup = true;
                    System.out.println("NEW CHAT");
                    System.out.println(responMessage);
                    long chatId = Long.valueOf(responMessage.getEmisor());
                    System.out.println("PARSED CHAT ID:" + chatId);
                    Chat chat = new Chat(chatId, responMessage.getReceptor(),
                            responMessage.getText(), Member.newMember(client, true));
                    currentChat = chat;
                    client.addChat(chat);
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

}
