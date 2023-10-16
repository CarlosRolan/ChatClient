package api;

import com.api.Codes;
import com.data.MSG;

public class ClientAPI implements Codes {

    // TODO AVOID TO USE CHATS AND CONNECTIONS AND USE ONLY REFS

    private MSG msgOut = null;

    public static ClientAPI newRequest() {
        return new ClientAPI();
    }

    private ClientAPI() {

    }

    /**
     * 
     * @param emisorId
     * @return
     */
    public MSG showAllOnlineReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CON);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    /**
     * 
     * @param requesterId
     * @param candidateId
     * @param requesterNick
     * @return
     */
    public MSG askForSingleReq(String requesterId, String candidateId, String requesterNick) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SINGLE);
        msgOut.setEmisor(requesterId);
        msgOut.setReceptor(candidateId);
        msgOut.setBody(requesterNick);

        return msgOut;
    }

    /**
     * 
     * @param candidateRespond
     * @param requesterId
     * @param candidateId
     * @param candidateNick
     * @return
     */
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

    /**
     * 
     * @param emisorId
     * @param emisorNick
     * @param receptorId
     * @param receptorNick
     * @param msgTxt
     * @return
     */
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

    /**
     * 
     * @param emisorId
     * @param receptorId
     * @return
     */
    public MSG exitSingleReq(String emisorId, String receptorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_EXIT_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(receptorId);

        return msgOut;

    }

    /**
     * 
     * @param emisorId
     * @return
     */
    public MSG showAllYourChatsReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_SHOW_ALL_CHAT);
        msgOut.setEmisor(emisorId);

        if ("".equals(msgOut)) {
            return null;
        }

        return msgOut;
    }

    /**
     * 
     * @param ChatId
     * @param emisorId
     * @return
     */
    public MSG selectChatReq(String ChatId, String emisorId, String numOfChats) {
        msgOut = new MSG(MSG.Type.REQUEST);
        msgOut.setAction(REQ_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(ChatId);
        msgOut.setBody(numOfChats);
        return msgOut;
    }

    /**
     * 
     * @param chatId        creatorId + numOfChats of the crator
     * @param creatorNick
     * @param chatTitle
     * @param chatDesc
     * @param memberRefList
     * @return
     *         MSG[REQUEST]
     *         action = REQ_CREATE_CHAT
     *         emisor = chatTitle
     *         receptor = chatDesc
     *         parameters = each parameter is a member ref
     */
    public MSG createNewChatReq(String chatTitle, String chatDesc, String[] memberStringRef, String numChats) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_CREATE_CHAT);
        msgOut.setEmisor(chatTitle);
        msgOut.setReceptor(chatDesc);
        msgOut.setParameters(memberStringRef);
        msgOut.setBody(numChats);

        return msgOut;

    }

    /**
     * 
     * @param chatId
     * @param emisorId
     * @param emisorNick
     * @param text
     * @return
     */
    public MSG sendMsgToChatReq(String chatId, String emisorId, String emisorNick, String text) {

        msgOut = new MSG(MSG.Type.MESSAGE);

        msgOut.setAction(MSG_TO_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);
        msgOut.setParameter(0, emisorNick);
        msgOut.setBody(text);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @return
     */
    public MSG requestUserInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SELECT_USER);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    /**
     * 
     * @param chatId
     * @param memberId
     * @return
     */
    public MSG addMemberToChatReq(String chatId, String memberId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_ADD_MEMBER);
        msgOut.setEmisor(chatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    /**
     * 
     * @param chatId
     * @param memberId
     * @return
     */
    public MSG deleteMemberinChatReq(String chatId, String memberId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_DELETE_MEMBER);
        msgOut.setEmisor(chatId);
        msgOut.setReceptor(memberId);
        msgOut.setParameter(0, "REG");

        return msgOut;
    }

    /**
     * 
     * @param ChatId
     * @return
     */
    public MSG showAllMembersReq(String ChatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_SHOW_ALL_MEMBERS_OF_CHAT);
        msgOut.setEmisor(ChatId);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @param chatId
     * @return
     */
    public MSG exitChatReq(String emisorId, String chatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_EXIT_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @param chatId
     * @return
     */
    public MSG deleteChatReq(String emisorId, String chatId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_DEL_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @return
     */
    public MSG getChatsInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_GET_CHATS_INFO);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @return
     */
    public MSG getUsersInfoReq(String emisorId) {
        msgOut = new MSG(MSG.Type.REQUEST);

        msgOut.setAction(REQ_GET_CHATS_INFO);
        msgOut.setEmisor(emisorId);

        return msgOut;
    }

    /* GUI */
    /**
     * 
     * @param emisorId
     * @param curretnTime
     * @return
     */
    public MSG updateStateReq(String emisorId, String curretnTime) {
        MSG updateState = new MSG(MSG.Type.REQUEST);

        updateState.setAction(REQ_UPDATE_STATE);
        updateState.setEmisor(emisorId);
        updateState.setBody(curretnTime);

        return updateState;
    }

}
