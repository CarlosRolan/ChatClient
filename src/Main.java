import console.ConsoleConnection;

public class Main {


	public static void main(String[] args) {
		ConsoleConnection cc = ConsoleConnection.getInstance();
		cc.start();
		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("ERROR SLEEPING");
			}

			cc.startMenu();
			
		}
	}
}
