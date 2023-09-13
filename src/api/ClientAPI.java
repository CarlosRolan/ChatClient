package api;

import com.api.Codes;
import com.chat.Chat;
import com.data.MSG;

public class ClientAPI implements Codes {

    //TODO AVOID TO USE CHATS AND CONNECTIONS AND USE ONLY REFS

    private MSG msgOut = null;

    public static ClientAPI newRequest() {
        return new ClientAPI();
    }

    private ClientAPI() {

    }

    public MSG showAllOnlineReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CON);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public MSG askForSingleReq(String requesterId, String candidateId, String requesterNick) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SINGLE);
        msgOut.setEmisor(requesterId);
        msgOut.setReceptor(candidateId);
        msgOut.setBody(requesterNick);

        return msgOut;
    }

    public MSG permissionRespondReq(boolean candidateRespond, String requesterId, String candidateId,
            String candidateNick) {
        msgOut = new MSG(MSG.Type.REQUEST);

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

    public MSG sendSingleMsgReq(String emisorId, String emisorNick, String receptorId, String receptorNick,
            String msgTxt) {
        msgOut = new MSG(MSG.Type.MESSAGE);

        msgOut.setAction(MSG_TO_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setParameter(0, emisorNick);
        msgOut.setParameter(1, receptorNick);
        msgOut.setReceptor(receptorId);
        msgOut.setBody(msgTxt);

        return msgOut;
    }

    public MSG exitSingleReq(String emisorId, String receptorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_EXIT_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);

        return msgOut;

    }

    public MSG showAllYourChatsReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CHAT);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    public MSG selectChatReq(String ChatId, String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(ChatId);
        return msgOut;
    }

    public MSG requestNewChatReq(int numChats, String creatorId, String creatorNick, String ChatTitle, String ChatDesc) {
        msgOut = new MSG(MSG.Type.REQUEST);

        String[] parameters = { ChatTitle, ChatDesc };

        msgOut.setAction(REQ_CREATE_CHAT);
        msgOut.setEmisor(creatorId);
        msgOut.setReceptor(String.valueOf(numChats));
        msgOut.setParameters(parameters);
        msgOut.setBody(creatorNick);

        return msgOut;

    }

    public MSG sendMsgToChatReq(Chat currentChat, String emisorId, String emisorNick, String text) {

        msgOut = new MSG(MSG.Type.MESSAGE);

        msgOut.setAction(MSG_TO_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(currentChat.getChatId());
        msgOut.setParameter(0, emisorNick);
        msgOut.setBody(text);

        return msgOut;
    }

    public MSG requestUserInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SELECT_USER);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    public MSG addMemberToChatReq(String chatId, String memberId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_ADD_MEMBER);
        msgOut.setEmisor(chatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    public MSG deleteMemberinChatReq(String chatId, String memberId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_DELETE_MEMBER);
        msgOut.setEmisor(chatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    public MSG showAllMembersReq(String ChatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SHOW_ALL_MEMBERS_OF_CHAT);
        msgOut.setEmisor(ChatId);

        return msgOut;
    }

    public MSG exitChatReq(String emisorId, String chatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_EXIT_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);

        return msgOut;
    }

    public MSG deleteChatReq(String emisorId, String chatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_DEL_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);

        return msgOut;
    }

    public MSG getChatsInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_GET_CHATS_INFO);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    public MSG getUsersInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_GET_CHATS_INFO);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    /* GUI */
    public MSG updateStateReq(String emisorId, String curretnTime) {
        MSG updateState = new MSG(MSG.Type.REQUEST);

        updateState.setAction(REQ_UPDATE_STATE);
        updateState.setEmisor(emisorId);
        updateState.setBody(curretnTime);

        return updateState;
    }

}
