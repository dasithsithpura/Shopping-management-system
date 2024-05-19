package Main;

import Main.Console.Clothing;
import Main.Console.CustomerHistory;
import Main.Console.Electronics;
import Main.Console.Product;
import Main.Console.ShoppingCart;
import Main.Console.User;
import Main.GUI.LoginPanel;
import Main.GUI.MainFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class WestminsterShoppingManager
{
    public static List<Product> productList;
    private ShoppingCart cart;
    private static final String FILENAME = "product_list.dat";
    private static HashSet<User> users = new HashSet<>();
    private static List<CustomerHistory> customerHistoryList = new ArrayList<>();
    public static HashSet<User> getUsers()
    {
        return users;
    }


    public WestminsterShoppingManager()
    {
        productList = new ArrayList<>();
        cart = new ShoppingCart();
        String filePath = "./users.txt";
        readUserFile( filePath );

    }

    public static List<CustomerHistory> getCustomerHistoryList()
    {
        return customerHistoryList;
    }

    public void readUserFile( String filePath )
    {
        try( BufferedReader br = new BufferedReader( new FileReader( filePath ) ) )
        {
            String line;
            while( ( line = br.readLine() ) != null )
            {
                String[] parts = line.split( "=" );
                if( parts.length == 2 )
                {
                    String[] userInfo = parts[1].split( "/" );
                    if( userInfo.length == 2 )
                    {
                        String username = userInfo[0].trim();
                        String password = userInfo[1].trim();
                        User user = new User( username, password );
                        users.add( user );
                    }
                }
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }



    // Other existing methods...

    public static List<Product> getProductList()
    {
        return productList;
    }
    public void saveProductListToFile() {
        try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(productList);
            System.out.println("Product list saved to file: " + FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadProductsFromFile() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILENAME));

            try {
                this.productList = (List)inputStream.readObject();
                System.out.println("\nProducts loaded from file successfully.\n");
            } catch (Throwable var5) {
                try {
                    inputStream.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            inputStream.close();
        } catch (ClassNotFoundException | IOException var6) {
            System.out.println(var6);
            System.out.println("\nNo previous data found. Starting with an empty product list.\n");
        }

    }

    /**
     * Add a new product to the productList
     * @param scanner
     */
    public void addNewProduct( Scanner scanner )
    {
        if( productList.size() >= 50 )
        {
            System.out.println( "Maximum product limit reached. Cannot add more products." );
            return;
        }

        System.out.println( "Add a new product to the system:" );
        System.out.println( "Select the product type:" );
        System.out.println( "1. Electronics Product" );
        System.out.println( "2. Clothing Product" );
        System.out.print( "Enter your choice: " );

        int choice;
        if( scanner.hasNextInt() )
        {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character after nextInt()

            switch( choice )
            {
                case 1:
                    addElectronicsProduct( scanner );
                    break;
                case 2:
                    addClothingProduct( scanner );
                    break;
                default:
                    System.out.println( "Invalid choice. Please enter 1 for Electronics or 2 for Clothing." );
                    break;
            }
        }
        else
        {
            System.out.println( "Invalid input. Please enter a number." );
            scanner.nextLine(); // Consume invalid input
        }
    }

    /**
     * Add a new electronics product to the productList
     * @param scanner
     */
    private void addElectronicsProduct( Scanner scanner )
    {
        System.out.println( "Adding a new electronics product..." );
        System.out.print( "Enter Product ID: " );
        String productId = scanner.nextLine();

        // Get other necessary information for electronics product
        // For example:
        System.out.print( "Enter Product Name: " );
        String productName = scanner.nextLine();

        System.out.print( "Enter Available Items: " );
        int availableItems = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after nextInt()

        System.out.print( "Enter Price: " );
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character after nextDouble()

        System.out.print( "Enter Brand: " );
        String brand = scanner.nextLine();

        System.out.print( "Enter Warranty Period (in months): " );
        int warrantyPeriod = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after nextInt()


        // Create an Main.Console.Electronics object and add it to the productList
        Electronics newElectronics = new Electronics( productId, productName, availableItems, price, brand, warrantyPeriod );
        productList.add( newElectronics );

        System.out.println( "Electronics product added successfully!" );
    }

    /**
     * Add a new clothing product to the productList
     * @param scanner
     */
    private void addClothingProduct( Scanner scanner )
    {
        System.out.println( "Adding a new clothing product..." );
        System.out.print( "Product ID: " );
        String productId = scanner.nextLine();

        // Get other necessary information for clothing product
        // For example:
        System.out.print( "Enter Product Name: " );
        String productName = scanner.nextLine();

        System.out.print( "Enter Available Items: " );
        int availableItems = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after nextInt()

        System.out.print( "Enter Price: " );
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character after nextDouble()

        System.out.print( "Enter Size: " );
        int size = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after nextInt()

        System.out.print( "Enter Color: " );
        String color = scanner.nextLine();

        // Create a Main.Console.Clothing object and add it to the productList
        Clothing newClothing = new Clothing( productId, productName, availableItems, price, size, color );
        productList.add( newClothing );

        System.out.println( "Clothing product added successfully!" );
    }

    /**
     * Delete a product from the productList
     * @param scanner
     */
    public void deleteProduct( Scanner scanner )
    {
        System.out.println( "Enter the Product ID to delete:" );
        String productIdToDelete = scanner.nextLine();

        boolean productFound = false;
        Product deletedProduct = null;
        for( Product product : productList )
        {
            if( product.getProductId().equals( productIdToDelete ) )
            {
                deletedProduct = product;
                productFound = true;
                productList.remove( product );
                break;
            }
        }

        if( productFound )
        {
            String productType = ( deletedProduct instanceof Electronics ) ? "Electronics" : "Clothing";
            System.out.println( productType + " product with ID " + productIdToDelete + " has been deleted." );

            int remainingProducts = productList.size();
            System.out.println( "Total number of products left in the system: " + remainingProducts );
        }
        else
        {
            System.out.println( "Product with ID " + productIdToDelete + " not found in the system." );
        }
    }

    /**
     * Print the productList
     */
    public void printProductList()
    {
        if( productList.isEmpty() )
        {
            System.out.println( "No products in the system." );
            return;
        }

        System.out.println( "List of Products in the System (Ordered by Product ID):" );
        Collections.sort( productList, Comparator.comparing( Product::getProductId ) );
        for( Product product : productList )
        {
            if( product instanceof Electronics )
            {
                System.out.println( "Product ID: " + product.getProductId() + " | Type: Electronics" );
                System.out.println( "Product Name: " + product.getProductName() );
                System.out.println( "Available Items: " + product.getAvailableItems() );
                System.out.println( "Price: " + product.getPrice() );
                System.out.println( "Brand: " + ( ( Electronics ) product ).getBrand() );
                System.out.println( "Warranty Period: " + ( ( Electronics ) product ).getWarrantyPeriod() + " months" );
            }
            else if( product instanceof Clothing )
            {
                System.out.println( "Product ID: " + product.getProductId() + " | Type: Clothing" );
                System.out.println( "Product Name: " + product.getProductName() );
                System.out.println( "Available Items: " + product.getAvailableItems() );
                System.out.println( "Price: " + product.getPrice() );
                System.out.println( "Size: " + ( ( Clothing ) product ).getSize() );
                System.out.println( "Color: " + ( ( Clothing ) product ).getColor() );
            }
            System.out.println( "---------------------------------------" );
            System.out.println( "invalid choice" );
        }
    }

    public static void main( String[] args )
    {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.loadProductsFromFile();
        Scanner scanner = new Scanner( System.in );


        boolean exit = false;
        while( !exit )
        {
            System.out.println("""
                                
                                ---------MENU-------------
                                1. Add New Product
                                2. Delete Product
                                3. Print Product List
                                4. Save Product List to file
                                5.Load data
                                6. Open GUI
                                7. Exit
                                --------------------------
                                """);
            System.out.print("Enter your choice: ");

            if( scanner.hasNextInt() )
            {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch( choice )
                {
                    case 1:
                        manager.addNewProduct( scanner );
                        break;
                    case 2:
                        manager.deleteProduct( scanner );
                        break;
                    case 3:
                        manager.printProductList();
                        break;
                    case 4:
                        manager.saveProductListToFile();
                        break;
                    case 5:
                        manager.loadProductsFromFile();
                        break;
                    case 6:
                        LoginPanel loginPanel = new LoginPanel();
                        break;
                    case 7:
                        exit = true;
                        System.out.println( "Exiting Westminster Shopping Manager. Goodbye!" );
                        break;
                    default:
                        System.out.println( "Invalid choice. Please enter a number" );
                        break;
                }
            }
            else
            {
                System.out.println( "Invalid input. Please enter a number." );
                scanner.nextLine();
            }
        }
        scanner.close();
    }

}
