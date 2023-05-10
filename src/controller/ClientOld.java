package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controller.chats.Chat;
import controller.chats.Member;

public class ClientOld implements ClientEnviroment, RequestAPI, StatusCodes{

	private Socket socket = null;
	private String pNick = "Nameless";
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ArrayList<Chat> chats = new ArrayList<>();

	public void addChat(Chat c) {
		chats.add(c);
	}
	public String getNick() {
		return pNick;
	}
	public Chat getChat(Chat selected) {
		int index = chats.indexOf(selected);
		return chats.get(index);
	}
	public Chat getChat(int index) {
		return chats.get(index);
	}
	public ArrayList<Chat> getAllChats() {
		return chats;
	}

	public ClientOld() throws IOException {
		socket = new Socket(HOSTNAME, PORT);
	}

	public ClientOld(String nick) {
		try {
			pNick = nick;
			socket = new Socket(HOSTNAME, PORT);

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			if (presentToServer()) {
				System.out.println(INFO_CONECXION_ACCEPTED);
			} else {
				System.out.println(INFO_CONECXION_REJECTED);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private boolean presentToServer() {
		Message presentation = new Message(PRESENT, getNick());
		
		writeMessage(presentation);

		Message presentationResponse = readMessage();
		if (presentationResponse.getAction().equals(PRESENTATION_SUCCES)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isConnected() {
		return socket.isConnected();
	}

	public void writeMessage(Message msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Message readMessage() {
		try {
			return (Message) ois.readObject() ;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void requestNewChat(String chatName, String chatDesc) {
        writeMessage(new Message(RequestAPI.CHAT_REQUESTED, getNick(), chatName, chatDesc));
    }


	



	

	
}
