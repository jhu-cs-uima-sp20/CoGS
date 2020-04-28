package com.uima.cogs;

public class Notes {
    private String name;
    private String imageUrl;

    public Notes(String name, String url) {
        this.name = name;
        this.imageUrl = url;
    }
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}


