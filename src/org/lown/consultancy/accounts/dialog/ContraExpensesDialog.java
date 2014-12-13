/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXDatePicker;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.ContraAccount;
import org.lown.consultancy.accounts.ContraExpenses;
import org.lown.consultancy.accounts.dao.CompanyService;
import org.lown.consultancy.accounts.tables.ContraAccountTable;
import org.lown.consultancy.accounts.tables.ContraExpensesTable;

/**
 *
 * @author LENOVO USER
 */
public class ContraExpensesDialog extends JPanel implements ActionListener{
    private static final String ACT_SAVE="add_save";    ;
    private static final String ACT_POST="save_Expense";       
    private static final String ACT_BACK="close";
    private static final String ACT_UPDATE="update";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font titleFont2 = new Font("Times New Roman", Font.PLAIN, 12);
    
    
    private static JDialog dlgContraExpenses;
    
    private JButton btnSave;
    private JButton btnAdd;    
    private JButton btnClose;
    private JButton btnUpdate;
    
    private JPanel pNewExpense;
    private JPanel pExpenses;
    
    private JLabel lbl_contraAccount;
    private JLabel lbl_description;
    private JLabel lbl_amount;
    private JLabel lbl_date;
    private JLabel lbl_availableFloat;
    
    private JTextField txt_availableFloat;
    private JTextField txt_description;
    private JTextField txt_amount;
    private JXDatePicker txDatePicker;
    
    private JComboBox cbo_contraAccount;
    
    private ContraExpensesTable contraTable;
    
    private TitledBorder titled2 = new TitledBorder("Add New Contra Expense");
    private TitledBorder titled1 = new TitledBorder("List of Contra Expenses");
    private ContraAccount account;
    private CompanyService cs;
    private List<ContraAccount> contraList;
    private List<ContraExpenses> expensesList;
    
    public ContraExpensesDialog()
    {
        cs=new CompanyService();
        contraList=cs.getAllContraAccounts();
        titled2.setTitleFont(titleFont);
        titled1.setTitleFont(titleFont);
        
        dlgContraExpenses= new JDialog((JDialog)null, "Manage Contra Expenses", true);
        dlgContraExpenses.setLayout(null);
        dlgContraExpenses.setSize(750, 500);//Width size, Height size
        dlgContraExpenses.setLocationRelativeTo(null);//center the invoice on the screen
        
        pNewExpense=new JPanel();
        pNewExpense.setBounds(20, 10, 690, 150);
        pNewExpense.setBorder(titled2);  
        pNewExpense.setLayout(null);
        dlgContraExpenses.add(pNewExpense);
        
        pExpenses=new JPanel();
        pExpenses.setBounds(20, 180, 690, 280);
        pExpenses.setBorder(titled1);  
        pExpenses.setLayout(null);
        dlgContraExpenses.add(pExpenses);
        
        lbl_availableFloat=new JLabel();
        lbl_availableFloat.setBounds(350, 20, 200, 25);         
        lbl_availableFloat.setText("Available Petty Cash (Kes):");
        lbl_availableFloat.setFont(titleFont);
        pNewExpense.add(lbl_availableFloat);
        
        txt_availableFloat=new JTextField();
        txt_availableFloat.setBounds(530, 20,150, 25);         
        txt_availableFloat.setText("");
        txt_availableFloat.setEditable(false);
        txt_availableFloat.setFont(titleFont); 
        txt_availableFloat.setHorizontalAlignment(JTextField.RIGHT);
        pNewExpense.add(txt_availableFloat); 
        
        
        lbl_date=new JLabel();
        lbl_date.setBounds(20, 20, 150, 25);         
        lbl_date.setText("Expense Date:");
        lbl_date.setFont(titleFont2);
        pNewExpense.add(lbl_date);
        
        txDatePicker=new JXDatePicker();
        txDatePicker.setDate(new Date());
        txDatePicker.setFormats(new String[] { "dd-MMM-yyyy" });
        txDatePicker.setBounds(150, 20, 130, 25);        
        pNewExpense.add(txDatePicker);
        
        lbl_description=new JLabel();
        lbl_description.setBounds(20, 60, 150, 25);         
        lbl_description.setText("Expense Description:");
        lbl_description.setFont(titleFont2);
        pNewExpense.add(lbl_description);
        
        txt_description=new JTextField();
        txt_description.setBounds(20, 90,150, 25);         
        txt_description.setText("");
        //txt_description.setEditable(false);
        txt_description.setFont(titleFont2);        
        pNewExpense.add(txt_description); 
        
        
        lbl_contraAccount=new JLabel();
        lbl_contraAccount.setBounds(200, 60, 150, 25);         
        lbl_contraAccount.setText("Contra Account: ");
        lbl_contraAccount.setFont(titleFont2);
        pNewExpense.add(lbl_contraAccount);
        
        cbo_contraAccount=new JComboBox();
        cbo_contraAccount.setBounds(200, 90, 200, 25);       
        cbo_contraAccount.setFont(titleFont2);        
        pNewExpense.add(cbo_contraAccount); 
        cbo_contraAccount.addItem("Select Account");
        for(ContraAccount c: contraList)
        {
            cbo_contraAccount.addItem(c.getContraName());
        }
        
        lbl_amount=new JLabel();
        lbl_amount.setBounds(430, 60, 150, 25);         
        lbl_amount.setText("Amount: ");
        lbl_amount.setFont(titleFont2);
        pNewExpense.add(lbl_amount);
        
        txt_amount=new JTextField();
        txt_amount.setBounds(430, 90, 100, 25);         
        txt_amount.setText("");
        txt_amount.setHorizontalAlignment(JTextField.RIGHT);
        txt_amount.setFont(titleFont2);        
        pNewExpense.add(txt_amount); 
        
        btnAdd=new JButton("Add Item");
        btnAdd.setBounds(570, 90, 100, 25);
        btnAdd.setActionCommand(ACT_SAVE);
        btnAdd.addActionListener(this);
        pNewExpense.add(btnAdd);
        
        btnUpdate=new JButton("Drop Item");
        btnUpdate.setBounds(570, 40, 100, 25);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        pExpenses.add(btnUpdate); 
        
         //Category List Table
        contraTable=new ContraExpensesTable();
        contraTable.setBounds(20,20,550, 250);
        pExpenses.add(contraTable);
        
        btnSave=new JButton("Save");
        btnSave.setBounds(570, 150, 100, 40);
        btnSave.setActionCommand(ACT_POST);
        btnSave.addActionListener(this);
        pExpenses.add(btnSave); 
        
        btnClose=new JButton("Close");
        btnClose.setBounds(570, 200, 100, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        pExpenses.add(btnClose); 
            
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgContraExpenses.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            contraTable.removeSelectedRow();
            return;
	}
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(cbo_contraAccount.getSelectedItem().equals("Select"))
            {
                JOptionPane.showMessageDialog(null, "Select the Contra Account first ... ");
                return;
            }
            
            if (txt_description.getText().isEmpty())
            {
               JOptionPane.showMessageDialog(null, "Enter The espense description first ... ");
               return; 
            }
            
            if (txt_amount.getText().isEmpty()&& !isNumeric(txt_amount.getText()))
            {
                JOptionPane.showMessageDialog(null, "Enter The espense Ammount first ... ");
                return; 
            }
            
            
            account=new ContraAccount();
            account=cs.getContraAccountByName(cbo_contraAccount.getSelectedItem().toString());
            
            double amount=0.0;
            if (!txt_amount.getText().isEmpty()&& isNumeric(txt_amount.getText()))
            {
                amount=Double.parseDouble(txt_amount.getText());
            }
            ContraExpenses expenseItem=new ContraExpenses();
            expenseItem.setAccount(account);
            expenseItem.setAmount(amount);
            expenseItem.setDescription(txt_description.getText());
            contraTable.insertRow(expenseItem);
            
            txt_description.setText("");
            txt_amount.setText("");
            cbo_contraAccount.setSelectedIndex(0);
        }
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
     public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Contra Expenses Dialog");
         ContraExpensesDialog contraDialog = new ContraExpensesDialog();      
         
         dlgContraExpenses.setVisible(true);          
         dlgContraExpenses.dispose(); //close the app once done
     }
}
