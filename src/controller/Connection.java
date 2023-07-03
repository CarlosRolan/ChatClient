package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import api.RequestCodes;
import controller.Msg.MsgType;

public class Connection implements ClientEnviroment, RequestCodes {

	private Socket mSocket = null;
	private String mNick = "Nameless";
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public String getNick() {
		return mNick;
	}

	public Connection(String nick) {

		boolean exception = false;
		String dots = "";

		do {
			exception = false;
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			try {
				mNick = nick;
				mSocket = new Socket(HOSTNAME, PORT);

				oos = new ObjectOutputStream(mSocket.getOutputStream());
				ois = new ObjectInputStream(mSocket.getInputStream());

				if (presentToServer()) {
					System.out.println(INFO_CONECXION_ACCEPTED);
				} else {
					System.out.println(INFO_CONECXION_REJECTED);
				}
			} catch (ConnectException e) {
				exception = true;
				System.out.print("\033[H\033[2J");
				System.out.println("Unable to coonect to the server. Trying." + dots);
				dots = dots.concat(".");
				if (dots.length() == 5) {
					dots = "";
				}
				System.out.flush();
			} catch (UnknownHostException e) {
				exception = true;
				System.out.println("UnknownHostException");
			} catch (IOException e) {
				exception = true;
				System.out.println("IOException");
			} catch (NullPointerException e) {
				exception = true;
				System.out.println("NullPointerException");
			}
		} while (exception);

	}

	private boolean presentToServer() {
		Msg presentation = new Msg(MsgType.REQUEST);
		presentation.setAction(PRESENT);
		presentation.setEmisor(getNick());
		writeMessage(presentation);

		Msg presentationResponse = readMessage();
		if (presentationResponse.getAction().equals(PRESENTATION_SUCCES)) {
			return true;
		} else {
			return false;
		}
	}

	public void writeMessage(Msg msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Msg readMessage() {
		try {
			return (Msg) ois.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

}
