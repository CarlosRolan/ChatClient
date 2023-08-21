package GUI;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.comunication.ApiCodes;
import com.comunication.MSG;
import com.comunication.PKG;
import com.comunication.handlers.IMSGHandler;
import com.comunication.handlers.IPKGHandler;

import GUI.view.MainMenu;
import controller.ClientConnection;

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
    public IUpdateUI iUIListener;
    public List<String> connectedUsers = new ArrayList<>();
    public final ClientConnection pClientCon;

    /* GETTERS */
    public ClientConnection getClientConnection() {
        return pClientCon;
    }

    /* CONSTRUCTOR */
    private AppState() {
        setTheme();
        String nick = askForUserName();
        pClientCon = new ClientConnection(nick, IMSG_HANDLER, IPCKG_HANDLER);
    }

    /* IMPLEMENTATIONS */
    // MSG
    private final IMSGHandler IMSG_HANDLER = new IMSGHandler() {

        @Override
        public void handleRequest(MSG request) {
            switch (request.getAction()) {
                case REQ_INIT_CHAT:
            }

        }

        @Override
        public void handleMessage(MSG message) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleMessage'");
        }

        @Override
        public void handleError(MSG error) {
            switch (error.getAction()) {
                case ApiCodes.ERROR_CHAT_NOT_FOUND:
                    System.out.println(error.toString());
                    break;
            }
        }

    };

    // PACKAGE
    private final IPKGHandler IPCKG_HANDLER = new IPKGHandler() {

        @Override
        public void handleMixed(PKG mixed) {
        }

        @Override
        public void handleCollection(PKG collection) {
            for (MSG iter : collection.getMessagesList()) {
                connectedUsers.add(iter.getEmisor());
            }
            iUIListener.update();
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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            pClientCon.refreshStateReq();
        }
    }

    public void setOnUpdate(IUpdateUI onUpdate) {
        iUIListener = onUpdate;
    }

    public interface IUpdateUI {
        void update();
    }
}