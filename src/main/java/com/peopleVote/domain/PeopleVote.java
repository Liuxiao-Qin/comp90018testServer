package com.peopleVote.domain;

public class PeopleVote {
    private int groupNumber;
    private String locationName;
    private String voteStartTime;
    private String username;
    private String ifVote;

    public PeopleVote(int groupNumber, String locationName, String voteStartTime, String username, String ifVote) {
        this.groupNumber = groupNumber;
        this.locationName = locationName;
        this.voteStartTime = voteStartTime;
        this.username = username;
        this.ifVote = ifVote;
    }

    public PeopleVote() {
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getVoteStartTime() {
        return voteStartTime;
    }

    public void setVoteStartTime(String voteStartTime) {
        this.voteStartTime = voteStartTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIfVote() {
        return ifVote;
    }

    public void setIfVote(String ifVote) {
        this.ifVote = ifVote;
    }

    @Override
    public String toString() {
        return "PeopleVote{" +
                "groupNumber=" + groupNumber +
                ", locationName='" + locationName + '\'' +
                ", voteStartTime='" + voteStartTime + '\'' +
                ", username='" + username + '\'' +
                ", ifVote='" + ifVote + '\'' +
                '}';
    }
}
