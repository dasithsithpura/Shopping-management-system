package Main.GUI;


import Main.Console.Clothing;
import Main.Console.CustomerHistory;
import Main.Console.Electronics;
import Main.Console.Product;
import Main.Console.User;
import Main.WestminsterShoppingManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame
{

    private JComboBox<String> categoryCombo;
    private JTable productTable;
    private JTextArea productDetails;
    private JButton addToCartButton;
    private JButton showBasketBtn;
    private DefaultTableModel tableModel;
    private static List<Product> products;
    private static List<Product> selectedProducts;
    private Map<String,Integer> productQuantities;
    private BasketFrame basketFrame;


    public MainFrame()
    {
        super( "Shopping App" );
        products = WestminsterShoppingManager.getProductList();
        productQuantities = new HashMap<>();
        selectedProducts = new ArrayList<>();  // Initialize selectedProducts
        initComponents();
        setupLayout();
        populateProductTable();
        addListeners();
        sort();
    }

    /**
     * Initialize components
     */
    private void initComponents()
    {
        // Initialize components
        categoryCombo = new JComboBox<>();
        productTable = new JTable();
        productDetails = new JTextArea();
        addToCartButton = new JButton( "Add to Cart" );
        showBasketBtn = new JButton( "Cart" );
        showBasketBtn.setVisible( false );

        // Set up category combo box
        categoryCombo.addItem( "All" );
        categoryCombo.addItem( "Electronics" );
        categoryCombo.addItem( "Clothing" );

        // Set up product table
        String[] columnNames = { "Product ID", "Name", "Category", "Price", "Info" };
        tableModel = new DefaultTableModel( columnNames, 0 );
        productTable.setModel( tableModel );
        TableColumn column = productTable.getColumnModel().getColumn( 4 );
        column.setPreferredWidth(160);
    }

    /**
 w    * Populate the product table with products
     */
    private void populateProductTable()
    {
        for( Product product : products )
        {
            productQuantities.put( product.getProductId(), product.getAvailableItems() );

            String info;
            if( product instanceof Clothing )
            {
                Clothing cloth = ( Clothing ) product;
                info = "Colour -"+cloth.getColor() + "    SIZE    -"+ cloth.getSize();
            }
            else
            {
                Electronics electronics = ( Electronics ) product;
                info = "Brand -"+ electronics.getBrand() + "     WarrantyPeriod -" + electronics.getWarrantyPeriod();
            }

            Object[] productRow = { product.getProductId(), product.getProductName(), product.getCategory(),
                    product.getPrice(), info };
            tableModel.addRow( productRow );
            productTable.getTableHeader().setResizingAllowed(false);
            productTable.getTableHeader().setReorderingAllowed(false);
            productTable.getTableHeader().setResizingAllowed(false);


        }
    }

    /**
     * Sort the product table by price in ascending order
     */
    private void sort()
    {
        productTable.setAutoCreateRowSorter( true );
        productTable.setRowSorter( new TableRowSorter<>( productTable.getModel() ) );

        TableRowSorter<DefaultTableModel> sorter = ( TableRowSorter<DefaultTableModel> ) productTable.getRowSorter();
        sorter.setSortKeys( Collections.singletonList( new RowSorter.SortKey( 1, SortOrder.ASCENDING ) ) );
    }

    /**
     * Set up the layout of the frame
     */
    private void setupLayout()
    {
        // Create panels
        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        topPanel.setLayout( new GridLayout( 2, 3, 20, 20 ) );

        topPanel.add( new JPanel() );
        topPanel.add( new JPanel() );
        topPanel.add( showBasketBtn );
        topPanel.add( new JPanel() );
        topPanel.add( categoryCombo );
        topPanel.add( new JPanel() );

        centerPanel.setLayout( new GridLayout( 3, 1 ) );
        centerPanel.add( new JScrollPane( productTable ) );
        centerPanel.add( new JScrollPane( productDetails ) );

        bottomPanel.setLayout( new FlowLayout() );
        bottomPanel.add( addToCartButton );
        centerPanel.add( bottomPanel );

        setLayout( new BorderLayout() );
        add( topPanel, BorderLayout.NORTH );
        add( centerPanel, BorderLayout.CENTER );


        pack();
        setVisible( true );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setSize( 800, 600 );
    }

    public static List<Product> getProducts()
    {
        return products;
    }


    /**
     * Add listeners to components
     */
    private void addListeners()
    {
        categoryCombo.addActionListener( e ->
        {
            String category = ( String ) categoryCombo.getSelectedItem();
            filterProductTable( category );
        } );

        addToCartButton.addActionListener( e ->
        {
            showBasketBtn.setVisible( true );

            if( basketFrame == null )
            {
                basketFrame = new BasketFrame();
            }

            int row = productTable.getSelectedRow();
            if( row >= 0 )
            {
                Product product = products.get( row );
                selectedProducts.add( product );

                boolean productExistsInBasket = false;
                int currentQuantity = 0;
                for( int i = 0; i < basketFrame.getBasketTable().getRowCount(); i++ )
                {
                    if( basketFrame.getBasketTable().getValueAt( i, 0 ).equals( product.getProductName() ) )
                    {

                        currentQuantity = ( int ) basketFrame.getBasketTable().getValueAt( i, 1 );
                        basketFrame.getBasketTable().setValueAt( currentQuantity + 1, i, 1 );

                        double newTotalPrice = ( currentQuantity + 1 ) * product.getPrice();
                        basketFrame.getBasketTable().setValueAt( newTotalPrice, i, 2 );

                        productExistsInBasket = true;
                        break;
                    }
                }

                if( !productExistsInBasket )
                {
                    Object[] newRow = { product.getProductName(), 1, product.getPrice() };
                    ( ( DefaultTableModel ) basketFrame.getBasketTable().getModel() ).addRow( newRow );
                }

                // Update product quantities

                User user = LoginPanel.getLoggedUser();
                List<CustomerHistory> customerHistoryList = WestminsterShoppingManager.getCustomerHistoryList();

                User existingUser = findUserInHistoryList( user.getUsername(), customerHistoryList );
                User currentUser = ( existingUser != null ) ? existingUser : user;

                boolean isFirstTimePurchase = ( existingUser == null );

                CustomerHistory customerHistory = new CustomerHistory( currentUser, selectedProducts, 0 );
                customerHistory.setFirstTimePurchesDiscount( isFirstTimePurchase );
                customerHistoryList.add( customerHistory );
                basketFrame.updateBill( selectedProducts, currentUser );


            }
        } );
        ;


        showBasketBtn.addActionListener( e ->
        {
            if( basketFrame == null )
            {
                basketFrame = new BasketFrame();
            }
            basketFrame.setVisible( true );
        } );

        productTable.getSelectionModel().addListSelectionListener( e ->
        {
            if( !e.getValueIsAdjusting() && productTable.getSelectedRow() >= 0 )
            {
                int row = productTable.getSelectedRow();
                displayProductDetails( row );
            }
        } );

        /**
         * Highlight products with low stock
         */
        productTable.setDefaultRenderer( Object.class, new DefaultTableCellRenderer()
        {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column )
            {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String productId = ( String ) table.getValueAt( row, 0 );
                int quantity = productQuantities.getOrDefault( productId, 0 );

                if( quantity < 3 )
                {
                    c.setBackground( Color.RED );
                }
                else
                {
                    c.setBackground( null );
                }

                return c;
            }
        } );
    }

    /**
     * Count the number of products in a list with a given category
     * @param usernameToFind The username to find
     * @param historyList The list to search
     * @return The user if found, null otherwise
     */
    private User findUserInHistoryList( String usernameToFind, List<CustomerHistory> historyList )
    {
        for( CustomerHistory history : historyList )
        {
            User historyUser = history.getUser();
            if( historyUser != null && historyUser.getUsername().equals( usernameToFind ) )
            {
                return historyUser;
            }
        }
        return null;
    }

    /**
     * Filter the product table by category
     * @param category The category to filter by
     */
    private void filterProductTable( String category )
    {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>( tableModel );
        productTable.setRowSorter( sorter );

        if( category.equals( "All" ) )
        {
            sorter.setRowFilter( null );
        }
        else
        {
            sorter.setRowFilter( RowFilter.regexFilter( category, 2 ) );
        }
    }


    public static List<Product> getSelectedProducts()
    {
        return selectedProducts;
    }

    public void setSelectedProducts( List<Product> selectedProducts )
    {
        this.selectedProducts = selectedProducts;
    }




    private void displayProductDetails( int row )
    {
        String productId = ( String ) productTable.getValueAt( row, 0 );
        String name = ( String ) productTable.getValueAt( row, 1 );
        String category = ( String ) productTable.getValueAt( row, 2 );
        Double price = ( Double ) productTable.getValueAt( row, 3 );
        String info = ( String ) productTable.getValueAt( row, 4 );


        productDetails.setEditable(false);
        productDetails.setLineWrap(true);
        productDetails.setWrapStyleWord(true);

        String details = """
                        **SELECTED PRODUCT**
                        Product ID :  %s
                        Name        :  %s
                        Price         :  %.2f
                        Category   :  %s
                        Details      :  %s"""
                                 .formatted(productId, name, price, category, info);

        // Apply font and alignment enhancements
        productDetails.setFont(new Font("Arial", Font.PLAIN, 18));
        productDetails.setAlignmentX(CENTER_ALIGNMENT);
        productDetails.setAlignmentY(CENTER_ALIGNMENT);
        productDetails.setText(details);
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( MainFrame::new );
    }
}
