package mhj.Grp10_AppProject.Model;

import android.location.Location;

import java.io.Serializable;

public class SalesItem implements Serializable {

    private int itemId;
    private String description;
    private float price;
    private String user;
    private int imageResourceId;
    private String image;
    private Location location;


    SalesItem()
    {
    }

    public SalesItem(int id, String desc, float price, String user, int imgId){
        this.itemId = id;
        this.description = desc;
        this.price = price;
        this.user = user;
        this.imageResourceId = imgId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
