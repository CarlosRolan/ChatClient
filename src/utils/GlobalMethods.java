package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import CLI.CLIActions;

public class GlobalMethods {

    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CLIActions.DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
