package CLI;

public enum ConsoleState {

    MAIN,
    SINGLE,
    CHAT_MENU,
    CHAT,
    CHAT_SETTINGS;

    private static ConsoleState instance = MAIN;

    public static ConsoleState getState() {
        return instance;
    }

    public static void changeState(final ConsoleState newState) {
        switch (newState) {
            case MAIN:
                instance = MAIN;
                break;
            case SINGLE:
                instance = SINGLE;
                break;
            case CHAT_MENU:
                instance = CHAT_MENU;
                break;
            case CHAT:
                instance = CHAT;
                break;
            case CHAT_SETTINGS:
                instance = CHAT_SETTINGS;
                break;
            default:
                instance = MAIN;
                break;
        }
    }

}
