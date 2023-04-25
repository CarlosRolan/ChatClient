import controller.console.ConsoleConnection;
import controller.log.HistoryUser;

public class Main {

  public static void main(String[] args) {

    Connection c = new Connection();
    //We listen the server in a sub-Thread

    HistoryUser.getInstance().logIn();

    cc.start();
    while (true) {
      //Start the Console User Interface
      cc.startSesion();
    }
  }
}
