package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.chat.Chat;
import com.comunication.Connection;
import com.comunication.Msg;

import api.ClientAPI;

public class ClientConnection extends Connection implements Env {

    public final Handler pHandler;

    private List<Chat> chats = new ArrayList<Chat>();

    public ClientConnection(Socket arg0, Handler handler) {
        super(arg0);
        pHandler = handler;
    }

    public ClientConnection(String nickName, Handler handler) {
        super(nickName);
        pHandler = handler;
    }

    public void sendToSingle(String singleId, String txt) {
        writeMessage(ClientAPI.newRequest().sendSingleMsg(getConId(), singleId, txt));
    }

    public void sendToChat(Chat currentChat, String txt) {
        writeMessage(ClientAPI.newRequest().sendMsgToChat(currentChat, getConId(), getNick(), txt));
    }

    public void addMember(Chat currentChat, String memberId) {
        writeMessage(ClientAPI.newRequest().addMemberToChat(currentChat.getChatId(), memberId));
    }

    public void deletemember(Chat currentChat, String memberId) {
        writeMessage(ClientAPI.newRequest().deleteMemberinChat(currentChat.getChatId(), memberId));
    }

    public void listenServer() throws ClassNotFoundException, IOException {
        Msg respond = readMessage();
        switch (respond.PACKAGE_TYPE) {
            case REQUEST:
                pHandler.handleRequest(respond);
                break;
            case MESSAGE:
                pHandler.handleMessage(respond);
                break;
            case ERROR:
                pHandler.handleError(respond);
                break;
        }
    }

    public interface Handler {
        void handleRequest(Msg respondReq);

        void handleMessage(Msg respondMsg);

        void handleError(Msg respondError);
    }
}
