package ort.techtown.share_calendar.Data;

import java.time.LocalDate;

public class CalendarUtil {

    public static LocalDate selectedDate;
    public static LocalDate today;
    public static String UID;
    private static String Name;

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }
}
