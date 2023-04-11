import java.security.Key;

import javax.swing.InputMap;

import console.ConsoleConnection;

public class Main {

	static boolean ctr = false;

	public static void main(String[] args) {
		
		ConsoleConnection cc = ConsoleConnection.getInstance();
		cc.start();
		while(true) {
			if (!cc.isChatting()) {
				cc.startMenu();
			} else {
				cc.sendMsgToChat();
			}
			
		}
		
	}
}
