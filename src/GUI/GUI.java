package GUI;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.api.Codes;
import com.chat.Chat;
import com.chat.Member;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;
import com.data.MSG;
import com.data.PKG;

import GUI.view.MainView;
import GUI.view.components.panels.PConv;
import controller.connection.ClientConnection;
import controller.manager.FileManager;

public class GUI extends Thread implements Codes {

    private static volatile GUI instance = null;
    private static final long UPDATE_TIME = 5000;

    /* STATIC */
    synchronized public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }

        return instance;
    }

    private final ClientConnection pClientCon;
    /* PROPERTIES */

    private List<String> mUserRefList = new ArrayList<>();
    private List<String> mChatRefList = new ArrayList<>();
    private IGUIListener iUpdate;
    public volatile List<PConv> convListRef = new ArrayList<>();
    private final TimerTask tupdateTask = new TimerTask() {

        @Override
        public void run() {
            try {
                pClientCon.refreshState();
            } catch (final SocketException e) {
                System.err.println("SOCKET EXCEPTION ON WRITING UPDDATE_REQ");
            } catch (final IOException e) {
                System.err.println("IO EXCEPTION ON WRITING UPDDATE_REQ");
            }
        }

    };

    /* GETTERs */

    public ClientConnection getSession() {
        return pClientCon;
    }

    public List<String> getChatRefList() {
        return mChatRefList;
    }

    public List<String> getUserRefList() {
        return mUserRefList;
    }

    /* SETTERs */
    public void addChatRef(String chatRef) {
        mChatRefList.add(chatRef);
    }

    public void addConRef(String conRef) {
        int i = mUserRefList.indexOf(conRef);

        try {
            mUserRefList.set(i, conRef);
        } catch (IndexOutOfBoundsException e) {
            mUserRefList.add(conRef);
        }

    }

    public void addPanelInstance(PConv convPanel) {
        convListRef.add(convPanel);
    }

    public void removePanelInstance(PConv convPanel) {
        convListRef.remove(convPanel);
    }

    public PConv getPanelInstance(String convPanelId) {
        for (PConv iConv : convListRef) {
            if (convPanelId.equals(iConv.getConvId())) {
                return iConv;
            }
        }
        System.out.println("INSTANCE NOT FOUND");
        return null;
    }

    public Chat getChatFromId(String chatId) {
        for (String iRef : mChatRefList) {
            Chat iter = Chat.initChat(iRef);
            if (iter.getChatId().equals(chatId)) {
                return iter;
            }
        }

        return null;
    }

    public boolean usersHasUpdate(List<String> updatedUserRefList) {
        if (updatedUserRefList.size() != mUserRefList.size())
            return true;

        for (String iUpdatedUserRef : updatedUserRefList) {
            if (!mUserRefList.contains(iUpdatedUserRef))
                return true;
        }

        return false;
    }

    public boolean chatsHasUpdate(List<String> updatedChatRefList) {

        if (updatedChatRefList.size() != mChatRefList.size())
            return true;

        for (String iUpdatedRef : updatedChatRefList) {
            if (!mChatRefList.contains(iUpdatedRef))
                return true;
        }

        return false;
    }

    public void setUpdateListener(IGUIListener listener) {
        iUpdate = listener;
    }

    /* CONSTRUCTOR */
    private GUI() {
        setTheme();
        pClientCon = new ClientConnection(askForUserName(), IMSG_HANDLER, IPCKG_HANDLER);
        new Timer().scheduleAtFixedRate(tupdateTask, 1000, UPDATE_TIME);
        FileManager.initInstance(pClientCon);
    }

    private void setTheme() {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private String askForUserName() {
        return JOptionPane.showInputDialog(null, "Your USER name");
    }

    public void launchApp() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }

    @Override
    public void run() {

        try {
            while (true)
                pClientCon.listen();
        } catch (ClassNotFoundException e) {
            System.err.println("CLSS NOT FOUND");
        } catch (IOException e) {
            System.err.println("IOEXCEPTION");
        }
        System.err.println("LISTEN THREAD STOPPED");

    }

    /* IMPLEMENTATIONS */
    // MSG
    private final IMSGHandler IMSG_HANDLER = new IMSGHandler() {

        @Override
        public void handleRequest(MSG request) {
            switch (request.getAction()) {

                case REQ_INIT_CHAT:
                    SwingUtils.executeOnSwingThread(() -> iUpdate.addedToChat(request));
                    break;

                default:
                    System.out.println(WARN_UNREGISTERED_MSG_REQUEST_ACTION);
                    break;
            }

        }

        @Override
        public void handleMessage(MSG message) {

            switch (message.getAction()) {
                case MSG_FROM_SINGLE:
                    SwingUtils.executeOnSwingThread(() -> iUpdate.onMessageReceived(message, false));
                    break;
                case MSG_FROM_CHAT:
                    /*
                     * toChat.setAction(MSG_FROM_CHAT);
                     * toChat.setEmisor(emisorId);
                     * toChat.setReceptor(currentChat.getChatId());
                     * toChat.setParameter(0, emisorNick);
                     * toChat.setBody(text);
                     */
                    SwingUtils.executeOnSwingThread(() -> iUpdate.onMessageReceived(message, true));
                    break;
                default:
                    System.out.println(WARN_UNREGISTERED_MSG_MESSAGE_ACTION);
                    break;
            }
        }

        @Override
        public void handleError(MSG error) {
            switch (error.getAction()) {

                case ERROR_CHAT_NOT_FOUND:
                    JOptionPane.showMessageDialog(null, "CHAT NOT FOUND");
                    break;
                case ERROR_SELF_REFERENCE:
                    JOptionPane.showMessageDialog(null, "SELF REFERENCE");
                    break;
                case ERROR_CLIENT_NOT_FOUND:
                    JOptionPane.showMessageDialog(null, "CLIENT NOT FOUND");
                    break;

                default:
                    System.out.println(WARN_UREGISTERED_MSG_ERROR_ACTION);
                    break;
            }
        }

        @Override
        public void unHandledMSG(MSG arg0) {
            throw new UnsupportedOperationException("UNHANDLED MSG TYPE!!!");
        }
    };

    // PACKAGE
    private final IPKGHandler IPCKG_HANDLER = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
            switch (mixed.getPKGName()) {

                default:
                    System.err.println(WARN_UNREGISTERED_PKG_MIXED_ACTION);
                    break;
            }
        }

        @Override
        public void handleCollection(PKG collection) {

            switch (collection.getPKGName()) {

                case COLLECTION_UPDATE:

                    List<String> updatedUserRefList = new ArrayList<>();
                    List<String> updatedChatRefList = new ArrayList<>();

                    for (MSG iMsg : collection.getMessagesList()) {

                        if (iMsg.getAction().equals(REQ_INIT_CON)) {
                            String conId = iMsg.getEmisor();

                            if (!conId.equals(pClientCon.getConId())) {
                                String conNick = iMsg.getReceptor();

                                String iConRef = conId + Member.SEPARATOR + conNick;
                                updatedUserRefList.add(iConRef);
                            }
                        }

                        if (iMsg.getAction().equals(REQ_INIT_CHAT)) {
                            Chat iChat = Chat.instanceChat(iMsg);
                            updatedChatRefList.add(iChat.getReference());
                        }
                    }

                    System.out.println("----UPDATE PKG----");
                    for (String string : updatedUserRefList) {
                        System.out.println("USER " + string);
                    }

                    for (String string : updatedChatRefList) {
                        System.out.println("CHAT " + string);
                    }
                    System.out.println("------------------");

                    if (usersHasUpdate(updatedUserRefList)) {
                        mUserRefList = null;
                        mUserRefList = updatedUserRefList;
                        SwingUtils.executeOnSwingThread(() -> iUpdate.updateUsers());
                    }

                    if (chatsHasUpdate(updatedChatRefList)) {
                        mChatRefList = null;
                        mChatRefList = updatedChatRefList;
                        SwingUtils.executeOnSwingThread(() -> iUpdate.updateChats());

                    }

                    break;

                default:
                    System.err.println(WARN_UNREGISTERED_PKG_COLLECTION_ACTION);
                    break;
            }

        }

        @Override
        public void unHandledPKG(PKG arg0) {
            throw new UnsupportedOperationException("UNHANDLED PKG TYPE!!!!");
        }

    };

    public interface IGUIListener {

        void addedToChat(MSG chatInfo);

        void updateUsers();

        void updateChats();

        void onMessageReceived(MSG msgReceived, boolean isChat);
    }

}