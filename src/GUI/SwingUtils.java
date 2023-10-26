package GUI;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SwingUtils {

    public static void executeOnSwingThread(Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean confrimDialog(JComponent parent, String message) {
        int respond = JOptionPane.showConfirmDialog(parent, "CONFIRM", message, JOptionPane.YES_NO_OPTION);

        switch (respond) {
            case JOptionPane.YES_OPTION:
                return true;
            default:
                return false;
        }
    }
}
