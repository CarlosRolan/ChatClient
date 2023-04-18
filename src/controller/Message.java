package controller;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private static final String NO_TEXT = "none";
    private static final String NO_EMISOR = "SERVER";

    private String pAction = null;
    private String pEmisor = NO_EMISOR;
    private String pReceptor = null;
    private String pText = NO_TEXT;

    //Getters
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

    //Constructors
    public Message(String action, String emisor, String receptor, String text) {
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
        pText = text;
    }
    public Message(String action, String emisor, String receptor) {
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
    }
    public Message(String action, String emisor) {
        pAction = action;
        pEmisor = emisor;
    }
    public Message(String action) {
        pAction = action;
    }
    public Message(String ...parameters) {
        try {
            pAction = parameters[0];
            pAction = parameters[1];
            pAction = parameters[2];
            pAction = parameters[3];
        } catch (IndexOutOfBoundsException e) {
        
        }
    }

    @Override
    public String toString() {
        return "MSG[\n\tAction{"+pAction+"}\n\t"+"Emisor{"+pEmisor+"}\n\t"+"Receptor{"+pReceptor+"}\n\t"+"Text{"+pText+"}\n]";
    }
    public String requestInfo() {
        return "___"+pAction+"____\t" + ":" + "[" + pEmisor + "]"+ "=>"+"[" + pReceptor +"]";
    }
    
    
}
