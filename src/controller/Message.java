package controller;

import java.io.Serializable;

public class Message implements Serializable {
    public enum MsgType {
        REQUEST,
        MESSAGE,
        ERROR;
    }

    private static final long serialVersionUID = 123456789L;

    private final MsgType PACKAGE_TYPE;
    private String pAction = null;
    private String pEmisor;
    private String pReceptor = null;
    private String pText;
    private String[] params;

    public MsgType typeOf() {
        return PACKAGE_TYPE;
    }

    public String getAction() {
        return pAction;
    }

    public String getReceptor() {
        return pReceptor;
    }

    public String getEmisor() {
        return pEmisor;
    }

    public String getText() {
        return pText;
    }

    public String getParameter(int index) {
        return params[index];
    }

    public Object[] getParameters() {
        return params;
    }

    public Message(final MsgType type, String action, String emisor, String receptor, String text) {
        PACKAGE_TYPE = type;
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
        pText = text;
    }

    public Message(final MsgType type, String action, String emisor, String receptor) {
        PACKAGE_TYPE = type;
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
    }

    public Message(final MsgType type, String action, String emisor) {
        PACKAGE_TYPE = type;
        pAction = action;
        pEmisor = emisor;
    }

    public Message(final MsgType type, String action) {
        PACKAGE_TYPE = type;
        pAction = action;
    }

    public Message(final MsgType type, String action, String emisor, String receptor, String text,
            String... parameters) {
        PACKAGE_TYPE = type;
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
        pText = text;
        params = parameters;
    }

    @Override
    public String toString() {
        return "MSG[\n\tAction{" + pAction + "}\n\t" + "Emisor{" + pEmisor + "}\n\t" + "Receptor{" + pReceptor + "}\n\t"
                + "Text{" + pText + "}\n]";
    }

}
