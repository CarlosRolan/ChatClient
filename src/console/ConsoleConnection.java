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
    private Scanner sc = new Scanner(System.in);

    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        client = new Client(sc.nextLine());
    }

    private ConsoleConnection(String nick) {
        client = new Client(nick);
    }

    private void showMenu() {
        System.out.println("===================");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
    }

    public void startMenu() {

        showMenu();
        switch (sc.nextLine()) {
            case "1":
                client.writeMessage(new Message(SHOW_ALL_ONLINE, client.getNick()));
                break;
            case "2":
                break;
            case "3":
                break;
            default:
                break;
        }
    }

    public Message getServerResponseMessage() {
        return client.readMessage();
    }

    public void listenServer() throws IOException {

        Message responMessage = getServerResponseMessage();

        if (responMessage.getAction().equals(REQUEST_RESPONSE)) {

        }

        switch (responMessage.getAction()) {
            case REQUEST_RESPONSE:
                manageResponse(responMessage);
                break;
            default:
                System.out.println("FROM SERVER {" + responMessage.toString() + "}");
                break;

        }

    }

    private void manageResponse(Message requestResponse) {
        switch (requestResponse.getAction()) {
            case SHOW_ALL_ONLINE:
                break;
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                listenServer();
            } catch (IOException e) {
                System.out.println("ERROR LISTENING");
            }
        }
    }

}
