import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.ClientEnviroment;
import controller.log.HistoryUser;

public class Connection extends Thread implements ClientEnviroment {

    private Client client;

    public Connection() {
        try {
            client = new Client(new Socket(HOSTNAME, PORT), null, 0);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(validate()) {
            HistoryUser.getInstance().logIn();
        } else {
            HistoryUser.getInstance().log("COULD NOT CONNECT WITH THE SERVER");
        }
    }

    private boolean validate() {
        client.sendPresentation();

        Request validation;

        do {

            validation = (Request) client.read();

        } while (validation == null);

        if (validation.getAction().equals(RequestCodes.PRESENT)) {
            return true;
        }
        return false;

    }

    private void handleResponse(Object response) {
        if (response instanceof Message) {
            handleMessage((Message) response);
        } else if (response instanceof Request) {
            handleRequest((Request) response);
        }
    }

    private void handleRequest(Request req) {

    }

    private void handleMessage(Message msg) {

    }

    @Override
    public void run() {
        while (true) {
            handleResponse(client.read());
        }
    }

}
