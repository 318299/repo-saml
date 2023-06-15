package com.aemcx.core.models;
public class AccordionComponent{
    private String restaurant;
    private String title;
    private String link;
    private String message;
    private String imageLink;
    
    public String getRestaurant(){
        return restaurant;
    }
    public void setRestaurant(String restaurant){
        this.restaurant = restaurant;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}