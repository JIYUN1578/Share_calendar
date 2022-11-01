package ort.techtown.share_calendar.Data;

public class Grouplist {
    String grname;
    boolean isSeen;

    public String getGrname() {
        return grname;
    }

    public void setGrname(String grname) {
        this.grname = grname;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public Grouplist(String grname, boolean isSeen) {
        this.grname = grname;
        this.isSeen = isSeen;
    }
}
