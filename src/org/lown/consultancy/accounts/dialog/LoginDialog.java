/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.User;
import org.lown.consultancy.accounts.dao.UserDAO;

/**
 *
 * @author LENOVO USER
 */
public class LoginDialog extends JPanel implements ActionListener{
    public static final String ACT_LOGIN="login";
    public static final String ACT_BACK="exit";
    
    private static JDialog dlgLoginForm;
    private JButton btnBack;
    public JButton btnLogin; 
    
    public static JTextField txt_uName;
    public static JPasswordField txt_Pass;
    public static Map<String,Integer>rolesMap;
    
    public static final Font font = new Font("Times New Roman", Font.PLAIN, 16);
    private JLabel lbl_uName;
    private JLabel lbl_Password;
    private JLabel lbl_Title;
    private User user;
    private UserDAO userDao;
    
     
    public LoginDialog()
    {
        //log info
        
        AccountsManagement.logger.info("Application User Log on UI...");
        user =new User();
        userDao=new UserDAO();
        rolesMap=userDao.getUserRoleMap();
        dlgLoginForm= new JDialog((JDialog)null, "LOGIN", true);
        dlgLoginForm.setLayout(null);
        dlgLoginForm.setSize(350, 300);
        dlgLoginForm.setLocationRelativeTo(null);//center the dialog
        
        lbl_Title=new JLabel();
        lbl_Title.setBounds(10, 10, 200, 20);         
        lbl_Title.setText("LOGIN DETAILS");
        lbl_Title.setFont(MainMenu.titleFont);         
        dlgLoginForm.add(lbl_Title);
        
        lbl_uName=new JLabel();
        lbl_uName.setBounds(10, 40, 100, 20);         
        lbl_uName.setText("User Name:"); 
        lbl_uName.setVerticalAlignment(JLabel.TOP);
        lbl_uName.setFont(font);
        dlgLoginForm.add(lbl_uName);
        
        txt_uName=new JTextField();
        txt_uName.setBounds(100, 40, 170, 25);          
        txt_uName.setFont(font);
        txt_uName.setToolTipText("Enter your Login username.");
        dlgLoginForm.add(txt_uName);
        
        lbl_Password=new JLabel();
        lbl_Password.setBounds(10, 80, 100, 20);         
        lbl_Password.setText("Password:");  
        lbl_Password.setFont(font);
        //lbl_Fname.setFont(font);
        dlgLoginForm.add(lbl_Password);
        
        txt_Pass=new JPasswordField();
        txt_Pass.setBounds(100, 80, 170, 25);          
        txt_Pass.setFont(font);
        txt_Pass.setToolTipText("Enter your login Password .");
        dlgLoginForm.add(txt_Pass);
        
        btnLogin=new JButton("Login");
        btnLogin.setBounds(10, 160, 100, 40);
        btnLogin.setActionCommand(ACT_LOGIN);
        btnLogin.addActionListener(this);
        btnLogin.setToolTipText("Click to Access the Company Center.");
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgLoginForm.add(btnLogin);
        
        btnBack=new JButton("Cancel");
        btnBack.setBounds(200, 160, 100, 40);
        btnBack.setActionCommand(ACT_BACK);
        btnBack.addActionListener(this);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setToolTipText("Click to Exit.");
        
        dlgLoginForm.getRootPane().setDefaultButton(btnLogin); //sent login button as the default button
         
        dlgLoginForm.add(btnBack);
        dlgLoginForm.setVisible(true);          
        dlgLoginForm.dispose(); //close the app once done
    }
     public static void createAndShowGUI()
     {
         LoginDialog login = new LoginDialog();
         AccountsManagement.logger.info("Loading The Log on Page");
         
     }
     
     public void actionPerformed(ActionEvent e)
     {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgLoginForm.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_LOGIN))
        {
            //dlgLoginForm.setVisible(false);
            if(txt_uName.getText()==null)
            {
                JOptionPane.showMessageDialog(null, "Enter User name...");
                return;
            }
            if(txt_Pass.getText()==null)
            {
                JOptionPane.showMessageDialog(null, "Enter Password...");
                return;
            }
            String pass=txt_Pass.getText();
            String uName=txt_uName.getText();            
            user=userDao.getUser(uName, pass);                      
            if(user!=null)
            {
                AccountsManagement.logger.info("Sucessfull Log on... Loading Main Menu");
                AccountsManagement.logger.info("User: "+ user.getName()+" Login  Sucessfull");
                dlgLoginForm.setVisible(false); 
                user.setRoles(userDao.getUserRole(user));
                MainMenu.createAndShowGUI(user);
                //MainMenu.createAndShowGUI();
            }
            else
            {
                AccountsManagement.logger.info("Failled Loging on User: "+txt_uName.getText());
                JOptionPane.showMessageDialog(null, "Invalid User name or Password Try Again...");
                return;
             }
          
            return;
	}
         
     }
        
    
}