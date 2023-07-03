import controller.console.ConsoleConnection;

public class Main {

  public static void main(String[] args) throws InterruptedException {

    ConsoleConnection cc = ConsoleConnection.getInstance();

    cc.start();
    // We listen the server in a sub-Thread

    cc.printIntro();
    while (true) {
      // Start the Console User Interface
      cc.startSesion();
      Thread.sleep(1000);
    }
  }
}
