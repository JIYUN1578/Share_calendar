package ort.techtown.share_calendar.Data;

public class Info {
    boolean isDone;
    String start;
    String end;
    String title;
    String info;
    String color;

    public Info(boolean isDone, String start, String end, String title, String info, String color) {
        this.isDone = isDone;
        this.start = start;
        this.end = end;
        this.title = title;
        this.info = info;
        this.color = color;
    }
    public Info() {
        this.isDone = false;
        this.start = "!!";
        this.end = "!!";
        this.title = "!!";
        this.info = "!!";
        this.color = "!!";
    }
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
