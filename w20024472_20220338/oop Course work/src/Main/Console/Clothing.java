package Main.Console;

import java.io.Serializable;

public class Clothing extends Product implements Serializable
{
    private int size;
    private String color;


    public Clothing(String productId, String productName, int availableItems, double price,int size, String color) {
        super(productId, productName, availableItems, price,"Clothing");
        this.size = size;
        this.color = color;
    }
    public Clothing() {
        super("", "", 0, 0, "Clothing");
        this.size = 0;
        this.color = "";
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return super.toString() +
                       ", Clothing{" +
                       "size=" + size +
                       ", color='" + color + '\'' +
                       '}';
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getType() {
        return "Main.Console.Clothing";
    }
}
