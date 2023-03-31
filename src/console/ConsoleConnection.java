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

    public static ConsoleConnection getInstance() {
        if (instance == null) {
            instance = new ConsoleConnection();
        }
        return instance;
    }
    // --------

    private Client client = null;

    private String currentChatter = null;

    private ConsoleConnection() {
        Scanner sc = new Scanner(System.in);

        System.out.println(ACTION_SET_NICK);
        client = new Client(sc.nextLine());
        sc.close();
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

    public void startMenu() {
        Scanner sc = new Scanner(System.in);
        showMenu();
        switch (sc.nextLine()) {
            case "1":
                client.writeMessage(new Message(SHOW_ALL_ONLINE, client.getNick()));
                break;
            case "2":
                requestChat();
                break;
            case "3":
                break;
            default:
                break;
        }
        sc.close();
    }

    public void requestChat() {
        Scanner sc = new Scanner(System.in);
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
        sc.close();
    }

    public void initChat(Message msg) {
        System.out.println(msg.getEmisor() + " wants to CHAT with you");
        System.out.println(MENU_ALLOW_CHAT);
        System.out.println(MENU_DENY_CHAT);

        Scanner sc = new Scanner(System.in);
        String allowChatting = null;

        allowChatting = sc.nextLine();

        if (allowChatting.equals("a")) {
            client.writeMessage(new Message(ACCEPT_CHAT, client.getNick(), msg.getEmisor()));
            currentChatter = msg.getEmisor();
        } else if (allowChatting.equals("b")) {
            client.writeMessage(new Message(REJECT_CHAT, client.getNick(), msg.getEmisor()));
            currentChatter = null;
        }
        sc.close();
    }

    public void listenServer() throws IOException {

        Message responMessage = client.readMessage();

        switch (responMessage.getAction()) {
            case SHOW_ALL_ONLINE:
                System.out.println(responMessage.getText());
                break;
            case ASKING_PERMISSION:
                initChat(responMessage);
                break;
            case WAITING_FOR_PERMISSION:
                System.out.println("Waiting for " + responMessage.getEmisor() + " to accept the CHAT");
                break;
            case CLIENT_NOT_FOUND:
                System.out.println(responMessage.getAction());
                break;
            case SELF_REFERENCE:
                System.out.println(responMessage.getAction());
                break;
            case START_CHAT:

                break;
            default:
                System.out.println("FROM {" + responMessage.toString() + "}");
                break;
        }
    }

    private void chatting() {

        Scanner sc = new Scanner(System.in);
        System.out.println(MENU_CHAT_1);
        System.out.println(MENU_CHAT_EXIT);

        String msgToChat = sc.nextLine();

        do {

            msgToChat = sc.nextLine();

            if (!msgToChat.isBlank() || !msgToChat.isEmpty()) {
                client.writeMessage(new Message(TO_CHAT, client.getNick(), currentChatter, msgToChat));
            }

        } while (!msgToChat.equals("0"));

        currentChatter = null;
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
