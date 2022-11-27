package ort.techtown.share_calendar.Data;

import java.time.LocalDate;

public class CalendarUtil {

    public static LocalDate selectedDate;
    public static LocalDate today;
    public static String UID;
    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String name) {
        userName = name;
    }
}
