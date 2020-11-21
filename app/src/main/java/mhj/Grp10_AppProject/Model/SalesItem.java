package mhj.Grp10_AppProject.Model;

import java.io.Serializable;

public class SalesItem implements Serializable {

    public int ImageResourceId;
    public int ItemId;
    public String Description;
    public float Price;
    public String User;
    public String Location;
    public String Image;


    SalesItem()
    {
    }

    public SalesItem(int id, String desc, float price, String user, String location, int imgId){
        this.ItemId = id;
        this.Description = desc;
        this.Price = price;
        this.User = user;
        this.Location = location;
        this.ImageResourceId = imgId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
