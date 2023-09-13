package controller.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.chat.Chat;
import com.controller.Connection;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;

import api.ClientAPI;

public class ClientConnection extends Connection implements Env {

    /* CONSTRUCTORS */
    public ClientConnection(Socket socket, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(socket, msgHandler, pckgHandler);
    }

    public ClientConnection(String nickName, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(nickName, msgHandler, pckgHandler);
    }

    /* PUBLIC */
    /* REQUEST */
    public void showAllChats() throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllYourChatsReq(getConId()));
    }

    public void showAllMemberforChat(String chatId) throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllMembersReq(chatId));
    }

    // TODO we need the bnick of the requested single chat
    public void sendToSingle(String singleId, String singleNick, String txt) {
        try {
            write(ClientAPI.newRequest().sendSingleMsgReq(
                    getConId(),
                    getNick(),
                    singleId,
                    singleNick,
                    txt));
        } catch (IOException e) {

        }
    }

    public void sendToChat(String chatId, String txt) {
    
    }

    public void sendToChat(Chat currentChat, String txt) throws SocketException, IOException {
        write(ClientAPI.newRequest().sendMsgToChatReq(
                currentChat,
                getConId(),
                getNick(),
                txt));
    }

    public void exitSingle(String singleId) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitSingleReq(
                getConId(),
                singleId));
    }

    public void exitChat(Chat selectedChat) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChatReq(
                getConId(),
                selectedChat.getChatId()));
    }

    public void deleteChat(Chat deletedChat) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChatReq(
                getConId(),
                deletedChat.getChatId()));
    }

    public void addMemberToChat(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().addMemberToChatReq(
                currentChat.getChatId(),
                memberId));
    }

    public void deleteMember(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().deleteMemberinChatReq(
                currentChat.getChatId(),
                memberId));
    }

    public void newChat(String title, String desc, int numberChats) {
        try {
            write(
                    ClientAPI.newRequest().requestNewChatReq(
                            numberChats,
                            getConId(),
                            getNick(),
                            title,
                            desc));
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void refreshState() throws SocketException, IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        write(ClientAPI.newRequest().updateStateReq(getConId(), dtf.format(now)));
    }

    /* RESPONDS */

    /* OVERRIDE */

}
