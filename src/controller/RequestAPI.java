package controller;

public interface RequestAPI {

    final String PRESENT = "PRESENTATION";
    final String SHOW_ALL_ONLINE = "SHOW_ALL_ONLINE";

    final String INIT_CHAT = "INIT_CHAT";
    final String CHAT_REQUESTED = "CHAT_REQUESTED";
    final String ACCEPT_CHAT = "ACCEPT_CHAT";
    final String REJECT_CHAT = "REJECT_CHAT";
    final String TO_CHAT = "TO_CHAT";

    final String INFO_PRESENTATION_START = "PRESENTED to the server as ";
    final String INFO_NO_SERVER_RESPONSE = "NO server response";
    final String INFO_WAITING_RESPONSE = "Waiting to server response";

    final String INFO_CONECXION_REJECTED = "CONECXION_REJECTED";
    final String INFO_COMFIRMATION_SUCCESS = "OK";
	final String INFO_CONECXION_ACCEPTED = "CONECXION_ACCEPTED";
     
    final String ERROR_PRESENTATION = "Could not present ";
    final String ERROR_SERVER_CONNECTION = "Could not read response from server";

}
