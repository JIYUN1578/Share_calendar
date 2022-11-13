package ort.techtown.share_calendar.Data;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarColor {
    boolean isColor;
    ArrayList<String> colors;

    public CalendarColor(ArrayList<String> colors, boolean isColor) {
        this.colors = colors;
        this.isColor = isColor;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
}
