package GUI.view.components.panels;

import controller.manager.FileManager;

public class PSingle extends PConv {
 

    protected PSingle(String convId, String convTitle, String convSubTitle, IConvListener listener) {
        iConvListener = listener;
        mIsChat = false;
        mAdmin = false;
        mId = convId;
        mTitle = convTitle;
        mSubTitle = convSubTitle;
        mOpen = false;
        if (!FileManager.getInstance().initConvHistory(convTitle, false)) {
            history = FileManager.getInstance().loadConvHistory(convTitle, false);
            loadHistory();
        }
        initComponents();
    }
}
