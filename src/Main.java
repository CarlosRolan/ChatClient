import java.security.Key;

import javax.swing.InputMap;

import console.ConsoleConnection;

public class Main {
	public static void main(String[] args) {
		ConsoleConnection cc = ConsoleConnection.getInstance();
		cc.start();
		while(true) {
			cc.startSesion();
		}
		
	}
}
