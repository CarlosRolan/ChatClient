import java.security.Key;

import javax.swing.InputMap;

import console.ConsoleConnection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
