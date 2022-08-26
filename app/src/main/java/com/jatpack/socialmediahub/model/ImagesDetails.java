package com.jatpack.socialmediahub.model;

public class ImagesDetails {

    String date;
    String ImagePath;
    boolean b;
    ImagesDetails(String date , String imagePath, boolean b){
         this.date = date;
         this.ImagePath = imagePath;
         this.b =b;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getImagePath(){
        return ImagePath;
    }
    public void setImagePath(String imagePath){
        this.ImagePath = imagePath;
    }
    public boolean getB(){return b;}
    public void setB(boolean checked){this.b=b;}

}
