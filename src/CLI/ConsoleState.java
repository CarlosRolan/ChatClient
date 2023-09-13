package CLI;

public enum ConsoleState implements CLIActions {

    MAIN,
    SINGLE,
    CHAT_MENU,
    CHAT,
    CHAT_SETTINGS;

    private static ConsoleState instance = MAIN;

    public static ConsoleState get() {
        return instance;
    }

    public void change(final ConsoleState newState) {
        System.out.println(CLI_CLEAR_CONSOLE);
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
            default:
                instance = MAIN;
                break;
        }
    }
}
