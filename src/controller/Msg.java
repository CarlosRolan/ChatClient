package controller;

import java.io.Serializable;

public class Msg implements Serializable {
    private static final long serialVersionUID = 123456789L;

    public enum MsgType {
        REQUEST,
        MESSAGE,
        ERROR;
    }

    public final MsgType PACKAGE_TYPE;
    private String action;
    private String[] headers = new String[2];
    private String[] parameters = new String[3];
    private String body;

    // Getters
    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @return the headers
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * @return the EMISOR which is always the 0 position at the array
     */
    public String getEmisor() {
        return headers[0];
    }

    /**
     * @return the RECEPTOR which is always the 1 position at the array
     */
    public String getReceptor() {
        return headers[1];
    }

    /**
     * @return the parameters
     */
    public String[] getParameters() {
        return parameters;
    }

    public String getParameter(int pos) {
        return parameters[pos];
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    public String getText() {
        return getBody();
    }

    // Setters
    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    /**
     * @param headers the EMISOR at the 0 position
     */
    public void setEmisor(String emisor) {
        headers[0] = emisor;
    }

    /**
     * @param headers the EMISOR at the 0 position
     */
    public void setReceptor(String receptor) {
        headers[1] = receptor;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @param pos       the position where whe setting the param
     * @param parameter the parameter to set
     */
    public void setParameter(int pos, String parameter) {
        this.parameters[pos] = parameter;

    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Msg(MsgType TYPE) {
        PACKAGE_TYPE = TYPE;
    }

}
