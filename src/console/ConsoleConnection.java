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
        if (ConsoleConnection.lastLineIn.equals(Action_ASK_PERMISSION)) {
            client.setChatting(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            writeByConsole(sc.nextLine());
        } else if(!client.isChatting()) {
            showMenu();
            switch (sc.nextLine()) {
                case "1":
                    sendRequest(SHOW_ONLINE);
                    break;
                case "2":
                client.setChatting(true);
                    sendRequest(SHOW_ONLINE);
                    System.out.println(ACTION_SELECT_USER_TO_CHAT);
                    String selectedUser = sc.nextLine();
                    sendRequest(NEW_CHAT + selectedUser);
                
                    break;
                case "3":
                    break;
                default:
                    break;
            }
        } else if (client.isChatting()) {
            System.out.println("SEND MSG TO USER");
            writeByConsole();
        }
            
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

    public void listenServer() {
        try {
            ConsoleConnection.lastLineIn = client.read();
            System.out.println("FROM SERVER {" + lastLineIn + "}");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            listenServer();
        }
    }

}
