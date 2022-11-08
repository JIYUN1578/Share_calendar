package ort.techtown.share_calendar.Data;

import android.widget.CheckBox;

import java.util.ArrayList;

public class Vote {
    private String voteSummary;
    private Integer voteNum;
    private CheckBox checkbox_vote;
    private ArrayList<String> personList;

    public Vote() {}

    public String getVoteSummary() {return voteSummary;}

    public void setVoteSummary(String voteSummary) {this.voteSummary = voteSummary;}

    public Integer getVoteNum() {return voteNum;}

    public void setVoteNum(Integer voteNum) {this.voteNum = voteNum;}

    public CheckBox getCheckbox_vote() {return checkbox_vote;}

    public void setCheckbox_vote(CheckBox checkbox_vote) {this.checkbox_vote = checkbox_vote;}

    public ArrayList<String> getPersonList() {return personList;}

    public void setPersonList(ArrayList<String> personList) {this.personList = personList;}

    public Vote(String voteSummary, Integer voteNum, CheckBox checkbox_vote, ArrayList<String> personList) {
        this.voteSummary = voteSummary;
        this.voteNum = voteNum;
        this.checkbox_vote = checkbox_vote;
        this.personList = personList;
    }
}
