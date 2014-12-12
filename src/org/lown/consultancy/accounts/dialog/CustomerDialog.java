/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.dao.CustomerService;
import org.lown.consultancy.accounts.tables.CustomerListTable;

/**
 *
 * @author LENOVO USER
 */
public class CustomerDialog extends JPanel implements ActionListener, FocusListener{
    
    //Action Listeners constants
    public static final String ACT_SAVE="save__update_customer";
    public static final String ACT_CUSTOMER="customer";
    public static final String ACT_DELETE="void_customer";
    public static final String ACT_CANCEL="cancel";
    public static final String ACT_BACK="exit";
    public static final String ACT_ENROLL="Enroll_Identify";
    public static final String ACT_FIND="search_customer";    
    public static final Font font = new Font("Times New Roman", Font.PLAIN, 16);
    //Text boxes and combo boxes
    public static JTextField txt_Custname;
    public static JTextField txt_Custnumber;
    public static JTextField txt_custAddress;
    public static JTextField txt_CustPhone;
    public static JTextField txt_CustContact;
   
    
    //Label controls
    private JLabel lbl_Custname;
    private JLabel lbl_Custnumber;
    private JLabel lbl_CustAddress;
    private JLabel lbl_CustPhone;   
    private JLabel lbl_CustContact;    
    private JLabel lbl_title;
    
    
    //Command Buttons
    public static JButton btnSave;
    public static JButton btnDelete;
    public static JButton btnEnroll;
    private JButton btnBack;
    public static JButton btnCancel;
    
    
    //Display panel
    private static JDialog dlgCustomer;
    
    public static Customer customer; //the new customer
    public static Customer oldCustomer; //currently selected customer
    public static CustomerService cs;
    
    public CustomerDialog()
    {
         //log info
        AccountsManagement.logger.info("Creating Register Customer UI...");
       
        dlgCustomer= new JDialog((JDialog)null, "CUSTOMER DETAILS", true);
        dlgCustomer.setLayout(null);
        //dlgCustomer.setBounds(300, 50,700, 400);
        dlgCustomer.setSize(700, 400);
        dlgCustomer.setLocationRelativeTo(null); //center the dialog
        
        customer = new Customer();
        cs=new CustomerService();
               
        lbl_title=new JLabel();
        lbl_title.setBounds(200, 10, 400, 20);         
        lbl_title.setText("CUSTOMER REGISTRATION DETAILS");
        lbl_title.setFont(MainMenu.titleFont);         
        dlgCustomer.add(lbl_title);
        
        
        
        lbl_Custnumber=new JLabel();
        lbl_Custnumber.setBounds(50, 80, 200, 20);         
        lbl_Custnumber.setText("Customer Number/ID:");  
        lbl_Custnumber.setFont(font);
        //lbl_Fname.setFont(font);
        dlgCustomer.add(lbl_Custnumber);
        
        txt_Custnumber=new JTextField();
        txt_Custnumber.setBounds(240, 80, 200, 25);          
        txt_Custnumber.setFont(font);
        dlgCustomer.add(txt_Custnumber);
        
        lbl_Custname=new JLabel();
        lbl_Custname.setBounds(50, 120, 200, 20);         
        lbl_Custname.setText("Customer/Company Name:");
        lbl_Custname.setFont(font);
        dlgCustomer.add(lbl_Custname);
        
        txt_Custname=new JTextField();
        txt_Custname.setBounds(240, 120, 350, 25);          
        txt_Custname.setFont(font);
        txt_Custname.setActionCommand(ACT_CUSTOMER);
        txt_Custname.addFocusListener(this);
        dlgCustomer.add(txt_Custname);
        
        lbl_CustAddress=new JLabel();
        lbl_CustAddress.setBounds(50, 160, 200, 20);         
        lbl_CustAddress.setText("Physical Address:");     
        lbl_CustAddress.setFont(font);
        dlgCustomer.add(lbl_CustAddress);
        
        txt_custAddress=new JTextField();
        txt_custAddress.setBounds(240, 160, 350, 25);          
        txt_custAddress.setFont(font);
        dlgCustomer.add(txt_custAddress);
        
        lbl_CustPhone=new JLabel();
        lbl_CustPhone.setBounds(50, 200, 200, 20);         
        lbl_CustPhone.setText("Phone:");     
        lbl_CustPhone.setFont(font);
        dlgCustomer.add(lbl_CustPhone);
        
        txt_CustPhone=new JTextField();
        txt_CustPhone.setBounds(240, 200, 200, 25);          
        txt_CustPhone.setFont(font);
        dlgCustomer.add(txt_CustPhone);   
        
        
        lbl_CustContact=new JLabel();
        lbl_CustContact.setBounds(50, 240, 200, 20);         
        lbl_CustContact.setText("Contact Person:"); 
        lbl_CustContact.setFont(font);
        dlgCustomer.add(lbl_CustContact);
        
        txt_CustContact=new JTextField();
        txt_CustContact.setBounds(240, 240, 350, 25);          
        txt_CustContact.setFont(font);
        dlgCustomer.add(txt_CustContact);
        
       
        btnSave=new JButton("Save Record");
        btnSave.setBounds(50, 300, 120, 40);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        //btnSave.setEnabled(false);
        dlgCustomer.add(btnSave);
        
        btnCancel=new JButton("Update");
        btnCancel.setBounds(200, 300, 120, 40);
        btnCancel.setActionCommand(ACT_CANCEL);
        btnCancel.addActionListener(this);
        dlgCustomer.add(btnCancel);      
        
       // btnFind=new JButton("Find");
       // btnFind.setBounds(500, 220, 150, 40);
       // btnFind.setActionCommand(ACT_FIND);
       // btnFind.addActionListener(this);
       // dlgCustomer.add(btnFind);
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(350, 300, 120, 40);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        btnDelete.setEnabled(false);
        dlgCustomer.add(btnDelete);
        
        btnBack=new JButton("Back");
        btnBack.setBounds(500, 300, 120, 40);
        btnBack.setActionCommand(ACT_BACK);
        btnBack.addActionListener(this);
        dlgCustomer.add(btnBack);
        
       
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgCustomer.setVisible(false);
            return;
	}
       
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(cs.checkDuplicateCustomerNumber(txt_Custnumber.getText())>0)
             {
                 JOptionPane.showMessageDialog(null, "This Customer's Id is Already Assigned... ");
                 return;
             }
            if(validFields()==true)//check if all fields are correctly filled
            {
               customer.setCustomerName(txt_Custname.getText());
               customer.setCustomerNumber(txt_Custnumber.getText());
               customer.setAddress(txt_custAddress.getText());
               customer.setContactPerson(txt_CustContact.getText());
               customer.setPhone(txt_CustPhone.getText());                
                try {
                    cs.saveCustomer(customer);                    
                   // btnSave.setEnabled(false);
                   // btnSave.setEnabled(false);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            if (oldCustomer.getCustomer_id()>0)
            {
              if(validFields()==true)
              {
                  int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to delete this Customer?","Warning",JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION)
                    {
//                        oldParticipant.deleteParticipant();
//                        oldParticipant.deleteAuditTrail();
                        return;
                    }
                  
              }
            }
        }
        else if(e.getActionCommand().equals(ACT_CANCEL))
        {
            if(btnCancel.getText()=="Update")
            {
                
                if (oldCustomer!=null)
                {
                    if(validFields()==true)//check if all fields are correctly filled
                    {
                        customer.setCustomerName(txt_Custname.getText());
                        customer.setCustomerNumber(txt_Custnumber.getText());
                        customer.setAddress(txt_custAddress.getText());
                        customer.setContactPerson(txt_CustContact.getText());
                        customer.setPhone(txt_CustPhone.getText()); 
                        customer.setCustomer_id(oldCustomer.getCustomer_id());
                        try {
                            cs.updateCustomer(customer);
                        } catch (SQLException ex) {
                            Logger.getLogger(CustomerDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "There is No Record to Update... ");
                }
                return;
                
            }
            else if("Cancel".equals(btnCancel.getText()))
            {
                txt_Custname.setText("");
                txt_Custnumber.setText("");                
                txt_custAddress.setText("");
                txt_CustPhone.setText("");
                txt_CustContact.setText("");
                btnCancel.setText("Update");
                btnSave.setEnabled(false);
                return;
                
            }        
        
        
        }
        
    }
    
    //Focus Listener Methods 
    @Override
    public void focusGained(FocusEvent e) {
        //displayMessage("Focus gained", e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        
        if (e.getSource()==txt_Custname)
        {
            txt_CustContact.setText(txt_Custname.getText());
        }
        
    }
    public boolean validFields()
    {
         
         if (txt_Custname.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Customer/Company's name Missing... ");
             return false;
         }
         if (txt_CustContact.getText()==null)
         {             
             JOptionPane.showMessageDialog(null, "Customer/Company's Contact Person Missing...");
             return false;
         }
         
         if (txt_CustPhone.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Customer/Company's Phone Number Missing...");
             return false;
         }
         
         if (txt_Custnumber.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Customer/Company's Id/Number Missing...");
             return false;
         }          
         
         return true;
     }
    public static void createAndShowGUI()
     {
         CustomerDialog customerDialog=new CustomerDialog();
         if(CustomerListTable.selectedCustomer!=null)
         {
             oldCustomer=new Customer();
             oldCustomer=CustomerListTable.selectedCustomer;
             txt_Custname.setText(oldCustomer.getCustomerName());
             txt_Custnumber.setText(oldCustomer.getCustomerNumber());
             txt_custAddress.setText(oldCustomer.getAddress());
             txt_CustPhone.setText(oldCustomer.getPhone());
             txt_CustContact.setText(oldCustomer.getContactPerson());            
             btnDelete.setEnabled(false);
             btnSave.setEnabled(false);             
         }
         else
         {
             btnDelete.setEnabled(false);
             btnCancel.setEnabled(false);
         }
        dlgCustomer.setVisible(true);           
        dlgCustomer.dispose(); //close the app once done
     }   
  

    
}
