import javax.swing.JOptionPane;

import CLI.CLIConnection;
import GUI.AppState;
import GUI.view.MainMenu;

public class Main {

  public static void main(String[] args) {

    // Custom button text
    Object[] options = { "Graphic Interface",
        "Console Interface" };

    int n = JOptionPane.showOptionDialog(null,
        "Wanna use the App or use by console?",
        "INTERFACE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]);

    if (n == JOptionPane.NO_OPTION) {

      CLIConnection cc = CLIConnection.getInstance();
      cc.start();

      // We listen the server in a sub-Thread
      while (cc.isAlive()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        cc.startConsoleSesion();
      }

    } else {
      AppState appGUI = AppState.getInstance();
      appGUI.start();
      MainMenu mm = new MainMenu();
      mm.setVisible(true);
      while (true) {
        appGUI.pClientCon.listen();
      }

    }

  }

}
