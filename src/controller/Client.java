package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements ClientEnviroment {

	public final String CONECXION_ACCEPTED = "CONECXION_ACCEPTED";
	public final String CONECXION_REJECTED = "CONECXION_REJECTED";
	public final String COMFIRMATION_SUCCESS = "OK";

	private Socket socket;
	private String nick = "Nameless";
	private ArrayList<Long> chatIds;
	private boolean online = false;

	private BufferedReader br;
	private BufferedWriter bw;

	public boolean isOnline() {
		return this.online;
	}

	public String getUserNick() {
		return this.nick;
	}

	public void goOnline(boolean online) {
		this.online = online;
	}

	public Client() throws IOException {
		this.socket = new Socket(HOSTNAME, PORT);
	}

	public Client(String nick) {
		try {
			this.nick = nick;
			this.socket = new Socket(HOSTNAME, PORT);
			this.br = new BufferedReader(
					new InputStreamReader(this.socket.getInputStream()));
			this.bw = new BufferedWriter(
					new OutputStreamWriter(this.socket.getOutputStream()));
			
			if (presentToServer()) {
				System.out.println(CONECXION_ACCEPTED);
				this.online = true;
			} else {
				System.out.println(CONECXION_REJECTED);
				this.online = false;
			}

		} catch (UnknownHostException e) {
			this.online = false;		
		} catch (IOException e) {
			this.online = false;
		} catch (NullPointerException e) {
			System.out.println("YOU CANT WRITE NOR READ");
			this.online = false;
		}
		

	}

	public void write(String msg) throws IOException {
		System.out.println("OUT TO SERVER " + msg);
		this.bw.write(msg);
		this.bw.newLine();
		this.bw.flush();
	}

	public String read() throws IOException {
		return this.br.readLine();
	}

	public boolean isConnected() {
		return this.socket.isConnected();
	}

	private boolean presentToServer() {
		try {
			System.out.println("Presented to the server as " + this.nick);
			write(this.nick);

			System.out.println("Waiting for server CONFIRMATION");

			if (read().equals("OK")) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			System.out.println("Could not PRESENT to the server");
			this.online = false;
			return false;
		}

	}
}
