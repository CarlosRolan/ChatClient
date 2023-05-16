package controller;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private static final String NO_TEXT = "none";
    private static final String NO_EMISOR = "SERVER";

    private String pAction = null;
    private String pEmisor = NO_EMISOR;
    private String pReceptor = null;
    private String pText = NO_TEXT;
    private ArrayList<String> params;

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
    public String getParameter(int index) {
        return params.get(index);
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
    public Message(String action, String emisor, String receptor, String text, String ...parameters) {
        pAction = action;
        pEmisor = emisor;
        pReceptor = receptor;
        pText = text;
        for (int i = 0; i < parameters.length; i++) {
            params.add(parameters[i]);
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
