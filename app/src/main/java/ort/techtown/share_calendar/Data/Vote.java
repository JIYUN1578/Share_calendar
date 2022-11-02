package ort.techtown.share_calendar.Data;

import android.widget.CheckBox;

public class Vote {
    private String voteSummary;
    private Integer voteNum;
    private CheckBox checkbox_vote;

    public Vote() {}

    public String getVoteSummary() {return voteSummary;}

    public void setVoteSummary(String voteSummary) {this.voteSummary = voteSummary;}

    public Integer getVoteNum() {return voteNum;}

    public void setVoteNum(Integer voteNum) {this.voteNum = voteNum;}

    public CheckBox getCheckbox_vote() {return checkbox_vote;}

    public void setCheckbox_vote(CheckBox checkbox_vote) {this.checkbox_vote = checkbox_vote;}

    public Vote(String voteSummary, Integer voteNum, CheckBox checkbox_vote) {
        this.voteSummary = voteSummary;
        this.voteNum = voteNum;
        this.checkbox_vote = checkbox_vote;
    }
}
