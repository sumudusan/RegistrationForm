import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.stream.StreamSupport;

public class RegistrationForm extends JDialog
{
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JTextField tfName;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel registerPanel;

    public RegistrationForm (JFrame parent)
    {
        super(parent);
        setTitle("Create an account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        registerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmpassword = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmpassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user=addUserToDatabase(name,email,phone,password);
        if (user!=null)
        {
            dispose();
        }
    }
    public User user;
    private User addUserToDatabase(String name,String email,String phone,String password)
        {
            User user=null;
            final String DB_URL="jdbc:mysql://localhost:3306/mystore";
            final String USERNAME="root";
            final String PASSWORD="";

            try {
                Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

                Statement stmt=conn.createStatement();
                String sql="INSERT INTO users (name,email,phone,password)"+
                        "VALUES (?,?,?,?)";
                PreparedStatement preparedStatement=conn.prepareStatement(sql);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,phone);
                preparedStatement.setString(4,password);

                //Insert row into table
                int addedRows =preparedStatement.executeUpdate();
                if (addedRows>0)
                {
                    user=new User();
                    user.name=name;
                    user.email=email;
                    user.phone=phone;
                    user.password=password;
                }
                stmt.close();;
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            return user;
        }


    public static void main(String[] args) {
        RegistrationForm myForm=new RegistrationForm(null);
        User user=myForm.user;
        if (user !=null)
        {
            System.out.println("Successfull registration of:"+user.name);
        }
        else
        {
            System.out.println("Registration cancelled");
        }
    }
}

