package api;

import com.chat.Chat;
import com.comunication.ApiCodes;
import com.comunication.Msg;
import com.comunication.Msg.MsgType;

public class ClientAPI implements ApiCodes {

    private Msg msgOut = null;

    public static ClientAPI newRequest() {
        return new ClientAPI();
    }

    private ClientAPI() {

    }

    public Msg showAllOnline(String emisorId) {
        msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CON);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public Msg askForSingle(String requesterId, String candidateId, String requesterNick) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_SINGLE);
        msgOut.setEmisor(requesterId);
        msgOut.setReceptor(candidateId);
        msgOut.setBody(requesterNick);

        return msgOut;
    }

    public Msg permissionRespond(boolean candidateRespond, String requesterId, String candidateId,
            String candidateNick) {
        msgOut = new Msg(MsgType.REQUEST);

        if (candidateRespond) {
            msgOut.setAction(REQ_ALLOW);
        } else {
            msgOut.setAction(REQ_DENY);
        }
        msgOut.setEmisor(candidateId);
        msgOut.setReceptor(requesterId);
        msgOut.setBody(candidateNick);

        return msgOut;
    }

    public Msg sendSingleMsg(String emisorId, String receptorId, String msgTxt) {
        msgOut = new Msg(MsgType.MESSAGE);

        msgOut.setAction(MSG_TO_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);
        msgOut.setBody(msgTxt);

        return msgOut;
    }

    public Msg exitSingle(String emisorId, String receptorId) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_EXIT_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);

        return msgOut;

    }

    public Msg showAllYourChats(String emisorId) {
        msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CHAT);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public Msg selectChat(String ChatId, String emisorId) {
        msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(ChatId);
        return msgOut;
    }

    public Msg requestNewChat(String creatorId, String creatorNick, String ChatTitle, String ChatDesc) {
        msgOut = new Msg(MsgType.REQUEST);

        String[] parameters = { ChatTitle, ChatDesc };

        msgOut.setAction(REQ_CREATE_CHAT);
        msgOut.setEmisor(creatorId);
        msgOut.setParameters(parameters);
        msgOut.setBody(creatorNick);

        return msgOut;

    }

    public Msg sendMsgToChat(Chat currentChat, String emisorId, String emisorNick, String text) {

        msgOut = new Msg(MsgType.MESSAGE);

        msgOut.setAction(MSG_TO_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(currentChat.getChatId());
        msgOut.setParameter(0, emisorNick);
        msgOut.setBody(text);

        return msgOut;
    }

    public Msg requestUserInfo(String emisorId) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_SELECT_USER);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    public Msg addMemberToChat(String ChatId, String memberId) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_ADD_MEMBER);
        msgOut.setEmisor(ChatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    public Msg deleteMemberinChat(String ChatId, String memberId) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_DELETE_MEMBER);
        msgOut.setEmisor(ChatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    public Msg showAllMembers(String ChatId) {
        msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_SHOW_ALL_MEMBERS_OF_CHAT);
        msgOut.setEmisor(ChatId);

        return msgOut;
    }

}
