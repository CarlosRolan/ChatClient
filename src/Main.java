import java.io.IOException;
import java.security.Key;

import javax.swing.InputMap;

import console.ConsoleConnection;
import log.HistoryUser;

public class Main {
	public static void main(String[] args) {
		ConsoleConnection cc = ConsoleConnection.getInstance();
		//We listen the server in a sub-Thread
		
			HistoryUser.getInstance().logIn();
	
		cc.start();
		while(true) {
			//Start the Console User Interface
			cc.startSesion();
		}
		
	}
}
