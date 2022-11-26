package ort.techtown.share_calendar.Data;

public class Comment {
    private String comment_name, comment_uid, comment_time, comment_summary;

    public Comment() {}

    public String getComment_name() {return comment_name;}

    public void setComment_name(String comment_name) {this.comment_name = comment_name;}

    public String getComment_uid() {return comment_uid;}

    public void setComment_uid(String comment_uid) {this.comment_uid = comment_uid;}

    public String getComment_time() {return comment_time;}

    public void setComment_time(String comment_time) {this.comment_time = comment_time;}

    public String getComment_summary() {return comment_summary;}

    public void setComment_summary(String comment_summary) {this.comment_summary = comment_summary;}

    public Comment(String comment_name, String comment_uid, String comment_time, String comment_summary) {
        this.comment_name = comment_name;
        this.comment_uid = comment_uid;
        this.comment_time = comment_time;
        this.comment_summary = comment_summary;
    }
}
