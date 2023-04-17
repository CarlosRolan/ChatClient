package console;

import java.io.IOException;
import java.util.Scanner;

import controller.Client;
import controller.Message;
import controller.RequestAPI;
import controller.ResponseCommands;

public class ConsoleConnection extends Thread implements RequestAPI, ConsoleActions, ResponseCommands {
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
    private Client client = null;

    private boolean chatting = false;

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

    private void showMenu() {
        System.out.println("==========|" + client.getNick() + "|==========");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
    }

    public void startSesion() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!chatting) {
            showMenu();
        }
        
        String op = sc.nextLine();
        if (chatting) {
            sendMsgToChat(op);
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
                requestChat();
                break;
            case "3":
                break;
            case "a":
                client.writeMessage(new Message(ACCEPT_CHAT, client.getNick(), chatNick, client.getNick() + " ACCEPTS " + chatNick));
                chatting = true;
                break;
            case "b":
                client.writeMessage(new Message(REJECT_CHAT, client.getNick(), chatNick));
                break;
            default:
                break;
        }
    }

    public void requestChat() {
        System.out.println(ACTION_SELECT_USER);
        System.out.println(MENU_OP_2_1);
        System.out.println(MENU_OP_2_2);
        String op = sc.nextLine();

        if (op.equals("a")) {
            System.out.println(ACTION_SELECT_USER_BY_ID);
            String userID = sc.nextLine();
            client.writeMessage(new Message(CHAT_REQUESTED, client.getNick(), userID, ACTION_SELECT_USER_BY_ID));
        } else if (op.equals("b")) {
            System.out.println(ACTION_SELECT_USER_BY_NICKNAME);
            String userNick = sc.nextLine();
            client.writeMessage(
                    new Message(CHAT_REQUESTED, client.getNick(), userNick, ACTION_SELECT_USER_BY_NICKNAME));
        } else {
            System.out.println(MENU_OP_ERROR);
        }
    }

    public void initChat(Message msg) {
        System.out.println(msg.getEmisor() + " wants to CHAT with you");
        System.out.println(MENU_ALLOW_CHAT);
        System.out.println(MENU_DENY_CHAT);
        chatNick = msg.getEmisor();
    }

    public void sendMsgToChat(String text) {
        client.writeMessage(new Message(TO_CHAT, client.getNick(), chatNick, text));
        System.out.println("==\t\t[You]:\"" + text + "\"");
    }

    public void listenServer() throws IOException {
        Message responMessage = client.readMessage();
        handleResponse(responMessage);
    }

    private void readChat(Message responMessage) {
        String emisorNick = responMessage.getEmisor();
        System.out.println("==[" + emisorNick + "]:\"" + responMessage.getText() + "\"");
    }

    private void handleResponse(Message responMessage) {
        switch (responMessage.getAction()) {
            case SHOW_ALL_ONLINE:
                System.out.println(responMessage.getText());
                break;
            case ASKED_FOR_PERMISSION:
                initChat(responMessage);
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
            case START_CHAT:
                chatting = true;
                System.out.println("====" + chatNick + "====");
                break;
            case REJECT_CHAT:
                chatting = false;
                System.out.println(responMessage.getText());
                break;
            case TO_CHAT:
                readChat(responMessage);
                break;
            default:
                System.out.println("FROM {" + responMessage.toString() + "}");
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                listenServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
