import controller.console.ConsoleConnection;

public class Main {

  public static void main(String[] args) {

    ConsoleConnection cc = ConsoleConnection.getInstance();
    cc.start();

    // We listen the server in a sub-Thread
    while (cc.isAlive()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      cc.startSesion();
    }

  }

}
