package com.uima.cogs;

import java.util.ArrayList;

public class User {

    private String name;
    private String courses;
    private String imageUrl;
    private ArrayList<String> groups = new ArrayList<>();

    public String getName(){
        return name;
    }

    public String getCourses(){
        return courses;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String name){
        groups.add(name);
    }

    public void setName(String userName){
        name = userName;
    }

    public void setCourses(String userCourses){
        courses = userCourses;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
