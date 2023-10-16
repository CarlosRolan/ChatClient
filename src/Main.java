import java.io.IOException;

import javax.swing.JOptionPane;

import CLI.CLI;
import GUI.GUI;

public class Main {

    private static void sleepInMs(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int selectUI() {
        // Custom button text
        Object[] options = { "Graphic Interface",
                "Console Interface" };
        return JOptionPane.showOptionDialog(null,
                "Wanna use the App or use by console?",
                "INTERFACE",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
    }

    private static void launchGraphicUI() {
        GUI appGUI = GUI.getInstance();
        appGUI.start();

        appGUI.launchApp();

        sleepInMs(100);
    }

    private static void launchCLI() {
        CLI cc = CLI.getInstance();
        cc.start();

        // We listen the server in a sub-Thread
        while (cc.isAlive()) {

            sleepInMs(100);

            try {
                cc.startConsoleSesion();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /*
     * MAIN EXECUTION
     */

    public static void main(String[] args) {

        int op = selectUI();

        switch (op) {
            case JOptionPane.YES_OPTION:
                launchGraphicUI();
                break;
            case JOptionPane.NO_OPTION:
                launchCLI();
                break;
            default:
                System.out.println("Closing localChat");
                break;
        }
    }

}
