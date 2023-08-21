package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.sql.rowset.spi.XmlReader;

import com.chat.Chat;

public class SaveData {

    /* SINGELTON */
    private static SaveData instance;

    public static SaveData getInstance() {
        if (instance == null) {
            instance = new SaveData();
        }

        return instance;
    }

    private final static String FILE_DIR = "../../store/";
    private final static String FILE_NAME = "ClientData.txt";

    private XmlReader xmlReader;
    private File file;

    private SaveData() {
        file = new File(FILE_DIR + FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String loadChatsRaw() {
        String rawData = "";
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            rawData += line;
        }

        myReader.close();

        return rawData;
    }

    public void saveChatXML(Chat chat) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        writer.println(chat.toXML());

        writer.close();

    }
}
