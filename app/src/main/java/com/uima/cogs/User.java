package com.uima.cogs;

public class User {

    private String name;
    private String courses;
    private String imageUrl;

    public String getName(){
        return name;
    }

    public String getCourses(){
        return courses;
    }

    public String getImageUrl() {
        return imageUrl;
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
