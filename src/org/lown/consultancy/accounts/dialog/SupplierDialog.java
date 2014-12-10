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
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.api.SupplierService;
import org.lown.consultancy.accounts.tables.SupplierList;

/**
 *
 * @author LENOVO USER
 */
public class SupplierDialog extends JPanel implements ActionListener, FocusListener{
    
    //Action Listeners constants
    public static final String ACT_SAVE="save__update_supplier";
    public static final String ACT_SUPPLIER="supplier";
    public static final String ACT_DELETE="void_supplier";
    public static final String ACT_CANCEL="cancel";
    public static final String ACT_BACK="exit";    
    public static final String ACT_FIND="search_supplier";    
    public static final Font font = new Font("Times New Roman", Font.PLAIN, 16);
    
    //Text boxes and combo boxes
    public static JTextField txt_supplierName;
    public static JTextField txt_supplierNumber;
    public static JTextField txt_supplierAddress;
    public static JTextField txt_supplierPhone;
    public static JTextField txt_supplierContact;
    public static JTextField txt_pin;
   
    
    //Label controls
    private JLabel lbl_supplierName;
    private JLabel lbl_supplierNumber;
    private JLabel lbl_supplierAddress;
    private JLabel lbl_supplierPhone;   
    private JLabel lbl_supplierContact;    
    private JLabel lbl_title;
    private JLabel lbl_pin;
    
    
    //Command Buttons
    public static JButton btnSave;
    public static JButton btnDelete;
    public static JButton btnEnroll;
    private JButton btnBack;
    public static JButton btnCancel;
    
    
    //Display panel
    private static JDialog dlgSupplier;
    
    public static Supplier supplier; //the new customer
    public static Supplier oldSupplier; //currently selected customer
    public static SupplierService ss;
    
    public SupplierDialog()
    {
         //log info
        AccountsManagement.logger.info("Creating Register Supplier UI...");
       
        dlgSupplier= new JDialog((JDialog)null, "SUPPLIER DETAILS", true);
        dlgSupplier.setLayout(null);
        //dlgSupplier.setBounds(300, 50,700, 400);
        dlgSupplier.setSize(700, 400);
        dlgSupplier.setLocationRelativeTo(null); //center the dialog
        
        supplier = new Supplier();
        ss=new SupplierService();
               
        lbl_title=new JLabel();
        lbl_title.setBounds(200, 10, 400, 20);         
        lbl_title.setText("SUPPLIER REGISTRATION DETAILS");
        lbl_title.setFont(MainMenu.titleFont);         
        dlgSupplier.add(lbl_title);
        
        
        
        lbl_supplierNumber=new JLabel();
        lbl_supplierNumber.setBounds(50, 60, 200, 20);         
        lbl_supplierNumber.setText("Supplier Number/ID:");  
        lbl_supplierNumber.setFont(font);
        //lbl_Fname.setFont(font);
        dlgSupplier.add(lbl_supplierNumber);
        
        txt_supplierNumber=new JTextField();
        txt_supplierNumber.setBounds(240, 60, 200, 25);          
        txt_supplierNumber.setFont(font);
        dlgSupplier.add(txt_supplierNumber);
        
        lbl_supplierName=new JLabel();
        lbl_supplierName.setBounds(50, 100, 200, 20);         
        lbl_supplierName.setText("Supplier/Company Name:");
        lbl_supplierName.setFont(font);
        dlgSupplier.add(lbl_supplierName);
        
        txt_supplierName=new JTextField();
        txt_supplierName.setBounds(240, 100, 350, 25);          
        txt_supplierName.setFont(font);
        txt_supplierName.setActionCommand(ACT_SUPPLIER);
        txt_supplierName.addFocusListener(this);
        dlgSupplier.add(txt_supplierName);
        
        lbl_supplierAddress=new JLabel();
        lbl_supplierAddress.setBounds(50, 140, 200, 20);         
        lbl_supplierAddress.setText("Physical Address:");     
        lbl_supplierAddress.setFont(font);
        dlgSupplier.add(lbl_supplierAddress);
        
        txt_supplierAddress=new JTextField();
        txt_supplierAddress.setBounds(240, 140, 350, 25);          
        txt_supplierAddress.setFont(font);
        dlgSupplier.add(txt_supplierAddress);
        
        lbl_supplierPhone=new JLabel();
        lbl_supplierPhone.setBounds(50, 180, 200, 20);         
        lbl_supplierPhone.setText("Phone:");     
        lbl_supplierPhone.setFont(font);
        dlgSupplier.add(lbl_supplierPhone);
        
        txt_supplierPhone=new JTextField();
        txt_supplierPhone.setBounds(240, 180, 200, 25);          
        txt_supplierPhone.setFont(font);
        dlgSupplier.add(txt_supplierPhone);   
        
        
        lbl_supplierContact=new JLabel();
        lbl_supplierContact.setBounds(50, 220, 200, 20);         
        lbl_supplierContact.setText("Contact Person:"); 
        lbl_supplierContact.setFont(font);
        dlgSupplier.add(lbl_supplierContact);
        
        txt_supplierContact=new JTextField();
        txt_supplierContact.setBounds(240, 220, 350, 25);          
        txt_supplierContact.setFont(font);
        dlgSupplier.add(txt_supplierContact);
        
        lbl_pin=new JLabel();
        lbl_pin.setBounds(50, 260, 200, 20);         
        lbl_pin.setText("Pin Number:"); 
        lbl_pin.setFont(font);
        dlgSupplier.add(lbl_pin);
        
        txt_pin=new JTextField();
        txt_pin.setBounds(240, 260, 350, 25);          
        txt_pin.setFont(font);
        dlgSupplier.add(txt_pin);
        
       
        btnSave=new JButton("Save Record");
        btnSave.setBounds(50, 300, 120, 40);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        //btnSave.setEnabled(false);
        dlgSupplier.add(btnSave);
        
        btnCancel=new JButton("Update");
        btnCancel.setBounds(200, 300, 120, 40);
        btnCancel.setActionCommand(ACT_CANCEL);
        btnCancel.addActionListener(this);
        dlgSupplier.add(btnCancel);      
        
       // btnFind=new JButton("Find");
       // btnFind.setBounds(500, 220, 150, 40);
       // btnFind.setActionCommand(ACT_FIND);
       // btnFind.addActionListener(this);
       // dlgSupplier.add(btnFind);
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(350, 300, 120, 40);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        btnDelete.setEnabled(false);
        dlgSupplier.add(btnDelete);
        
        btnBack=new JButton("Back");
        btnBack.setBounds(500, 300, 120, 40);
        btnBack.setActionCommand(ACT_BACK);
        btnBack.addActionListener(this);
        dlgSupplier.add(btnBack);       
       
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgSupplier.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(ss.checkDuplicateSupplierNumber(txt_supplierNumber.getText())>0)
             {
                 JOptionPane.showMessageDialog(null, "This Customer's Id is Already Assigned... ");
                 return;
             }
            if(validFields()==true)//check if all fields are correctly filled
            {
               supplier.setSupplierName(txt_supplierName.getText());
               supplier.setSupplierNumber(txt_supplierNumber.getText());
               supplier.setAddress(txt_supplierAddress.getText());
               supplier.setContactPerson(txt_supplierContact.getText());
               supplier.setPhone(txt_supplierPhone.getText()); 
               supplier.setPin(txt_pin.getText()); 
                try {
                    ss.saveSupplier(supplier);                    
                   // btnSave.setEnabled(false);
                   // btnSave.setEnabled(false);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            if (oldSupplier.getSupplier_id()>0)
            {
              if(validFields()==true)
              {
                  int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to delete this Supplier?","Warning",JOptionPane.YES_NO_OPTION);
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
                
                if (oldSupplier!=null)
                {
                    if(validFields()==true)//check if all fields are correctly filled
                    {
                        supplier.setSupplierName(txt_supplierName.getText());
                        supplier.setSupplierNumber(txt_supplierNumber.getText());
                        supplier.setAddress(txt_supplierAddress.getText());
                        supplier.setContactPerson(txt_supplierContact.getText());
                        supplier.setPhone(txt_supplierPhone.getText()); 
                        supplier.setSupplier_id(oldSupplier.getSupplier_id());
                        supplier.setPin(txt_pin.getText());
                        try {
                            ss.updateSupplier(supplier);
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
                txt_supplierName.setText("");
                txt_supplierNumber.setText("");                
                txt_supplierAddress.setText("");
                txt_supplierPhone.setText("");
                txt_supplierContact.setText("");
                txt_pin.setText("");
                btnCancel.setText("Update");
                btnSave.setEnabled(false);
                return;                
            }        
        }
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        //displayMessage("Focus gained", e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        
        if (e.getSource()==txt_supplierName)
        {
            txt_supplierContact.setText(txt_supplierName.getText());
        }        
    }
    
    public static void createAndShowGUI()
     {
         SupplierDialog supplierDialog=new SupplierDialog();
         if(SupplierList.selectedSupplier!=null)
         {
             System.out.println("Suppulier Not Blank");
             oldSupplier=new Supplier();
             
             oldSupplier=SupplierList.selectedSupplier;
             txt_supplierName.setText(oldSupplier.getSupplierName());
             txt_supplierNumber.setText(oldSupplier.getSupplierNumber());
             txt_supplierAddress.setText(oldSupplier.getAddress());
             txt_supplierContact.setText(oldSupplier.getContactPerson());
             txt_supplierPhone.setText(oldSupplier.getPhone());
             txt_pin.setText(oldSupplier.getPin());    
             btnDelete.setEnabled(false);
             btnSave.setEnabled(false);             
         }
         else
         {
             btnDelete.setEnabled(false);
             btnCancel.setEnabled(false);
         }
         //SupplierDialog supplierDialog = new SupplierDialog(); 
          dlgSupplier.setVisible(true);           
          dlgSupplier.dispose(); //close the app once done
     }   
    public boolean validFields()
    {
         
         if (txt_supplierName.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Supplier/Company's name Missing... ");
             return false;
         }
         if (txt_supplierContact.getText()==null)
         {             
             JOptionPane.showMessageDialog(null, "Supplier/Company's Contact Person Missing...");
             return false;
         }
         
         if (txt_supplierPhone.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Supplier/Company's Phone Number Missing...");
             return false;
         }
         
         if (txt_supplierNumber.getText()==null)
         {            
             JOptionPane.showMessageDialog(null, "Supplier/Company's Id/Number Missing...");
             return false;
         }          
         
         return true;
     }
    
}
