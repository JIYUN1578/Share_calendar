package ort.techtown.share_calendar.Class;

public class Post {
    private String image_url, title, summary, name;
    private Boolean vote;

    public Post() {}

    public String getImage_url() {return image_url;}

    public void setImage_url(String image_url) {this.image_url = image_url;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getSummary() {return summary;}

    public void setSummary(String summary) {this.summary = summary;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Boolean getVote() {return vote;}

    public void setVote(Boolean vote) {this.vote = vote;}
}
