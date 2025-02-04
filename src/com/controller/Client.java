package com.controller;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import javax.swing.JDialog;

import com.comunication.*;

import javafx.scene.control.TextInputDialog;

public class Client implements ClientEnviroment, RequestAPI, ResponseCommands {

	private static Client clientInstance = getClientInstance();

	public static Client getClientInstance() {
		if (clientInstance == null) {
			String userName = null;
			// Mostrar diálogo antes de cargar la interfaz
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Inicio de sesión");
			dialog.setHeaderText("Bienvenido al Chat");
			dialog.setContentText("Por favor, ingresa tu nombre de usuario:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent() && !result.get().trim().isEmpty()) {
				userName = result.get().trim();
			} else {
				System.out.println("El usuario canceló la entrada o ingresó un nombre vacío.");
				System.exit(0); // Cerrar la aplicación si no hay nombre
			}

			clientInstance = new Client(userName);
		}
		return clientInstance;
	}

	private String serverId = null;
	private Socket socket = null;
	private String pNick = "Nameless";
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public String getNick() {
		return pNick;
	}

	public String getServerId() {
		return serverId;
	}

	public Client() throws IOException {
		socket = new Socket(HOSTNAME, PORT);
	}

	public Client(String nick) {
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
			System.out.println("UnknownHostException");
		} catch (IOException e) {
			System.out.println("IOEx");
		} catch (NullPointerException e) {
			System.out.println("Null pointer");
		}
	}

	private boolean presentToServer() {
		System.out.println("Sending presentation");
		Message presentation = new Message(PRESENTATION, getNick());
		writeMessage(presentation);

		Message presentationResponse = readMessage();
		if (presentationResponse.getAction().equals(PRESENTATION_SUCCES)) {
			System.out.println("Getting Id from SERVER");
			String[] userInfo = presentationResponse.getText().split("_");
			serverId = userInfo[0];
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
			return (Message) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void requestOnlineUsers() {
		Message onlineUsers = new Message(SHOW_ALL_ONLINE);
		writeMessage(onlineUsers);
	}

	public void notifyImOnline() {
		Message onlineUser = new Message("USER_ONLINE", getServerId(), "SERVER");
		writeMessage(onlineUser);
	}

	public void sendMsgToChat(String receptorId, String txt) {
		writeMessage(new Message("TO_CHAT", getServerId(), receptorId, txt));
	}
}
