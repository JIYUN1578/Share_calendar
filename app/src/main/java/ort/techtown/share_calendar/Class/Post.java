package ort.techtown.share_calendar.Class;

import java.util.ArrayList;

public class Post {
    private String image_url, title, summary, name, uid;
    private Boolean isVote;

    public Post() {}

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}

    public String getImage_url() {return image_url;}

    public void setImage_url(String image_url) {this.image_url = image_url;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getSummary() {return summary;}

    public void setSummary(String summary) {this.summary = summary;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Boolean getVote() {return isVote;}

    public void setVote(Boolean vote) {isVote = vote;}
}
