import controller.console.ConsoleConnection;

public class Main {

  public static void main(String[] args) {
    ConsoleConnection cc = ConsoleConnection.getInstance();
    //We listen the server in a sub-Thread
    cc.start();
    while (true) {
      //Start the Console User Interface
      cc.startSesion();
    }
  }
}
