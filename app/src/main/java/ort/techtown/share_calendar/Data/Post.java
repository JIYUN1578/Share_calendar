package ort.techtown.share_calendar.Data;

import java.util.ArrayList;

public class Post {
    private String image_url, title, summary, name, uid, time;
    private Boolean isVote;
    private ArrayList<Vote> voteArrayList;
    private ArrayList<String> votePersonList;
    private ArrayList<Comment> commentList;

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

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

    public ArrayList<Vote> getVoteArrayList() {return voteArrayList;}

    public void setVoteArrayList(ArrayList<Vote> voteArrayList) {this.voteArrayList = voteArrayList;}

    public ArrayList<String> getVotePersonList() {return votePersonList;}

    public void setVotePersonList(ArrayList<String> votePersonList) {this.votePersonList = votePersonList;}

    public ArrayList<Comment> getCommentList() {return commentList;}

    public void setCommentList(ArrayList<Comment> commentList) {this.commentList = commentList;}

    public Post(String uid, String name, String time, String image_url, String title, String summary, Boolean isVote, ArrayList<Vote> voteArrayList, ArrayList<String> votePersonList, ArrayList<Comment> commentList) {
        this.uid = uid;
        this.name = name;
        this.time = time;
        this.image_url = image_url;
        this.title = title;
        this.summary = summary;
        this.isVote = isVote;
        this.voteArrayList = voteArrayList;
        this.votePersonList = votePersonList;
        this.commentList = commentList;
    }
}
