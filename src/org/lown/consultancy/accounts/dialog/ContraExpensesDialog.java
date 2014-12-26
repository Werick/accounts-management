/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
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
import org.jdesktop.swingx.JXDatePicker;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.ContraAccount;
import org.lown.consultancy.accounts.ContraExpenses;
import org.lown.consultancy.accounts.ReportDescriptor;
import org.lown.consultancy.accounts.dao.CashDAO;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.dao.ReportsDAO;
import org.lown.consultancy.accounts.tables.ContraExpensesTable;
import org.lown.consultancy.accounts.tables.ExpensesList;

/**
 *
 * @author LENOVO USER
 */
public class ContraExpensesDialog extends JPanel implements ActionListener{
    private static final String ACT_SAVE="add_save";
    private static final String ACT_VIEW="view";
    private static final String ACT_HIDE="hide";
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
    private JButton btnView;
    private JButton btnHide;
    
    private JPanel pNewExpense;
    private JPanel pExpenses;
    private JPanel pExpensesList;
    
    private JLabel lbl_contraAccount;
    private JLabel lbl_description;
    private JLabel lbl_amount;
    private JLabel lbl_date;
    private JLabel lbl_availableFloat;
    private JLabel lbl_total;
    
    private JTextField txt_total;
    private JTextField txt_availableFloat;
    private JTextField txt_description;
    private JTextField txt_amount;
    private JXDatePicker txDatePicker;
    
    private JComboBox cbo_contraAccount;
    
    private ContraExpensesTable contraTable;
    private ExpensesList contraTable2;
    
    private TitledBorder titled2 = new TitledBorder("Add New Contra Expense");
    private TitledBorder titled1 = new TitledBorder("List of Contra Expenses");
    private ContraAccount account;
    private CompanyDAO companyDAO;
    private CashDAO cashDAO;
    private List<ContraAccount> contraList;
    private List<ContraExpenses> expensesList;
    private double total;
    private double availBalance;
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    public ContraExpensesDialog()
    {
        companyDAO=new CompanyDAO();
        cashDAO=new CashDAO();
        total=0.0;
        availBalance=cashDAO.getAccountBalanceById(6);//this section of code will need a better approach to load the available cash in petty cashbook
        contraList=companyDAO.getAllContraAccounts();
        titled2.setTitleFont(titleFont);
        titled1.setTitleFont(titleFont);
        
        dlgContraExpenses= new JDialog((JDialog)null, "Manage Contra Expenses", true);
        dlgContraExpenses.setLayout(null);
        dlgContraExpenses.setSize(750, 500);//Width size, Height size
        dlgContraExpenses.setLocationRelativeTo(null);//center the invoice on the screen
        
        pNewExpense=new JPanel();
        pNewExpense.setBounds(20, 10, 690, 130);
        pNewExpense.setBorder(titled2);  
        pNewExpense.setLayout(null);
        dlgContraExpenses.add(pNewExpense);
        
        pExpenses=new JPanel();
        pExpenses.setBounds(20, 140, 690, 320);
        pExpenses.setBorder(titled1);  
        pExpenses.setLayout(null);
        dlgContraExpenses.add(pExpenses);
        
        pExpensesList=new JPanel();
        pExpensesList.setBounds(20, 140, 690, 320);
        pExpensesList.setBorder(titled1);  
        pExpensesList.setLayout(null);
        pExpensesList.setVisible(false);
        dlgContraExpenses.add(pExpensesList);
        
        
        lbl_availableFloat=new JLabel();
        lbl_availableFloat.setBounds(350, 20, 200, 25);         
        lbl_availableFloat.setText("Available Petty Cash (Kes):");
        lbl_availableFloat.setFont(titleFont);
        pNewExpense.add(lbl_availableFloat);
        
        txt_availableFloat=new JTextField();
        txt_availableFloat.setBounds(530, 20,150, 25);         
        txt_availableFloat.setText(df.format(availBalance));
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
        
         //Contra Expenses List Table
        contraTable=new ContraExpensesTable();
        contraTable.setBounds(20,20,550, 250);
        pExpenses.add(contraTable);
        
        //Contra Expenses List Table
        contraTable2=new ExpensesList();
        contraTable2.setBounds(20,65,550, 250);
        pExpensesList.add(contraTable2);
        contraTable2.insertRow();
        
       
        
        btnSave=new JButton("Save");
        btnSave.setBounds(570, 100, 100, 40);
        btnSave.setActionCommand(ACT_POST);
        btnSave.addActionListener(this);
        pExpenses.add(btnSave); 
        
        btnView=new JButton("View");
        btnView.setBounds(570, 150, 100, 40);
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this);
        pExpenses.add(btnView); 
        
        btnClose=new JButton("Close");
        btnClose.setBounds(570, 200, 100, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        pExpenses.add(btnClose); 
        
        btnHide=new JButton("Hide");
        btnHide.setBounds(570, 200, 100, 40);
        btnHide.setActionCommand(ACT_HIDE);
        btnHide.addActionListener(this);
        pExpensesList.add(btnHide);
        
        lbl_total=new JLabel();
        lbl_total.setBounds(210, 270, 200, 25);         
        lbl_total.setText("Total Posted (Kes):");
        lbl_total.setFont(titleFont);
        pExpenses.add(lbl_total);
        
        txt_total=new JTextField();
        txt_total.setBounds(390, 270,150, 25);         
        txt_total.setText(df.format(total));
        txt_total.setEditable(false);
        txt_total.setFont(titleFont); 
        txt_total.setHorizontalAlignment(JTextField.RIGHT);
        pExpenses.add(txt_total); 
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgContraExpenses.setVisible(false);
            return;
	}
         else if(e.getActionCommand().equals(ACT_HIDE))
        {
            pExpensesList.setVisible(false);
            pExpenses.setVisible(true);
            btnAdd.setEnabled(true);
        }
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            pExpensesList.setVisible(true);
            pExpenses.setVisible(false);
            btnAdd.setEnabled(false);
            
            //Testing Reports
            System.out.println("Testing Reports Output");
            ReportsDAO report=new ReportsDAO();
            List<ReportDescriptor> demoReport=report.getPettyCashReport();
            for(ReportDescriptor r:demoReport)
            {
                System.out.println(r.getTransactionDate()+"\t"+r.getDescription()+"\t"+r.getDrAmount()+"\t"+r.getCrAmount()+"\t"+r.getBalance());
            }
                
            
        }
        else if(e.getActionCommand().equals(ACT_POST))
        {
            if(txDatePicker.getDate().compareTo(new Date())>0)
            {
                JOptionPane.showMessageDialog(null, "Expenditure Date cannot be a future date...");
                return;
            }
            
            if(total>availBalance)
            {
               JOptionPane.showMessageDialog(null, "Sorry, You don't have enough Balance to settle all the Listed Expenses...");
                return; 
            }
            
            expensesList=contraTable.getExpensesList();
            if(!expensesList.isEmpty())
            {
                //update list before posting
                for(ContraExpenses c: expensesList)
                {
                    c.setExpenseDate(txDatePicker.getDate());
                    
                    System.out.println("Date: "+c.getExpenseDate()+"  Descr: "+c.getDescription()+"  Amount: "+c.getAmount()+"  Account:"+c.getAccount().getContraName());
                }
                //create the petty cash CR object
                Cash pettyCash=new Cash();
                pettyCash.setAccount("Petty Cash");//we need to a better way of specifying the account name
                pettyCash.setAmount(total);
                pettyCash.setDate(txDatePicker.getDate());
                pettyCash.setTxType("CR");
                pettyCash.setTxCode(19); //still not able to automatically load the transaction codes
                try {
                    cashDAO.postPettyCashExpense(pettyCash, expensesList);
                    //close the dialog after posting expenses
                    dlgContraExpenses.setVisible(false);
                } catch (SQLException ex) {
                    Logger.getLogger(ContraExpensesDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Sorry!!! No Expenses to be Posted...");
                return;
            }
        }
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            total=total-ContraExpensesTable.selectedAmount;
            txt_total.setText(df.format(total));
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
            account=companyDAO.getContraAccountByName(cbo_contraAccount.getSelectedItem().toString());
            
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
            total=total+amount;
            txt_total.setText(df.format(total));
            
            
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
