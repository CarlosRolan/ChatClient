package console;

import java.io.IOException;
import java.util.Scanner;

import controller.Client;

public class ConsoleConnection extends Thread implements ConsoleCommands {

    private Client client = null;
    private Scanner sc = new Scanner(System.in);

    private static ConsoleConnection instance;

    public static ConsoleConnection getInstance() {
        if (instance == null) {
            instance = new ConsoleConnection();
        }
        return instance;
    }

    public void retryConnection() {
        this.client = new Client(this.client.getUserNick());
        if (this.client.isOnline()) {
            printMenu();
        }
    }

    public Client getClient() {
        return this.client;
    }

    private ConsoleConnection() {
        System.out.println("__Write a nickName__:");
        this.client = new Client(sc.nextLine());
    }

    private ConsoleConnection(String nick) {
        this.client = new Client(nick);
    }

    public void printMenu() {
        System.out.println("===================");
        System.out.println("1.Show online users");
        System.out.println("2.Chat with \"userID\"");
        System.out.println("3.Talk to SERVER");
    }

    public void startMenu() {

        printMenu();

        if (this.client.isOnline()) {

            switch (sc.nextLine()) {
                case "1":
                    sendRequest(SHOW_ONLINE);
                    break;

                case "2": 
                    sendRequest(NEW_CHAT);
                    break;

                case "3":

                    break;

                default:
                    break;
            }
        } else {
            sc.reset();
        }

    }

    private String dots() {
        if (dots().length() > 3) {
            return ".";
        } else {
            return dots() + ".";
        }
    }

    public void sendRequest(final String command) {
        try {
            this.client.write(command);
        } catch (IOException e) {
            System.out.println("NOT SEND to SERVER");
        }
    }

    public void writeByConsole(String statement) {
        try {
            System.out.println(statement);
            this.client.write(sc.nextLine());
        } catch (IOException e) {
            System.out.println("NOT SEND to SERVER");
        }
    }

    public void listenServer() {
        while (true) {
            try {
                System.out.println("FROM SERVER {" + this.client.read() + "}");
            } catch (NullPointerException | IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println("Waiting to server response");
                this.client.goOnline(false);

                if (!getClient().isOnline()) {
                    retryConnection();
                }

            }
        }
    }

    @Override
    public void run() {
        listenServer();
    }

}
