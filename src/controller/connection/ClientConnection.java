package controller.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.chat.Chat;
import com.chat.Member;
import com.controller.Connection;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;

import GUI.GUI;
import api.ClientAPI;
import controller.manager.FileManager;

public class ClientConnection extends Connection implements Env {

    private int mNumChats = 0;

    /* GETTERs */
    /**
     * @return the numChats
     */
    public String getNumChats() {
        return String.valueOf(mNumChats);
    }

    public void oneMoreChat() {
        mNumChats++;
    }

    /* CONSTRUCTORS */
    /**
     * 
     * @param socket
     * @param msgHandler
     * @param pckgHandler
     */
    public ClientConnection(Socket socket, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(socket, msgHandler, pckgHandler);
        FileManager.initInstance(this);
    }

    /**
     * 
     * @param nickName
     * @param msgHandler
     * @param pckgHandler
     */
    public ClientConnection(String nickName, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
        super(nickName, msgHandler, pckgHandler);
        FileManager.initInstance(this);
    }

    /* PUBLIC */
    /* REQUEST */
    /**
     * 
     * @throws SocketException
     * @throws IOException
     */
    public void showAllChats() throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllYourChatsReq(getConId()));
    }

    /**
     * 
     * @param chatId
     * @throws SocketException
     * @throws IOException
     */
    public void showAllMemberforChat(String chatId) throws SocketException, IOException {
        write(ClientAPI.newRequest().showAllMembersReq(chatId));
    }

    /**
     * 
     * @param singleId
     * @param singleNick
     * @param txt
     */
    public void sendToSingle(String singleId, String singleNick, String txt) {
        // TODO unhandled exceptions
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

    public void sendMsgToChat(String chatId, String emisorId, String emisorNick, String line) {
        // TODO unhandled exceptions
        try {
            write(ClientAPI.newRequest().sendMsgToChatReq(chatId, emisorId, line));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param singleId
     * @throws SocketException
     * @throws IOException
     */
    public void exitSingle(String singleId) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitSingleReq(
                getConId(),
                singleId));
    }

    /**
     * 
     * @param selectedChat
     * @throws SocketException
     * @throws IOException
     */
    public void exitChat(Chat selectedChat) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChatReq(
                getConId(),
                selectedChat.getChatId()));
    }

    /**
     * 
     * @param deletedChat
     * @throws SocketException
     * @throws IOException
     */
    public void deleteChat(String chatId) throws SocketException, IOException {
        write(ClientAPI.newRequest().exitChatReq(
                getConId(),
                chatId));
    }

    /**
     * 
     * @param currentChat
     * @param memberId
     * @throws SocketException
     * @throws IOException
     */
    public void addMemberToChat(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().addMemberToChatReq(
                currentChat.getChatId(),
                memberId));
    }

    /**
     * 
     * @param currentChat
     * @param memberId
     * @throws SocketException
     * @throws IOException
     */
    public void deleteMember(Chat currentChat, String memberId) throws SocketException, IOException {
        write(ClientAPI.newRequest().deleteMemberinChatReq(
                currentChat.getChatId(),
                memberId));
    }

    public boolean createNewChat(String chatTitle, String chatDec, List<String> membersToAddList) {

        if (FileManager.getInstance().initConvHistory(chatTitle, true)) {
            String[] memberRefs;
            Member creator = Member.newCreator(getConId(), getNick());

            if (membersToAddList == null) {
                // if no members(membersToAddList == null) we add a member at 0: the creator
                memberRefs = new String[1];
                memberRefs[0] = creator.getReference();
            } else {
                // we parse the membersToAddList to an Array
                memberRefs = new String[membersToAddList.size() + 1];
                memberRefs[0] = creator.getReference();

                int i = 1;
                for (String iMemberRef : membersToAddList) {
                    memberRefs[i] = iMemberRef;
                    i++;
                }
            }

            // TODO unhandled exceptions
            try {
                GUI.getInstance().getSession()
                        .write(ClientAPI.newRequest().createNewChatReq(chatTitle, chatDec, memberRefs, getNumChats()));
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            oneMoreChat();

            return true;
        } else {

            return false;
        }

    }

    public void refreshState() throws SocketException, IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        write(ClientAPI.newRequest().updateStateReq(getConId(), dtf.format(now)));
    }

    public void editMemberRights(Chat editingChat, List<String> memberRefUpdated) {

    }

    /* RESPONDS */
    /* OVERRIDE */
}
