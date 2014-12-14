/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.ContraAccount;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.tables.CategoryListTable;
import org.lown.consultancy.accounts.tables.ContraAccountTable;

/**
 *
 * @author LENOVO USER
 */
public class ContraAccountsDialog extends JPanel implements ActionListener{
    
    private static final String ACT_SAVE="add_save";    ;
    private static final String ACT_VIEW="view_account";       
    private static final String ACT_BACK="close";
    private static final String ACT_UPDATE="update";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font titleFont2 = new Font("Times New Roman", Font.PLAIN, 12);
    
    private static JDialog dlgContra;
    
    private JButton btnView;
    private JButton btnSave;    
    private JButton btnClose;
    private JButton btnUpdate;
    
    private JPanel pNewAccount;
    private JPanel pAccounts;
    
    private JLabel lbl_name;
    private JLabel lbl_description;
    
    private JTextField txt_name;
    private JTextField txt_description;
    private ContraAccountTable contraTable;
    
    private TitledBorder titled2 = new TitledBorder("View/Add New Contra Account");
    private TitledBorder titled1 = new TitledBorder("List of Contra Accounts");
    private ContraAccount account;
    private CompanyDAO cs;
    
    public ContraAccountsDialog()
    {
        cs=new CompanyDAO();
        titled2.setTitleFont(titleFont);
        titled1.setTitleFont(titleFont);
        
        dlgContra= new JDialog((JDialog)null, "Manage Contra Accounts", true);
        dlgContra.setLayout(null);
        dlgContra.setSize(750, 500);//Width size, Height size
        dlgContra.setLocationRelativeTo(null);//center the invoice on the screen
        
        pNewAccount=new JPanel();
        pNewAccount.setBounds(20, 20, 690, 150);
        pNewAccount.setBorder(titled2);  
        pNewAccount.setLayout(null);
        dlgContra.add(pNewAccount);
        
        pAccounts=new JPanel();
        pAccounts.setBounds(20, 180, 690, 280);
        pAccounts.setBorder(titled1);  
        pAccounts.setLayout(null);
        dlgContra.add(pAccounts);
        
        
        lbl_name=new JLabel();
        lbl_name.setBounds(20, 30, 200, 25);         
        lbl_name.setText("Contra Account Name : ");
        lbl_name.setFont(titleFont2);
        pNewAccount.add(lbl_name);
        
        txt_name=new JTextField();
        txt_name.setBounds(200, 30, 300, 25);         
        txt_name.setText("");
        //txt_name.setEditable(false);
        txt_name.setFont(titleFont2);        
        pNewAccount.add(txt_name); 
        
        lbl_description=new JLabel();
        lbl_description.setBounds(20, 80, 200, 25);         
        lbl_description.setText("Contra Description : ");
        lbl_description.setFont(titleFont2);
        pNewAccount.add(lbl_description);
        
        txt_description=new JTextField();
        txt_description.setBounds(200, 80,300, 25);         
        txt_description.setText("");
        //txt_description.setEditable(false);
        txt_description.setFont(titleFont2);        
        pNewAccount.add(txt_description); 
        
        btnSave=new JButton("Add New");
        btnSave.setBounds(550, 30, 100, 25);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        pNewAccount.add(btnSave);
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(550, 80, 100, 25);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        pNewAccount.add(btnUpdate); 
        
        
         //Category List Table
        contraTable=new ContraAccountTable();
        contraTable.setBounds(20,20,550, 250);
        pAccounts.add(contraTable);
        contraTable.insertRow();       
               
        btnView=new JButton("View");
        btnView.setBounds(580, 50, 100, 30);
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this);
        pAccounts.add(btnView);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(580, 100, 100, 30);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        pAccounts.add(btnClose);
        
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgContra.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            if (ContraAccountTable.selectedAccount!=null)
            {
                txt_name.setText(ContraAccountTable.selectedAccount.getContraName());
                txt_description.setText(ContraAccountTable.selectedAccount.getContraDescription());
            }
        }
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnSave.getText().equalsIgnoreCase("Add New"))
            {
                txt_name.setText("");
                txt_description.setText("");
                btnSave.setText("Save");
                btnUpdate.setText("Cancel");
                btnView.setEnabled(false);
                return;                
            }
            else if(btnSave.getText().equalsIgnoreCase("Save"))
            {
                
                if(txt_name.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the Contra Account name first...");
                    return;
                }
                if(txt_description.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the Contra Account description first...");
                    return;
                }
                
                account=new ContraAccount();
                account.setContraName(txt_name.getText());
                account.setContraDescription(txt_description.getText());
                try {
                    
                    cs.saveContraAccount(account);
                    contraTable.insertRow(account);
                   
                    btnSave.setText("Add New");
                    btnUpdate.setText("Update");
                    btnView.setEnabled(true);
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(ContraAccountsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            if(btnUpdate.getText().equalsIgnoreCase("Cancel"))
            {
                txt_name.setText("");
                txt_description.setText("");
                btnSave.setText("Add New");
                btnUpdate.setText("Update");
                return;                
            }
            else if(btnUpdate.getText().equalsIgnoreCase("Update"))
            {
                
                if(txt_name.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the Contra Account name first...");
                    return;
                }
                if(txt_description.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Enter the Contra Account description first...");
                    return;
                }
                
                account=new ContraAccount();
                account.setContraName(txt_name.getText());
                account.setContraDescription(txt_description.getText());
                account.setContra_id(ContraAccountTable.selectedAccount.getContra_id());
                
                try {
                    cs.updateContraAccount(account);
                } catch (SQLException ex) {
                    Logger.getLogger(ContraAccountsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                //btnSave.setText("Add New");
                btnUpdate.setText("Update");
                return;
            }
        }
    }
    
    public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Contra Accounts Dialog");
         ContraAccountsDialog contraDialog = new ContraAccountsDialog();      
         
         dlgContra.setVisible(true);          
         dlgContra.dispose(); //close the app once done
     }
}
