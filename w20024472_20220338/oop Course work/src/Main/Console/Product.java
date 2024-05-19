package Main.Console;

import java.io.Serializable;

public abstract class Product implements Serializable
{
    private String productId;
    private String productName;
    private int availableItems;
    private double price;
    private String category;
    private String info;

    public String getInfo()
    {
        return info;
    }

    public void setInfo( String info )
    {
        this.info = info;
    }

    @Override
    public String toString()
    {
        return "Product{" +
                       "productId='" + productId + '\'' +
                       ", productName='" + productName + '\'' +
                       ", availableItems=" + availableItems +
                       ", price=" + price +
                       ", category='" + category + '\'' +
                       ", info='" + info + '\'' +
                       '}';
    }
    public Product() {
        this.productId = "";
        this.productName = "";
        this.availableItems = 0;
        this.price = 0;
        this.category = "";
        this.info = "";
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory( String category )
    {
        this.category = category;
    }

    public Product( String productId, String productName, int availableItems, double price, String category) {
        this.category = category;
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailabilityFromId(int ID){
        return availableItems;

    }

}
