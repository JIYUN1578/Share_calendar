package ort.techtown.share_calendar.Data;

public class Group {
    String groupname, introduce;

    public Group(){}

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Group(String groupname, String introduce) {
        this.groupname = groupname;
        this.introduce = introduce;
    }
}
