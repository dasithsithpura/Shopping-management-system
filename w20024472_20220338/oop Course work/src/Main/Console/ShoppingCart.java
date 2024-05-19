package Main.Console;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products;
    private String filePath = "products.txt";

    public ShoppingCart() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
        } else {
            System.out.println("Main.Console.Product not found in the cart.");
        }
    }

    public double calculateTotalCost() {
        double totalCost = 0;
        for (Product product : products) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void saveProductsToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(products);
            System.out.println("Products saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public void loadProductsFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Product> savedProducts = (List<Product>) inputStream.readObject();
            products.addAll(savedProducts);
            System.out.println("Products loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }
}
