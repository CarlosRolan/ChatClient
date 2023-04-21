package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import console.ConsoleConnection;

public final class HistoryUser {
    // Singleton
    private static HistoryUser instance;

    public static HistoryUser getInstance() {
        if (instance == null) {
            instance = new HistoryUser();
        }
        return instance;
    }
    // ---

    private File f;
    private FileWriter fw;
    private LocalDateTime today = LocalDateTime.now();
    private String mUser;
    private boolean isNewFile = true;

    private HistoryUser() {
        mUser = ConsoleConnection.getInstance().client.getNick();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");

        f = new File("./logs/" + dtf.format(today) + ".txt");

        isNewFile = f.exists();

        try {
            f.createNewFile();
            f.setWritable(true);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logIn() {
        try {
            if (isNewFile) {
                fw.write(mUser + " LOGGED IN at : " + today.getHour() + ":" + today.getMinute() + ":"
                        + today.getSecond() + "\n");
            } else {
                fw.append("\n" + mUser + " LOGGED IN at : " + today.getHour() + ":" + today.getMinute() + ":"
                        + today.getSecond() + "\n");
            }
            fw.flush();
        } catch (IOException e) {
            System.out.println("Could not init the user history file");
        }
    }

    public void log(String msg) {
        try {
            fw.append(msg + " [" + today.getHour() + ":" + today.getMinute() + ":" + today.getSecond() + "]\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
