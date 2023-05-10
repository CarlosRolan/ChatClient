import controller.console.ConsoleConnection;
import controller.log.HistoryUser;

public class Main {

  public static void main(String[] args) {

    ConsoleConnection c = ConsoleConnection.getInstance();
    //We listen the server in a sub-Thread

    HistoryUser.getInstance().logIn();

    c.start();
    while (true) {
      //Start the Console User Interface
      c.startSesion();
    }
  }
}
