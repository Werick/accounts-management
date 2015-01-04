/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.lown.consultancy.accounts.Account;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.tables.AccountsTable;

/**
 *
 * @author LENOVO USER
 */
public class AccountDialog extends JPanel implements ActionListener{
    private static final String ACT_SAVE="save_add";
    private static final String ACT_UPDATE="update_cancel";
    private static final String ACT_DELETE="delete";
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_account";
    private static final String ACT_DISPLAY="display_account";
    private static final String ACT_HIDE="hide_account";
    
    private JLabel lbl_accountName;
    private JLabel lbl_accountNumber;
    private JLabel lbl_search;
    private JLabel lbl_bankName;
    private JLabel lbl_branch;    
    private JLabel lbl_openBalance;    
    private JLabel lbl_category;
    private JLabel lbl_title2;
    
    private static JTextField txt_accountName;
    private static JTextField txt_search;
    private static JTextField txt_accountNumber;
    private static JTextField txt_bankName;
    private static JTextField txt_branch;    
    private static JTextField txt_openBalance;
    
    private JComboBox cbo_category;
    
    private static JButton btnSave;
    private JButton btnUpdate;
    private JButton btnClose;
    private static JButton btnDelete;
    private JButton btnView;
    private JButton btnHide;
    private JButton btnDisplay;
    
    private static JDialog dlgAccount;
    private static Account account;
    
    private AccountsTable accountsTable;
    
    private JPanel pDetails;
    private JPanel pAccounts;
    
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 16);
    public static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
     //Border Titles
    private TitledBorder accountTitle = new TitledBorder("Account Details");
    private TitledBorder accountTitle2 = new TitledBorder("List of Accounts");
    
    private CompanyDAO cs;
   
    private String category[]={"Select","Cash","Bank","Mobile Money"};
    public AccountDialog()
    {
        AccountsManagement.logger.info("Creating Company Accounts UI...");        
        
        dlgAccount= new JDialog((JDialog)null, "Company Accounts", true);
        dlgAccount.setLayout(null);
        dlgAccount.setSize(650, 350);//Width size, Height size
        dlgAccount.setLocationRelativeTo(null);//center the invoice on the screen
        
        accountTitle.setTitleFont(title2Font);
         
        pDetails=new JPanel();
        pDetails.setBounds(20, 20, 600, 270);
        pDetails.setBorder(accountTitle);  
        pDetails.setLayout(null);
        dlgAccount.add( pDetails);       
        
        pAccounts=new JPanel();
        pAccounts.setBounds(10, 5, 600, 295);
        pAccounts.setBorder(accountTitle2);  
        pAccounts.setLayout(null);
        pAccounts.setVisible(false);        
        dlgAccount.add( pAccounts);
        
        //Category List Table
        accountsTable=new AccountsTable();
        accountsTable.setBounds(20,20,550, 225);
        pAccounts.add(accountsTable);
        accountsTable.insertRow();
        
        btnHide=new JButton("Hide");
        btnHide.setBounds(450, 260, 120, 25);
        btnHide.setActionCommand(ACT_HIDE);
        btnHide.addActionListener(this);
        btnHide.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pAccounts.add(btnHide);
        
        btnDisplay=new JButton("View Account");
        btnDisplay.setBounds(30, 260, 120, 25);
        btnDisplay.setActionCommand(ACT_DISPLAY);
        btnDisplay.addActionListener(this);
        btnDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pAccounts.add(btnDisplay);
        
        lbl_accountNumber=new JLabel();
        lbl_accountNumber.setBounds(10, 30, 150, 25);         
        lbl_accountNumber.setText("Account Number:");        
        pDetails.add(lbl_accountNumber);
        
        txt_accountNumber=new JTextField();
        txt_accountNumber.setBounds(150, 30, 150, 25);         
        txt_accountNumber.setText("");        
        pDetails.add(txt_accountNumber);
        
        lbl_accountName=new JLabel();
        lbl_accountName.setBounds(10, 65, 150, 25);         
        lbl_accountName.setText("Account Name:");        
        pDetails.add(lbl_accountName);
        
        txt_accountName=new JTextField();
        txt_accountName.setBounds(150, 65, 250, 25);         
        txt_accountName.setText("");        
        pDetails.add(txt_accountName);
        
        
        lbl_bankName=new JLabel();
        lbl_bankName.setBounds(10, 100, 150, 25);         
        lbl_bankName.setText("Bank Name:");        
        pDetails.add(lbl_bankName);
        
        txt_bankName=new JTextField();
        txt_bankName.setBounds(150, 100, 250, 25);         
        txt_bankName.setText("");        
        pDetails.add(txt_bankName);
        
        lbl_branch=new JLabel();
        lbl_branch.setBounds(10, 135, 150, 25);         
        lbl_branch.setText("Branch Name:");        
        pDetails.add(lbl_branch);
        
        txt_branch=new JTextField();
        txt_branch.setBounds(150, 135, 250, 25);         
        txt_branch.setText("");        
        pDetails.add(txt_branch);
        
        lbl_openBalance=new JLabel();
        lbl_openBalance.setBounds(10, 170, 150, 25);         
        lbl_openBalance.setText("Openning Balance:");        
        pDetails.add(lbl_openBalance);
        
        txt_openBalance=new JTextField();
        txt_openBalance.setBounds(150, 170, 250, 25);         
        txt_openBalance.setText("");        
        pDetails.add(txt_openBalance);
        
        lbl_category=new JLabel();
        lbl_category.setBounds(10, 205, 150, 25);         
        lbl_category.setText("Account Category:");        
        pDetails.add(lbl_category);
        
        cbo_category=new JComboBox(category);
        cbo_category.setBounds(150, 205, 250, 25);            
        pDetails.add(cbo_category);
        
        //buttons
        btnSave=new JButton("Add Account");
        btnSave.setBounds(450, 50, 120, 25);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pDetails.add(btnSave);
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(450, 90, 120, 25);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pDetails.add(btnUpdate);
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(450, 130, 120, 25);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this); 
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pDetails.add(btnDelete);
        
        btnView=new JButton("View Accounts");
        btnView.setBounds(450, 170, 120, 25);
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this);
        btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pDetails.add(btnView);   
        
        btnClose=new JButton("Back");
        btnClose.setBounds(450, 210, 120, 25);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        btnClose.setToolTipText("Click to go back to the Home/Main Menu Window");       
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pDetails.add(btnClose);        
    }
    
     @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgAccount.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            pDetails.setVisible(false);
            pAccounts.setVisible(true);
            return;
	}
        else if(e.getActionCommand().equals(ACT_DISPLAY))
        {
            if (AccountsTable.selectedAccount!=null)
            {
                pAccounts.setVisible(false);
                pDetails.setVisible(true);
                account=new Account();
                account=AccountsTable.selectedAccount;
                txt_accountName.setText(account.getAccount_name());
                txt_accountNumber.setText(account.getAccount_number());
                txt_bankName.setText(account.getBank_name());
                txt_branch.setText(account.getBranch());  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No Account is Selected...");
            }
           
            return;
	}
        else if(e.getActionCommand().equals(ACT_HIDE))
        {
            pAccounts.setVisible(false);
            pDetails.setVisible(true);
            return;
	}
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnSave.getText().equalsIgnoreCase("Add Account"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSave.setText("Save"); 
               btnUpdate.setText("Cancel");
               btnDelete.setEnabled(false);
               txt_accountName.setText("");
               txt_accountNumber.setText("");
               txt_branch.setText("");
               txt_bankName.setText("");
               txt_openBalance.setText("");
               cbo_category.setSelectedIndex(0);
               return;
            }
            else  if(btnSave.getText().equalsIgnoreCase("Save"))
            {
                if(validateInput())
                {
                    account=new Account();
                    Cash cashTx =new Cash();
                    cs=new CompanyDAO();
                    account.setAccount_number(txt_accountNumber.getText());
                    account.setAccount_name(txt_accountName.getText());
                    account.setBranch(txt_branch.getText());
                    account.setBank_name(txt_bankName.getText());
                    account.setCategory(cbo_category.getSelectedItem().toString());
                                        
                    if (isNumeric(txt_openBalance.getText()))
                    {
                        account.setOpen_balance(Double.parseDouble(txt_openBalance.getText()));
                        cashTx.setAmount(Double.parseDouble(txt_openBalance.getText()));
                    }   
                    cashTx.setTxType("DR");
                    cashTx.setDate(new java.util.Date());               
                    cashTx.setAccount(cbo_category.getSelectedItem().toString());
                    
                    try {
                        cs.saveAccount(account,cashTx);
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    btnSave.setText("Add Account"); 
                    btnUpdate.setText("Update");
                    btnDelete.setEnabled(true);
                }
            }
	}        
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            if(btnUpdate.getText().equalsIgnoreCase("Cancel"))
            {
               //clear the text boxes to allow addtion of new account record
               btnSave.setText("Add Account"); 
               btnUpdate.setText("Update");
               btnDelete.setEnabled(true);
               txt_accountName.setText("");
               txt_accountNumber.setText("");
               txt_branch.setText("");
               txt_bankName.setText("");
               txt_openBalance.setText("");
               cbo_category.setSelectedIndex(0);
               return;
            }
            else  if(btnUpdate.getText().equalsIgnoreCase("Update"))
            {
                if (validateInput())
                {
                    if (account!=null)
                    {
                        Cash cashTx=new Cash();
                        account=new Account();
                        cs=new CompanyDAO();
                        account.setAccount_number(txt_accountNumber.getText());
                        account.setAccount_name(txt_accountName.getText());
                        account.setBranch(txt_branch.getText());
                        account.setBank_name(txt_bankName.getText());
                        account.setCategory(cbo_category.getSelectedItem().toString());
                                        
                        if (isNumeric(txt_openBalance.getText()))
                        {
                            account.setOpen_balance(Double.parseDouble(txt_openBalance.getText()));
                            cashTx.setAmount(Double.parseDouble(txt_openBalance.getText()));
                        }  
                        
                        
                        
                        
                        cashTx.setTxType("DR");
                        cashTx.setDate(new java.util.Date());
                        try {
                             cs.updateAccount(account);                            
                        } catch (SQLException ex) {
                        Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            return; 
        }   
    }
     
     public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Company Accounts Dialog");
         AccountDialog accountDialog = new AccountDialog();      
//         if (ProductListTable.selectedProduct!=null)
//            {
//                System.out.println("Testing Selected view product..");
//                product=new Product();
//                product=ProductListTable.selectedProduct;
//                txt_productName.setText(product.getProductName());
//                txt_productCode.setText(product.getProductCode());
//                txt_measureUnit.setText(product.getMeasureUnit());
//                txt_sellingPrice.setText(Double.toString(productService.getProductSellingPrice(product.getProduct_id())));
//                txt_buyingPrice.setText(Double.toString(productService.getProductBuyingPrice(product.getProduct_id())));
//                Category selCategory=productCategoryMap.get(product.getCategory_id());
//                cbo_productCategory.setSelectedItem(selCategory.getCategoryCode());
//                btnSave.setEnabled(false);
//            }
//            else
//            {
//                btnDelete.setEnabled(false);
//                
//            }
         dlgAccount.setVisible(true);          
         dlgAccount.dispose(); //close the app once done
     }
     
     private boolean validateInput()
     {
          if (txt_accountNumber.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Account Number Missing...");
              return false;                    
          }
          if (txt_accountName.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Account Name Missing...");
              return false;                    
          }
                
           if (cbo_category.getSelectedIndex()==0)
          {
              JOptionPane.showMessageDialog(null, "Account Category not Specified...");
              return false;                    
          }
          
          if (txt_openBalance.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Account Opening Balance is Missing...");
              return false;                    
          }
                
         return true;
     }
     
    private boolean isNumeric(String str)  
    {  
        try 
            {  
                double d = Double.parseDouble(str);  
            }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
    }
    
}
