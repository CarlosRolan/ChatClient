package controller.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import com.controller.Connection;

import GUI.GUI;

public class FileManager {

    /* SINGELTON */
    private static FileManager instance;

    public static void initInstance(Connection userCon) {
        instance = new FileManager(userCon);
    }

    public static FileManager getInstance() {
        return instance;
    }

    private final String DATA_DIR = "./data";
    private final String CHATS_DIR = DATA_DIR + "/chats/";
    private final String USERS_DIR = DATA_DIR + "/users/";
    private final String PROFILE_DIR = DATA_DIR + "/profile/";

    private final String TXT_EXT = ".txt";

    private final String CONFIG_FILE = "_config" + TXT_EXT;
    private final String HISTORY_FILE = "_history" + TXT_EXT;

    private File mChatsDir;
    private File mUsersDir;
    private File mProfileFile;

    private FileManager(Connection userCon) {
        initPartenDirs(userCon.getConId(), userCon.getNick());
    }

    private void initPartenDirs(String userId, String userNick) {
        mChatsDir = new File(CHATS_DIR);
        mUsersDir = new File(USERS_DIR);
        mProfileFile = new File(PROFILE_DIR);

        mChatsDir.mkdirs();
        mUsersDir.mkdirs();
        mProfileFile.mkdirs();

    }

    public void initSession(String conId, String newNick) {
        File userSessionFile = new File(mProfileFile, conId + "_" + newNick + TXT_EXT);

        if (!userSessionFile.exists()) {
            try {
                userSessionFile.createNewFile();
                try {
                    FileWriter fw = new FileWriter(userSessionFile, Charset.defaultCharset(), true);
                    String nick = GUI.getInstance().getSession().getNick();
                    fw.append(nick + "\n");
                    fw.flush();

                    fw.close();

                } catch (IOException e) {
                    System.out.println("NO SE PUEDE GUARDAR EL ARCHIVO DE HISTORY");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Scanner myReader = null;

            try {
                myReader = new Scanner(userSessionFile);
            } catch (FileNotFoundException e) {
                System.out.println("PROBLEM LOAD CONV HISTORY");
            }

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

            }

            myReader.close();

        }

    }

    public boolean initConvHistory(String convTitle, boolean isChat) {
        String pathName;
        if (isChat) {
            pathName = mChatsDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        } else {
            pathName = mUsersDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        }

        File chatHistory = new File(pathName);

        if (!chatHistory.exists()) {
            try {
                chatHistory.createNewFile();
                return true;
            } catch (IOException e) {
                System.out.println("NO SE PUEDE CREAR EL ARCHIVO DE HISTORY" + convTitle);
                return false;
            }
        }
        return false;
    }

    public void saveConvHistory(String convTitle, String line, boolean isChat) {

        String pathName;
        if (isChat) {
            pathName = mChatsDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        } else {
            pathName = mUsersDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        }
        FileWriter fw = null;
        File chatHistory = new File(pathName);

        try {
            fw = new FileWriter(chatHistory, Charset.defaultCharset(), true);

            fw.append(line + "\n");
            fw.flush();

            fw.close();

        } catch (IOException e) {
            System.out.println("NO SE PUEDE GUARDAR EL ARCHIVO DE HISTORY");
        }

    }

    public ArrayList<String> loadConvHistory(String convTitle, boolean isChat) {

        String pathName;
        if (isChat) {
            pathName = mChatsDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        } else {
            pathName = mUsersDir.getAbsolutePath() + "/" + convTitle + HISTORY_FILE;
        }
        ArrayList<String> history = new ArrayList<>();
        File chatHistory = new File(pathName);
        Scanner myReader = null;

        try {
            myReader = new Scanner(chatHistory);
        } catch (FileNotFoundException e) {
            System.out.println("PROBLEM LOAD CONV HISTORY");
        }

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            history.add(line);
        }

        myReader.close();

        return history;

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
