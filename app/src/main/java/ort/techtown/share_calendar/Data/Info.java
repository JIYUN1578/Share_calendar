package ort.techtown.share_calendar.Data;

public class Info {
    boolean isDone;
    boolean isOpen;
    String start;
    String end;
    String title;
    String info;

    public Info(boolean isDone, boolean isOpen, String start, String end, String title, String info) {
        this.isDone = isDone;
        this.isOpen = isOpen;
        this.start = start;
        this.end = end;
        this.title = title;
        this.info = info;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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
}
