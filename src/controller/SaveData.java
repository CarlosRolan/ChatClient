package controller;

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

    private final String DATA_DIR = "./data";
    private final String CHATS_DIR = DATA_DIR + "/chats/";
    private final String USER_DIR = DATA_DIR + "/user";
    private final String USER_CONFIG = USER_DIR + "/config.txt";
    private final String TXT_EXT = ".txt";

    private XmlReader xmlReader;

    public String loadChatsRaw() {
        String rawData = "";
        Scanner myReader = null;
        try {
            myReader = new Scanner(new File(""));
        } catch (FileNotFoundException e) {
            System.out.println("PROBLEM LOAD CHAT RAW");
        }

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            rawData += line;
        }

        myReader.close();

        return rawData;
    }

    public void saveChatRaw(Chat chat) {
        File chatInfo = initChatDir(chat.getTitle());
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(chatInfo,
                    "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println("PROBLEM SAVING CHAT ON LOCAL");
        }
        writer.println(chat.toXML());

        writer.close();

    }

    private File initChatDir(String chatTitle) {

        File chatDir = new File(CHATS_DIR + chatTitle);

        chatDir.mkdirs();

        File chatInfo = new File(chatDir.getAbsolutePath() + "/info_" + chatTitle + TXT_EXT);

        try {
            chatInfo.createNewFile();
        } catch (IOException e) {
            System.out.println("PROBLEM INIT CHAT DIR");
        }

        return chatInfo;
    }

}
