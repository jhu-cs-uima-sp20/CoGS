package com.uima.cogs;

import java.util.ArrayList;

public class GroupDescription {

    private String groupName;
    private String courseName;
    private String shortDescription;
    private ArrayList<String> members = new ArrayList<>();

    public ArrayList<String> getMembers() {
        return members;
    }
    public String getCourseName() {
        return courseName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void addMembers(String memeberId){
        members.add(memeberId);
    }
}
