package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements ClientEnviroment, Log {

	private Socket socket = null;
	private String pNick = "Nameless";
	private ArrayList<Long> chatIds;
	private BufferedReader br = null;
	private BufferedWriter bw = null;

	private boolean chatting = false;

	public boolean isChatting() {
		return chatting;
	}

	public void setChatting(Boolean chatting) {
		this.chatting = chatting;
	}

	public Client() throws IOException {
		socket = new Socket(HOSTNAME, PORT);
	}

	public Client(String nick) {
		try {
			pNick = nick;
			socket = new Socket(HOSTNAME, PORT);
			br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));

			if (presentToServer()) {
				System.out.println(INFO_CONECXION_ACCEPTED);
			} else {
				System.out.println(INFO_CONECXION_REJECTED);
			}

		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
		} catch (IOException e) {
			System.out.println("IOEx");
		} catch (NullPointerException e) {
			System.out.println("Null pointer");
		}

	}

	private boolean presentToServer() {
		try {
			System.out.println(INFO_PRESENTATION_START + pNick);
			write(pNick);
			if (read().equals(INFO_COMFIRMATION_SUCCESS)) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			System.out.println(ERROR_PRESENTATION);
			return false;
		}

	}

	public void startChatWith() {
		chatting = true;
		System.out.println("~~~~~");
	}

	public void write(String msg) throws IOException {
		System.out.println(mgsToServer + msg);
		bw.write(msg);
		bw.newLine();
		bw.flush();
	}

	public String read() throws IOException {
		return br.readLine();
	}

	public boolean isConnected() throws NullPointerException {
		return socket.isConnected();
	}

	

}
