package api;

import com.comunication.ApiCodes;
import com.comunication.Msg;
import com.comunication.Msg.MsgType;

public class ClientAPI implements ApiCodes {

    public static ClientAPI newRequest() {
        return new ClientAPI();
    }

    private ClientAPI() {

    }

    public Msg showAllOnline(String emisorId) {
        Msg msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CON);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public Msg askForSingle(String requesterId, String candidateId, String requesterNick) {
        Msg msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_SINGLE);
        msgOut.setEmisor(requesterId);
        msgOut.setReceptor(candidateId);
        msgOut.setBody(requesterNick);

        return msgOut;
    }

    public Msg permissionRespond(boolean candidateRespond, String requesterId, String candidateId,
            String candidateNick) {
        Msg msgOut = new Msg(MsgType.REQUEST);

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
        Msg msgOut = new Msg(MsgType.MESSAGE);

        msgOut.setAction(MSG_SINGLE_MSG);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);
        msgOut.setBody(msgTxt);

        return msgOut;
    }

    public Msg exitSingle(String emisorId, String receptorId) {
        Msg msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(REQ_EXIT_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);

        return msgOut;

    }

    public Msg showAllChat(String emisorId) {
        Msg msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CHAT);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public Msg selectChat(String chatId) {
        Msg msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_CHAT);
        msgOut.setReceptor(chatId);
        return msgOut;
    }

    public Msg requestNewChat(String creatorId, String creatorNick, String chatTitle, String chatDesc) {
        Msg msgOut = new Msg(MsgType.REQUEST);

        String[] parameters = { chatTitle, chatDesc };

        msgOut.setAction(REQ_CREATE_CHAT);
        msgOut.setEmisor(creatorId);
        msgOut.setParameters(parameters);
        msgOut.setBody(creatorNick);

        return msgOut;

    }

}
