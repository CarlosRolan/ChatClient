import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JOptionPane;

import CLI.CLIConnection;
import GUI.AppState;
import GUI.view.MainMenu;

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

  public static void graphicUI() {
    AppState appGUI = AppState.getInstance();
    appGUI.start();
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new MainMenu().setVisible(true);
      }
    });

    sleepInMs(100);
  }

  public static void CLUI() {
    CLIConnection cc = CLIConnection.getInstance();
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

  public static void main(String[] args) {

    int op = selectUI();

    if (op == JOptionPane.NO_OPTION) {
      CLUI();
    } else {
      graphicUI();
    }

  }

}
