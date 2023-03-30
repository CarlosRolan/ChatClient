package controller;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 123456789L;

    public final static String NO_CLIENT = "SERVER";

    private String pAction = null;
    private String pEmisor = null;
    private String pReceptor = NO_CLIENT;
    private String pText = null;


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

    public Message(String action, String emisor, String receptor, String text) {
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
        pText = text;
    }

    public Message(String action, String emisor) {
        pAction = action;
        pEmisor = emisor;
    }

    public Message(String action, String emisor, String receptor) {
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
    }

    public Message(String action) {
        pAction = action;
    }

    @Override
    public String toString() {
        return "MSG[-\n\tAction{"+pAction+"}\n\t"+"Emisor{"+pEmisor+"}\n\t"+"Receptor{"+pReceptor+"}\n\t"+"Text{"+pText+"}\n]";
    }
    
    
}
