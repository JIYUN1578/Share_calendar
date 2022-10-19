package ort.techtown.share_calendar.Data;

public class GroupRecyclerView {
    String group_name, group_introduce;

    public GroupRecyclerView(String group_name, String group_introduce) {
        this.group_name = group_name;
        this.group_introduce = group_introduce;
    }

    public GroupRecyclerView() {
        this.group_name = "";
        this.group_introduce = "";
    }

    public String getGroup_name() { return group_name; }

    public void setGroup_name(String group_name) { this.group_name = group_name; }

    public String getGroup_introduce() { return group_introduce; }

    public void setGroup_introduce(String group_introduce) { this.group_introduce = group_introduce; }
}
