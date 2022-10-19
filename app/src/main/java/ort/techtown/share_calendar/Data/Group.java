package ort.techtown.share_calendar.Data;

import java.util.ArrayList;

public class Group {
    String groupname, introduce, hostuid, url;
    Integer groupnum;

    public Group(){}

    public String getGroupname() { return groupname; }

    public void setGroupname(String groupname) { this.groupname = groupname; }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getHostuid() { return hostuid; }

    public void setHostuid(String hostuid) { this.hostuid = hostuid; }

    public Integer getGroupnum() { return groupnum; }

    public void setGroupnum(Integer groupnum) { this.groupnum = groupnum; }

    public Group(String groupname, String introduce, String hostuid, Integer groupnum) {
        this.groupname = groupname;
        this.introduce = introduce;
        this.hostuid = hostuid;
        this.groupnum = groupnum;
    }
}
