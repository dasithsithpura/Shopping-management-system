package Main.Console;

import java.io.Serializable;

public class Electronics extends Product implements Serializable
{
    private int warrantyperiod;
    private String brand;


    public Electronics( String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price,"Electronics");
        this.warrantyperiod = warrantyPeriod;
        this.brand = brand;
    }
    public Electronics() {
        super("", "", 0, 0, "Electronics");
        this.warrantyperiod = 0;
        this.brand = "";
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyperiod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyperiod = warrantyPeriod;
    }

    public String getType() {
        return "Main.Console.Electronics";
    }

    @Override
    public String toString() {
        return super.toString() +
                       ", Electronics{" +
                       "warrantyperiod=" + warrantyperiod +
                       ", brand='" + brand + '\'' +
                       '}';
    }
}