package Main.GUI;

import Main.Console.User;
import Main.WestminsterShoppingManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

public class LoginPanel extends JFrame
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;


    private static User LoggedUser;

    public LoginPanel()
    {
        setVisible( true );
        // Create labels for username and password
        JLabel usernameLabel = new JLabel( "Username:" );
        JLabel passwordLabel = new JLabel( "Password:" );

        // Create text field for username
        usernameField = new JTextField( 20 );

        // Create password field
        passwordField = new JPasswordField( 20 );

        // Create login button
        loginButton = new JButton( "Login" );

        // Add action listener to login button
        loginButton.addActionListener( new LoginButtonListener() );

        getContentPane().setBackground( new Color( 173, 216, 230 ) );

        // Layout components using GridBagLayout
        setLayout( new GridBagLayout() );
        GridBagConstraints gc = new GridBagConstraints();

        // Create a panel for creative design
        JPanel creativePanel = new JPanel();
        creativePanel.setLayout( new BorderLayout() );
        creativePanel.setBackground( new Color( 173, 216, 230 ) ); // Light Blue background

        // Create a larger label for creative design
        JLabel creativeLabel = new JLabel( "<html><font color='black' size='6'>Welcome to</font></html>" );
        creativeLabel.setHorizontalAlignment( SwingConstants.CENTER );
        creativePanel.add( creativeLabel, BorderLayout.NORTH );

        // Additional label for Westminster Shopping Center
        JLabel shoppingCenterLabel = new JLabel( "<html><font color='black' size='7'>Westminster Shopping Center</font></html>" );
        creativePanel.add( shoppingCenterLabel, BorderLayout.CENTER );

        // Add creative panel to the frame
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        add( creativePanel, gc );

        // Reset gridwidth for other components
        gc.gridwidth = 1;

        // First row
        gc.gridx = 0;
        gc.gridy = 1;
        add( usernameLabel, gc );
        gc.gridx = 1;
        gc.gridy = 1;
        add( usernameField, gc );

        // Second row
        gc.gridx = 0;
        gc.gridy = 2;
        add( passwordLabel, gc );
        gc.gridx = 1;
        gc.gridy = 2;
        add( passwordField, gc );

        // Third row
        gc.gridx = 1;
        gc.gridy = 3;
        add( loginButton, gc );

        setSize( 600, 400 );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null ); // Center the frame on the screen
        setBackground( new Color( 173, 216, 230 ) );
    }

    public static User getLoggedUser()
    {
        return LoggedUser;
    }


    private class LoginButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            // Get username and password
            String username = usernameField.getText();
            String password = new String( passwordField.getPassword() );

            // Check credentials
            if( !checkValid( username, password ) )
            {
                JOptionPane.showMessageDialog( null, "Invalid username or password" );
            }
            else if( checkValid( username,password ) )
            {
                LoginPanel.this.dispose();
                LoggedUser = new User(username,password);
                MainFrame mainFrame = new MainFrame();

            }

        }

        private boolean checkValid( String username, String password )
        {
            HashSet<User> users = WestminsterShoppingManager.getUsers();
            for( User user : users )
            {
                if( user.getUsername().equals( username ) && ( user.getPassword().equals( password ) ) )
                {
                    return true;
                }
            }
            return false;
        }
       
    }





    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () ->
        {
            new LoginPanel().setVisible( true );
        } );
    }
}
