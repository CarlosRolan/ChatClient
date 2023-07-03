package controller.console;

import java.util.Scanner;

import api.RequestCodes;
import controller.ClientEnviroment;
import controller.Connection;
import controller.Msg;
import controller.Msg.MsgType;

public class ConsoleConnection extends Thread implements RequestCodes, ConsoleActions, ClientEnviroment {
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
    private boolean inSingleChat = false;
    public Connection c = null;

    public Connection getConnection() {
        return c;
    }

    public boolean isInSingleChat() {
        return inSingleChat;
    }

    private ConsoleConnection() {
        System.out.println(ACTION_SET_NICK);
        c = new Connection(sc.nextLine());

    }

    private ConsoleConnection(String nick) {
        c = new Connection(nick);
    }

    private void showMainMenu() {
        System.out.println("==========|" + c.getNick() + "|==========");
        System.out.println(MENU_OP_1);
        System.out.println(MENU_OP_2);
        System.out.println(MENU_OP_3);
        System.out.println(MENU_OP_EXIT);
    }

    public void printIntro() {
        System.out.println(INTRO);
    }

    public void startSesion() {
        showMainMenu();
        String op = sc.nextLine();
        selectMainAction(op);
    }

    public void selectMainAction(String op) {
        Msg msgOut;
        switch (op) {
            case OP_1:
                msgOut = new Msg(MsgType.REQUEST);
                msgOut.setAction(SHOW_ALL_ONLINE);
                msgOut.setParameter(0, "MEEE");
                c.writeMessage(msgOut);
                break;
            case OP_2:
                break;
            case OP_3:
                break;
            default:
                if ("a".equals(op))
                    break;
                if ("b".equals(op))
                    break;
                break;
        }
    }

    private void handleRequest(Msg reqRespond) {
        switch (reqRespond.getAction()) {
            case SHOW_ALL_ONLINE:
                System.out.println(reqRespond.showParameters());
                break;

        }

    }

    private void handleMessage(Msg responMessage) {

    }

    private void handleError(Msg respondError) {

    }

    public void listenServer() {
        Msg respond = c.readMessage();
        System.out.println(respond.toString());
        if (respond != null) {
            switch (respond.PACKAGE_TYPE) {
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

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void run() {
        System.out.println("Listening server on port " + PORT);
        while (true) {
            try {
                listenServer();
            } catch (Exception e) {
                clearConsole();
                System.out.println("SERVER IS OUT");
                System.out.println("Disconecting...");
                System.out.println("Bye!");
                System.exit(0);
            }

        }
    }

}
