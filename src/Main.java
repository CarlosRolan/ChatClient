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
			cc.startMenu();
		}
		
	}
}
