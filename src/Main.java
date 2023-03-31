import java.security.Key;

import console.ConsoleConnection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {

	static boolean ctr = false;

	public static void main(String[] args) {
		ConsoleConnection cc = ConsoleConnection.getInstance();
		cc.start();

		

		while(true) {


			new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyChar() == 's') {
						ctr = true;
						System.out.println("TRUE");
					}
					if (ctr && arg0.getKeyChar() == 'm') {
						cc.startMenu();
					}
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					if (arg0.getKeyChar() == 's') {
						System.out.println("FASLE");
						ctr = false;
					}
				}


			};


	
		}
	}
}
