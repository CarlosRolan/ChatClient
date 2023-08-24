package controller.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.chat.Chat;
import com.comunication.Connection;
import com.comunication.handlers.IMSGHandler;
import com.comunication.handlers.IPKGHandler;

import api.ClientAPI;

public class ClientConnection extends Connection implements Env {

    private List<Chat> pChats = new ArrayList<Chat>();

    /* SETTERS */

    public int getNumChats() {
        return pChats.size();
    }

    public List<Chat> getChats() {
        return pChats;
    }

    public Chat getChat(String chatId) {
        for (Chat iChat : pChats) {
            if (iChat.getChatId().equals(chatId)) {
                return iChat;
            }
        }
        return null;
    }

    public void setChats(List<Chat> updatedChats) {
        pChats = updatedChats;
    }

    public void setChat(Chat updatedChat) {
        for (Chat iChat : pChats) {
            if (iChat.getChatId().equals(updatedChat.getChatId())) {
                iChat = updatedChat;
            }
        }
    }

    /* CONSTRUCTORS */
    public ClientConnection(Socket socket, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(socket, msgHandler, pckgHandler);
    }

    public ClientConnection(String nickName, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(nickName, msgHandler, pckgHandler);

    }

    /* PUBLIC */
    /* REQUEST */
    public void showAllChatsReq() throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllYourChats(getConId()));
    }

    public void showAllMemberforChatReq(String chatId) throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllMembers(chatId));
    }

    public void sendToSingleReq(String singleId, String txt) throws SocketException, IOException {
        write(ClientAPI.newRequest().sendSingleMsg(getConId(), singleId, txt));
    }

    public void sendToChatReq(Chat currentChat, String txt) throws SocketException, IOException {
        write(ClientAPI.newRequest().sendMsgToChat(currentChat, getConId(), getNick(), txt));
    }

    public void exitSingleReq(String singleId) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitSingle(getConId(), singleId));
    }

    public void exitChatReq(Chat selectedChat) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChat(getConId(), selectedChat.getChatId()));
    }

    public void deleteChatReq(Chat deletedChat) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChat(getConId(), deletedChat.getChatId()));
    }

    public void addMemberToChatReq(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().addMemberToChat(currentChat.getChatId(), memberId));
    }

    public void deletememberReq(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().deleteMemberinChat(currentChat.getChatId(), memberId));
    }

    public void createChatReq(String title, String desc) throws SocketException, IOException {
        write(
                ClientAPI.newRequest().requestNewChat(getNumChats(), getConId(),
                        getNick(), title,
                        desc));
    }

    public void refreshStateReq() throws SocketException, IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        write(ClientAPI.newRequest().updateState(getConId(), dtf.format(now)));
    }

    /* RESPONDS */
    public void createChat(Chat newChat) {
        pChats.add(newChat);
    }

    public void updateChat(Chat updated) {
        for (Chat chat : pChats) {
            if (updated.getChatId().equals(chat.getChatId())) {
                chat = updated;
            }
        }
    }

    public void addChatToLocal(Chat chat) {
        pChats.add(chat);
    }

    /* OVERRIDE */

}
