package console;

import java.io.IOException;
import java.util.Scanner;

import controller.Client;

public class ConsoleConnection extends Thread implements ConsoleCommands, ConsoleActions {

    private Client client = null;
    private Scanner sc = new Scanner(System.in);
    public static String lastLineIn = "";

    private static ConsoleConnection instance;

    public static ConsoleConnection getInstance() {
        if (instance == null) {
            instance = new ConsoleConnection();
        }
        return instance;
    }

    private ConsoleConnection() {
        System.out.println(ACTION_WRITE_NICK);
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

    private String dots() {
        if (dots().length() > 3) {
            return ".";
        } else {
            return dots() + ".";
        }
    }

    private void sendRequest(final String command) {
        try {
            client.write(command);
        } catch (IOException e) {
            System.out.println("NOT SEND to SERVER");
        }
    }

    public void startMenu() {

        showMenu();
        switch (sc.nextLine()) {
            case "1":
                sendRequest(SHOW_ONLINE);
                break;
            case "2":
                sendRequest(NEW_CHAT);
                try {
                    client.write(client.getNick());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "3":
                break;
            default:
                break;
        }

        System.out.println("SEND MSG TO USER");
    }

    public void writeByConsole(String statement) {
        try {
            System.out.println(statement);
            client.write(sc.nextLine());
        } catch (IOException e) {
            System.out.println("NOT SEND to SERVER");
        }
    }

    public void writeByConsole() {
        try {
            client.write(sc.nextLine());
        } catch (IOException e) {
            System.out.println("NOT SEND to SERVER");
        }
    }

    public void listenServer() throws IOException {

        ConsoleConnection.lastLineIn = client.read();

        switch (lastLineIn) {
            case ACTION_ASK_PERMISSION:
            System.out.println(lastLineIn);
                break;
            default:
                System.out.println("FROM SERVER {" + lastLineIn + "}");
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
