package api;

import com.Msg;
import com.Msg.MsgType;
import com.RequestCodes;
public class ClientAPI implements RequestCodes {

    public static ClientAPI newRequest() {
        return new ClientAPI();
    }

    private ClientAPI() {

    }

    public Msg showAllOnline() {
        Msg msgOut = new Msg(MsgType.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_ONLINE);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public Msg askForSingle(String requesterId, String candidateId, String requesterNick) {
        Msg msgOut = new Msg(MsgType.REQUEST);

        msgOut.setAction(SINGLE_REQUESTED);
        msgOut.setEmisor(requesterId);
        msgOut.setReceptor(candidateId);
        msgOut.setBody(requesterNick);

        return msgOut;
    }

    public Msg permissionRespond(boolean candidateRespond, String requesterId, String candidateId, String candidateNick) {
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

    public Msg sendDirectMsg(String emisorId, String receptorId, String msgTxt) {
        Msg msgOut = new Msg(MsgType.MESSAGE);

        msgOut.setAction(DIRECT_MSG);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);
        msgOut.setBody(msgTxt);

        return msgOut;
    }

}
