/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.apache.commons.codec.digest.DigestUtils;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.User;
import org.lown.consultancy.accounts.dao.UserDAO;
import org.lown.consultancy.accounts.tables.ExpensesList;
import org.lown.consultancy.accounts.tables.UsersTable;

/**
 *
 * @author LENOVO USER
 */
public class UserDialog extends JPanel implements ActionListener{
    
    private static final String ACT_SAVE="add_user";
    private static final String ACT_VIEW="view";
    private static final String ACT_HIDE="hide";
    private static final String ACT_POST="save_Expense";       
    private static final String ACT_BACK="close";
    private static final String ACT_UPDATE="update_usr";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font titleFont2 = new Font("Times New Roman", Font.PLAIN, 14);
    
    
    private static JDialog dlgUsers;
    
    private JButton btnSave;        
    private JButton btnClose;
    private JButton btnUpdate;
    private JButton btnDelete;
    
    
    private JPanel pPerson;
    private JPanel pLogin;
    private JPanel pusersList;
    
    private JLabel lbl_sirName;
    private JLabel lbl_otherNames;
    private JLabel lbl_userName;
    private JLabel lbl_pass;
    private JLabel lbl_confirmPass;
    
    
    private JTextField txt_sirName;
    private JTextField txt_otherNames;
    private JTextField txt_userName;
    private JPasswordField txt_pass;
    private JPasswordField txt_confirmPass;
   
    
    private JCheckBox chk_clerk;
    private JCheckBox chk_admin;
    private JCheckBox chk_accountant;
    private JCheckBox chk_authenticated;
    
    private UsersTable usersTable;
    private ExpensesList contraTable2;
    
    private TitledBorder titled1 = new TitledBorder("Person Details");
    private TitledBorder titled2 = new TitledBorder("Login Details and Roles");
    private TitledBorder titled3 = new TitledBorder("List of Available Users");
    private User user;
    private UserDAO userDAO;    
    
    private List<User> usersList;
    private List<String>roles;
    
    public UserDialog()
    {
        titled2.setTitleFont(titleFont);
        titled1.setTitleFont(titleFont);
        
        dlgUsers= new JDialog((JDialog)null, "Manage Users", true);
        dlgUsers.setLayout(null);
        dlgUsers.setSize(750, 500);//Width size, Height size
        dlgUsers.setLocationRelativeTo(null);//center the invoice on the screen
        
        pPerson=new JPanel();
        pPerson.setBounds(20, 10, 400, 100);
        pPerson.setBorder(titled1);  
        pPerson.setLayout(null);
        dlgUsers.add(pPerson);
        
        pLogin=new JPanel();
        pLogin.setBounds(20, 110, 690, 130);
        pLogin.setBorder(titled2);  
        pLogin.setLayout(null);
        dlgUsers.add(pLogin);
        
        pusersList=new JPanel();
        pusersList.setBounds(20, 240, 690, 210);
        pusersList.setBorder(titled3);  
        pusersList.setLayout(null);        
        dlgUsers.add(pusersList);
        
        btnSave=new JButton("Add");
        btnSave.setBounds(450, 20, 100, 30);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        dlgUsers.add(btnSave); 
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(600, 20, 100, 30);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        dlgUsers.add(btnUpdate); 
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(450, 70, 100, 30);
        btnDelete.setActionCommand(ACT_BACK);
        btnDelete.addActionListener(this);
        dlgUsers.add(btnDelete);
        
        btnClose=new JButton("Close");
        btnClose.setBounds(600, 70, 100, 30);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgUsers.add(btnClose); 
        
        lbl_sirName=new JLabel();
        lbl_sirName.setBounds(20, 20, 150, 25);         
        lbl_sirName.setText("Sir Name:");
        lbl_sirName.setFont(titleFont2);
        pPerson.add(lbl_sirName);
        
        txt_sirName=new JTextField();
        txt_sirName.setBounds(150, 20,200, 25);         
        txt_sirName.setText("");        
        txt_sirName.setFont(titleFont2);        
        pPerson.add(txt_sirName); 
        
        lbl_otherNames=new JLabel();
        lbl_otherNames.setBounds(20, 50, 150, 25);         
        lbl_otherNames.setText("Other Names:");
        lbl_otherNames.setFont(titleFont2);
        pPerson.add(lbl_otherNames);
        
        txt_otherNames=new JTextField();
        txt_otherNames.setBounds(150, 50,200, 25);         
        txt_otherNames.setText("");        
        txt_otherNames.setFont(titleFont2);        
        pPerson.add(txt_otherNames); 
        
        lbl_userName=new JLabel();
        lbl_userName.setBounds(20, 20, 150, 25);         
        lbl_userName.setText("User Name:");
        lbl_userName.setFont(titleFont2);
        pLogin.add(lbl_userName);
        
        txt_userName=new JTextField();
        txt_userName.setBounds(150, 20,200, 25);         
        txt_userName.setText("");        
        txt_userName.setFont(titleFont2);        
        pLogin.add(txt_userName); 
        
        lbl_pass=new JLabel();
        lbl_pass.setBounds(20, 50, 150, 25);         
        lbl_pass.setText("Password:");
        lbl_pass.setFont(titleFont2);
        pLogin.add(lbl_pass);
        
        txt_pass=new JPasswordField();
        txt_pass.setBounds(150, 50,200, 25);         
        txt_pass.setText("");        
        txt_pass.setFont(titleFont2);        
        pLogin.add(txt_pass);
        
        lbl_confirmPass=new JLabel();
        lbl_confirmPass.setBounds(20, 80, 150, 25);         
        lbl_confirmPass.setText("Confirm Password:");
        lbl_confirmPass.setFont(titleFont2);
        pLogin.add(lbl_confirmPass);
        
        txt_confirmPass=new JPasswordField();
        txt_confirmPass.setBounds(150, 80,200, 25);         
        txt_confirmPass.setText("");        
        txt_confirmPass.setFont(titleFont2);        
        pLogin.add(txt_confirmPass);
        
        chk_authenticated=new JCheckBox("Authenticated");
        chk_authenticated.setBounds(400, 40,150, 25); 
        pLogin.add(chk_authenticated);
        
        chk_clerk=new JCheckBox("Clerk");
        chk_clerk.setBounds(400, 80,100, 25); 
        pLogin.add(chk_clerk);
        
        chk_admin=new JCheckBox("Admin");
        chk_admin.setBounds(550, 40,100, 25); 
        pLogin.add(chk_admin);
        
        chk_accountant=new JCheckBox("Accountant");
        chk_accountant.setBounds(550, 80,100, 25); 
        pLogin.add(chk_accountant);
        
         //Users List Table
        usersTable=new UsersTable();
        usersTable.setBounds(10,15,650, 180);
        pusersList.add(usersTable);
        usersTable.insertRow();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgUsers.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnSave.getText().equalsIgnoreCase("Add"))
            {
                
                //clear text boxes
                txt_confirmPass.setText("");
                txt_pass.setText("");
                txt_sirName.setText("");
                txt_userName.setText("");
                txt_otherNames.setText("");
                
                btnDelete.setEnabled(false);
                btnSave.setText("Save");
                btnUpdate.setText("Cancel");
                
                chk_accountant.setSelected(false);
                chk_admin.setSelected(false);
                chk_clerk.setSelected(false);
                chk_authenticated.setSelected(false);
                
                return;
            }
            else if(btnSave.getText().equalsIgnoreCase("Save"))
            {
                
                String passText = new String(txt_pass.getPassword());
                String confirmPassText = new String(txt_confirmPass.getPassword());
                if(!passText.equals(confirmPassText))
                {
                    JOptionPane.showMessageDialog(null, "The Passwords are not Matching...");
                    return;
                }
                
                if(txt_sirName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the user's Sir Name...");
                    return;
                }
                
                if(txt_otherNames.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter user's other Names...");
                    return;
                }
                
                if(txt_pass.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the user's Password...");
                    return;
                }
                
                 if(txt_userName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the user's login user name...");
                    return;
                }
                
                roles=new ArrayList<String>();
                if (chk_accountant.isSelected())
                {
                    roles.add(chk_accountant.getText().toLowerCase());
                }
                
                if (chk_admin.isSelected())
                {
                    roles.add(chk_admin.getText().toLowerCase());
                }
                
                if (chk_authenticated.isSelected())
                {
                    roles.add(chk_authenticated.getText().toLowerCase());
                }
                
                if (chk_clerk.isSelected())
                {
                    roles.add(chk_clerk.getText().toLowerCase());
                }
                
                if (roles.isEmpty())
                {
                   JOptionPane.showMessageDialog(null, "No Role has been assigned to the user...");
                   return; 
                }
                for(String s: roles)
                {
                    System.out.println("Roles: "+s);
                }
                
                
                user=new User();
                user.setName(txt_sirName.getText());
                try {
                    user.setPassword(MySQLPassword(passText));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                user.setUserName(txt_userName.getText());
                user.setOtherNames(txt_otherNames.getText());
                user.setRoles(roles);  
                
                userDAO=new UserDAO();
                try {
                    userDAO.saveUser(user);
                    btnSave.setText("Add");
                    btnUpdate.setText("Update");
                    btnDelete.setEnabled(true);
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
	}
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            if(btnUpdate.getText().equalsIgnoreCase("Cancel"))
            {
                
                //clear text boxes
                txt_confirmPass.setText("");
                txt_pass.setText("");
                txt_sirName.setText("");
                txt_userName.setText("");
                txt_otherNames.setText("");
                
                btnDelete.setEnabled(true);
                btnSave.setText("Add");
                btnUpdate.setText("Update");
                
                chk_accountant.setSelected(false);
                chk_admin.setSelected(false);
                chk_clerk.setSelected(false);
                chk_authenticated.setSelected(false);
                
                return;
            }
            else if(btnUpdate.getText().equalsIgnoreCase("Update"))
            {
                return;
            }
        }
    }
    
    public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Contra Expenses Dialog");
         UserDialog userDialog = new UserDialog();      
         
         dlgUsers.setVisible(true);          
         dlgUsers.dispose(); //close the app once done
     }
    /*
     * Passwhord Hashing function using sha1
     */
    public static String MySQLPassword(String plainText)  throws UnsupportedEncodingException
    {
        byte[] utf8 = plainText.getBytes("UTF-8");
        byte[] test = DigestUtils.sha(DigestUtils.sha(utf8));
        
        return "*" + convertToHex(test).toUpperCase();
    }
    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
}
