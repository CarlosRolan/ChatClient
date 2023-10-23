package CLI;

public enum ConsoleState implements CLIActions {

    MAIN,
    SINGLE,
    CHAT_MENU,
    CHAT,
    CHAT_SETTINGS,
    WAITING;

    private static ConsoleState instance = MAIN;

    public static ConsoleState get() {
        return instance;
    }

    public void change(final ConsoleState newState) {
        clearConsole();
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
            case WAITING:
                instance = WAITING;
                break;
            default:
                instance = MAIN;
                break;
        }
    }
}
