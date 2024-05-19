package Main.GUI;


import Main.Console.CustomerHistory;
import Main.Console.Product;
import Main.Console.User;
import Main.WestminsterShoppingManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Main.WestminsterShoppingManager.getCustomerHistoryList;
import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class BasketFrame extends JFrame
{

    private JTable basketTable;
    private JTextArea billDetails;
    private JButton buy;
    double total;
    List<CustomerHistory> users = WestminsterShoppingManager.getCustomerHistoryList();

    public BasketFrame()
    {
        initializeComponents();
        initializeLayout();
        addListeners();
        pack();
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setSize( 600, 600 );
        setLocationRelativeTo( null );

    }

    private void addListeners()
    {
        buy.addActionListener( e ->
        {
            List<Product> productList = MainFrame.getSelectedProducts();
            List<CustomerHistory> customerHistoryList = WestminsterShoppingManager.getCustomerHistoryList();
            User user = LoginPanel.getLoggedUser();
            for( CustomerHistory history : customerHistoryList )
            {
                User historyUser = history.getUser();
                // Check if historyUser is not null before invoking equals
                if( historyUser != null && historyUser.equals( user ) )
                {
                    history.setFirstTimePurchesDiscount( false );
                }
            }
            BasketFrame.this.dispose();

        } );
    }


    private void initializeComponents()
    {
        setName( "Shopping Basket" );
        basketTable = new JTable();
        billDetails = new JTextArea();
        buy = new JButton( "Buy" );

    }

    private void initializeLayout()
    {
        JPanel containerPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        containerPanel.setLayout( new GridLayout( 2, 1 ) );
        bottomPanel.setLayout( new FlowLayout() );

        containerPanel.add( createBasketTablePanel() );
        containerPanel.add( createBillDetailsPanel() );
        bottomPanel.add( buy );

        getContentPane().add( containerPanel, BorderLayout.CENTER );
        getContentPane().add( bottomPanel, BorderLayout.SOUTH );
    }

    private JScrollPane createBasketTablePanel()
    {
        String[] columnNames = { "Product", "Quantity", "Price" };
        DefaultTableModel tableModel = new DefaultTableModel( columnNames, 0 );
        basketTable.setModel( tableModel );
        basketTable.getTableHeader().setReorderingAllowed(false);
        basketTable.getTableHeader().setResizingAllowed(false);
        basketTable.setRowHeight( 30 );
        return new JScrollPane( basketTable );


    }

    private JScrollPane createBillDetailsPanel()
    {
        return new JScrollPane( billDetails );
    }


    private double calculateItemTotal( Product product, int quantity )
    {
        return product.getPrice() * quantity;
    }


    private double calculateUserDiscount( List<CustomerHistory> histories, double subtotal, User user )
    {
        for( CustomerHistory history : histories )
        {
            if( history.getUser().equals( user ) )
            {
                if( history.isFirstTimePurchesDiscount() )
                {
                    return 0.1 * subtotal;
                }
            }
        }
        return 0.0;
    }


    public static void main( String[] args )
    {
        BasketFrame frame = new BasketFrame();
        frame.setVisible( true );
    }

    public JTable getBasketTable()
    {
        return basketTable;
    }

    public void updateBill( List<Product> selectedProducts, User user )
    {
        double subtotal = calculateSubtotal();
        double categoryDiscount = calculateCategoryDiscount( selectedProducts, subtotal );
        double userDiscount = calculateUserDiscount( users, subtotal, user );
        total = subtotal - categoryDiscount - userDiscount;
        billDetails.setText( formatBill( subtotal, categoryDiscount, userDiscount, total ) );
    }

    private double calculateCategoryDiscount( List<Product> products, double total )
    {
        int clothingCount = countProductsByCategory( products, "Clothing" );
        int electronicsCount = countProductsByCategory( products, "Electronics" );

        if( clothingCount >= 3 || electronicsCount >= 3 )
        {
            return 0.2 * total;
        }
        return 0;
    }

    private int countProductsByCategory( List<Product> products, String category )
    {
        int count = 0;

        for( Product product : products )
        {
            if( product.getCategory().equals( category ) )
            {
                count++;
            }
        }

        return count;
    }

    private double calculateSubtotal()
    {
        double subtotal = 0;
        DefaultTableModel tableModel = ( DefaultTableModel ) basketTable.getModel();

        for( int i = 0; i < tableModel.getRowCount(); i++ )
        {
            int quantity = ( int ) tableModel.getValueAt( i, 1 );
            double price = ( double ) tableModel.getValueAt( i, 2 );
            double itemTotal = price;
            subtotal += itemTotal;
        }

        return subtotal;
    }

    private int getProductQuantity( Product product )
    {
        DefaultTableModel tableModel = ( DefaultTableModel ) basketTable.getModel();

        for( int i = 0; i < tableModel.getRowCount(); i++ )
        {
            if( tableModel.getValueAt( i, 0 ).equals( product.getProductName() ) )
            {
                return ( int ) tableModel.getValueAt( i, 1 );
            }
        }

        return 0;
    }


    private String formatBill(double subtotal, double categoryDiscount, double userDiscount, double total) {
        String formattedString = String.format("Subtotal:                    £%.2f\nCategory Discount:  £%.2f\nUser Discount:         £%.2f\n-------------------------------------\nTotal:                        £%.2f",
                subtotal, categoryDiscount, userDiscount, total);


        billDetails.setFont(new Font("Arial", Font.BOLD, 18));
        billDetails.setAlignmentX(CENTER_ALIGNMENT);
        billDetails.setAlignmentY(CENTER_ALIGNMENT);

        return formattedString;
    }


}