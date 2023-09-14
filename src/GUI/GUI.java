package GUI;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.api.Codes;
import com.chat.Chat;
import com.chat.ChatBuilder;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;
import com.data.MSG;
import com.data.PKG;

import GUI.view.MainMenu;
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

    /* PROPERTIES */
    /* FINAL */
    private final TimerTask tTask = new TimerTask() {

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

    public final ClientConnection pClientCon;

    private List<String> pConRefList = new ArrayList<>();
    private List<String> pChatRefList = new ArrayList<>();
    private IGUIListener iUpdate;

    /* GETTERs */
    public List<String> getChatRefList() {
        return pChatRefList;
    }

    public List<String> getConRefList() {
        return pConRefList;
    }

    /* SETTERs */
    public void addChatRef(String chatRef) {
        pChatRefList.add(chatRef);
    }

    public void addConRef(String conRef) {
        int i = pConRefList.indexOf(conRef);

        try {
            pConRefList.set(i, conRef);
        } catch (IndexOutOfBoundsException e) {
            pConRefList.add(conRef);
        }

    }

    public void setOnUpdate(IGUIListener iUpdate2) {
        iUpdate = iUpdate2;
    }

    /* PRIVATE METHODs */
    private boolean isNewChatReference(String chatReference) {
        for (String iter : pChatRefList) {
            if (iter.equals(chatReference)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNewConReference(String conRef) {
        for (String iter : pConRefList) {
            if (iter.startsWith(conRef)) {
                return false;
            }
        }
        return true;
    }

    /* CONSTRUCTOR */
    private GUI() {
        setTheme();
        pClientCon = new ClientConnection(askForUserName(), IMSG_HANDLER, IPCKG_HANDLER);
        new Timer().scheduleAtFixedRate(tTask, 1000, UPDATE_TIME);
        FileManager.initInstance(pClientCon);
    }

    /* IMPLEMENTATIONS */
    // MSG
    private final IMSGHandler IMSG_HANDLER = new IMSGHandler() {

        @Override
        public void handleRequest(MSG request) {
            switch (request.getAction()) {

                case REQ_INIT_CHAT:
                    Chat chatInstance = ChatBuilder.newChat(request);

                    if (isNewChatReference(chatInstance.getReference())) {
                        addChatRef(chatInstance.getReference());
                        SwingUtils.executeOnSwingThread(() -> iUpdate.onNewChat(chatInstance));
                    } else {
                        // TODO UNREACHEABLE CODE CHAT IS NEVER GONNA BE THE SAME
                        JOptionPane.showMessageDialog(null, "CHAT ALREADY CREATED");
                    }
                    break;

                default:
                    System.out.println(WARN_UNREGISTERED_MSG_REQUEST_ACTION);
                    break;
            }

        }

        @Override
        public void handleMessage(MSG message) {

            switch (message.getAction()) {
                case MSG_TO_SINGLE:
                    SwingUtils.executeOnSwingThread(() -> iUpdate.onMessageReceived(message));
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'unHandledMSG'");
        }
    };

    // PACKAGE
    private final IPKGHandler IPCKG_HANDLER = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
            switch (mixed.getPKGName()) {

                default:
                    System.out.println(WARN_UNREGISTERED_PKG_MIXED_ACTION);
                    break;

            }
        }

        @Override
        public void handleCollection(PKG collection) {

            switch (collection.getPKGName()) {

                case COLLECTION_UPDATE:
                    for (MSG iter : collection.getMessagesList()) {
                        if (iter.getAction().equals(REQ_INIT_CHAT)) {
                            Chat c = ChatBuilder.newChat(iter);
                            if (isNewChatReference(c.getReference())) {
                                addChatRef(c.getReference());
                            }
                        }
                        if (iter.getAction().equals(REQ_INIT_CON)) {
                            String conRef = iter.getEmisor() + "_" + iter.getReceptor();
                            // TODO WHAT TODO WITH TIME
                            String dateTime = "_" + iter.getBody();
                            if (isNewConReference(conRef)) {
                                addConRef(conRef);
                            }
                        }
                    }

                    SwingUtils.executeOnSwingThread(() -> iUpdate.onUpdate());
                    break;

                default:
                    System.out.println(WARN_UNREGISTERED_PKG_COLLECTION_ACTION);
                    break;

            }

        }

        @Override
        public void unHandledPKG(PKG arg0) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'unHandledPKG'");
        }

    };

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
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public String askForUserName() {
        return JOptionPane.showInputDialog(null, "Your USER name");
    }

    @Override
    public void run() {
        while (true) {
            try {
                pClientCon.listen();
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("PROBLEM LISTENING SERVER");
                break;
            }
        }
        System.err.println("LISTEN THREAD STOPPED");
    }

    public interface IGUIListener {
        void onUpdate();

        void onNewChat(Chat chat);

        void onMessageReceived(MSG msg);
    }

}