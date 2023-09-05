package GUI;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.chat.Chat;
import com.comunication.ApiCodes;
import com.comunication.MSG;
import com.comunication.PKG;
import com.comunication.handlers.IMSGHandler;
import com.comunication.handlers.IPKGHandler;

import GUI.view.MainMenu;
import api.ClientAPI;
import controller.connection.ClientConnection;
import utils.GlobalMethods;

public class AppState extends Thread implements ApiCodes {

    private static AppState instance = null;

    /* STATIC */
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }

    /* PROPERTIES */
    private volatile List<String> pOnlineUsers = new ArrayList<>();
    private IUpdate iUpdate;
    public final ClientConnection pClientCon;
    private final TimerTask tTask = new TimerTask() {

        @Override
        public void run() {
            try {
                pClientCon.write(
                        ClientAPI.newRequest().updateState(pClientCon.getConId(), GlobalMethods.getCurrentTime()));
            } catch (SocketException e) {
                System.err.println("SOCKET EXCEPTION ON WRITING UPDDATE_REQ");
            } catch (IOException e) {
                System.err.println("IO EXCEPTION ON WRITING UPDDATE_REQ");
            }
        }

    };

    synchronized public List<String> getOnlineUsersList() {
        return pOnlineUsers;
    }

    public ClientConnection getClientConnection() {
        return pClientCon;
    }

    public void setOnUpdate(IUpdate iUpdate2) {
        iUpdate = iUpdate2;
    }

    /* CONSTRUCTOR */
    private AppState() {
        setTheme();
        pClientCon = new ClientConnection(askForUserName(), IMSG_HANDLER, IPCKG_HANDLER);
        new Timer().scheduleAtFixedRate(tTask, 1000, 5000);
    }

    /* IMPLEMENTATIONS */
    // MSG
    private final IMSGHandler IMSG_HANDLER = new IMSGHandler() {

        @Override
        public void handleRequest(MSG request) {
            switch (request.getAction()) {

                case REQ_INIT_CHAT:
                    Chat chatInstance = Chat.instanceChat(request);
                    pClientCon.addChatToLocal(chatInstance);
                    iUpdate.onNewChat(chatInstance);
                    break;

                default:
                    System.out.println(WARN_UNHANDLED_MSG_REQUEST);
                    break;
            }

        }

        @Override
        public void handleMessage(MSG message) {

            switch (message.getAction()) {
                case MSG_TO_SINGLE:
                    SwingUtils.executeOnSwingThread(() -> iUpdate.onMessageReceived(message.getEmisor()));
                    break;
                default:
                    System.out.println(WARN_UNHANDLED_MSG_MESSAGE);
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
                    System.out.println(WARN_UNHANDLED_MSG_ERROR);
                    break;
            }
        }
    };

    // PACKAGE
    private final IPKGHandler IPCKG_HANDLER = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
            switch (mixed.getPKGName()) {

                default:
                    System.out.println(WARN_UNHANDLED_PKG_MIXED);
                    break;

            }
        }

        @Override
        public void handleCollection(PKG collection) {

            switch (collection.getPKGName()) {

                case COLLECTION_UPDATE:
                    pOnlineUsers.clear();
                    for (MSG iter : collection.getMessagesList()) {
                        if (iter.getAction().equals(REQ_INIT_CHAT)) {
                            Chat updatedChat = Chat.instanceChat(iter);
                            pClientCon.setChat(updatedChat);
                        }
                        if (iter.getAction().equals(REQ_INIT_CON)) {
                            pOnlineUsers.add(iter.getEmisor() + "_" + iter.getReceptor() + "_" + iter.getBody());
                        }
                    }
                    SwingUtils.executeOnSwingThread(() -> iUpdate.onUpdate());

                    break;

                default:
                    System.out.println(WARN_UNHANDLED_PKG_COLLECTION);
                    break;

            }

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

    public interface IUpdate {
        void onUpdate();

        void onNewChat(Chat newChat);

        void onMessageReceived(String emisorId);
    }

}