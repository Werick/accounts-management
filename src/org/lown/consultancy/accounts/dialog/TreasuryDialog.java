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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.lown.consultancy.accounts.Account;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.CashTransfer;
import org.lown.consultancy.accounts.dao.CashDAO;
import org.lown.consultancy.accounts.dao.CompanyDAO;

/**
 *
 * @author LENOVO USER
 */
public class TreasuryDialog extends JPanel implements ActionListener{
    
    private static final String ACT_BANK="manage_Bank";    ;
    private static final String ACT_SOURCE="source_account";  
    private static final String ACT_PETTYCASH="petty_account";  
    private static final String ACT_CONTRA="contra_accounts";  
    private static final String ACT_BACK="close";
    private static final String ACT_TRANSFER="transfer_money";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    
    private String payOptions[]={"Select","Cash Collection","Bank/Cheque Collection","Mobile Money"};
    private static JDialog dlgTreasury;
    
    private JButton btnBank;
    private JButton btnContra;
    private JButton btnExpenses;
    private JButton btnTransfer;
    private JButton btnClose;
    private JButton btnUpdate;
    
    private JPanel pTransfers;
    
    private JLabel lbl_transferDate;
    private JLabel lbl_source;
    private JLabel lbl_destination;
    private JLabel lbl_description;
    private JLabel lbl_transferAmount;
    private JLabel lbl_availableAmount;
    
    private JXDatePicker dp_transferDate;
    private JTextField txt_description;
    private JTextField txt_transferAmount;
    private JTextField txt_availableAmount;
    
    private JComboBox cbo_source;
    private JComboBox cbo_destination;
    private CashDAO cashDAO;
    private CompanyDAO companyService;
    
    
    private TitledBorder titled2 = new TitledBorder("Cash Transfer");
    private double availBalance;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private static Map<String, Integer> accountMap;
    private List<Account> accountList;
    private CashTransfer sourceAcc;
    private CashTransfer destinationAcc;
    
    public TreasuryDialog()
    {
        accountMap=new HashMap<String,Integer>();
        accountList=new ArrayList<Account>();
        
        companyService=new CompanyDAO();
        accountList=companyService.getAllAccounts();
        
        dlgTreasury= new JDialog((JDialog)null, "Manage Treasury", true);
        dlgTreasury.setLayout(null);
        dlgTreasury.setSize(750, 500);//Width size, Height size
        dlgTreasury.setLocationRelativeTo(null);//center the invoice on the screen
        
        pTransfers=new JPanel();
        pTransfers.setBounds(200, 30, 500, 400);
        pTransfers.setBorder(titled2);  
        pTransfers.setLayout(null);
        dlgTreasury.add(pTransfers);
        
        lbl_transferDate=new JLabel();
        lbl_transferDate.setBounds(20, 50, 200, 25);         
        lbl_transferDate.setText("Transfer Date: ");
        lbl_transferDate.setFont(titleFont);
        pTransfers.add(lbl_transferDate);
        
        dp_transferDate=new JXDatePicker();
        dp_transferDate.setDate(new Date());
        dp_transferDate.setFormats(new String[] { "dd-MMM-yyyy" });
        dp_transferDate.setBounds(250, 50, 150, 25);
        pTransfers.add(dp_transferDate);
        
        lbl_source=new JLabel();
        lbl_source.setBounds(20, 100, 200, 25);         
        lbl_source.setText("Source Account: ");
        lbl_source.setFont(titleFont);
        pTransfers.add(lbl_source);
        
        cbo_source=new JComboBox(payOptions);
        cbo_source.setBounds(250, 100, 220, 25);
        cbo_source.setFont(titleFont);
        cbo_source.setActionCommand(ACT_SOURCE);
        cbo_source.addActionListener(this); 
        pTransfers.add(cbo_source);
        
        for (Account acc:accountList)
        {
            if(acc.getBank_name().equalsIgnoreCase("Cash")) //skip cash account
            {
                continue;
            }
            cbo_source.addItem(acc.getAccount_name()+" - "+ acc.getBank_name()+" - "+acc.getBranch());
            String accName=acc.getAccount_name()+" - "+ acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
            accountMap.put(accName, acc.getAccount_id());
        }
        
        lbl_availableAmount=new JLabel();
        lbl_availableAmount.setBounds(20, 150, 220, 25);         
        lbl_availableAmount.setText("Available Amount (Kes) : ");
        lbl_availableAmount.setFont(titleFont);
        pTransfers.add(lbl_availableAmount);
        
        txt_availableAmount=new JTextField();
        txt_availableAmount.setBounds(250, 150, 200, 25);         
        txt_availableAmount.setText("");
        txt_availableAmount.setEditable(false);
        txt_availableAmount.setFont(titleFont);
        txt_availableAmount.setHorizontalAlignment(JTextField.RIGHT);
        pTransfers.add(txt_availableAmount);        
           
        lbl_destination=new JLabel();
        lbl_destination.setBounds(20, 200, 200, 25);         
        lbl_destination.setText("Receiving/Desitnation Account: ");
        lbl_destination.setFont(titleFont);
        pTransfers.add(lbl_destination);
        
        cbo_destination=new JComboBox(payOptions);
        cbo_destination.setBounds(250, 200, 220, 25);
        cbo_destination.setFont(titleFont);
        pTransfers.add(cbo_destination);
        
        for (Account acc:accountList)
        {
            if(acc.getBank_name().equalsIgnoreCase("Cash"))
            {
                continue;
            }
            cbo_destination.addItem(acc.getAccount_name()+" - "+ acc.getBank_name()+" - "+acc.getBranch());            
        }
        
        
        lbl_transferAmount=new JLabel();
        lbl_transferAmount.setBounds(20, 250, 200, 25);         
        lbl_transferAmount.setText("Transfer Amount (Kes) : ");
        lbl_transferAmount.setFont(titleFont);
        pTransfers.add(lbl_transferAmount);
        
        txt_transferAmount=new JTextField();
        txt_transferAmount.setBounds(250, 250, 200, 25);         
        txt_transferAmount.setText("");
        txt_transferAmount.setFont(titleFont);
        txt_transferAmount.setHorizontalAlignment(JTextField.RIGHT);
        pTransfers.add(txt_transferAmount);
        
        lbl_description=new JLabel();
        lbl_description.setBounds(20, 300, 200, 25);         
        lbl_description.setText("Description : ");
        lbl_description.setFont(titleFont);
        pTransfers.add(lbl_description);
        
        txt_description=new JTextField();
        txt_description.setBounds(250, 300, 200, 25);         
        txt_description.setText("");
        txt_description.setFont(titleFont);
        //txt_description.setHorizontalAlignment(JTextField.RIGHT);
        pTransfers.add(txt_description);
        
        btnUpdate=new JButton("Start Transfer");
        btnUpdate.setBounds(150, 350, 150, 40);
        btnUpdate.setActionCommand(ACT_TRANSFER);
        btnUpdate.addActionListener(this);
        pTransfers.add(btnUpdate); 
        
        
        btnBank=new JButton("Bank Accounts");
        btnBank.setBounds(20, 50, 150, 50);
        btnBank.setActionCommand(ACT_BANK);
        btnBank.addActionListener(this);
        dlgTreasury.add(btnBank);    
        
        btnTransfer=new JButton("Reconcile Accounts");
        btnTransfer.setBounds(20, 120, 150, 50);
        btnTransfer.setActionCommand(ACT_TRANSFER);
        btnTransfer.addActionListener(this);
        dlgTreasury.add(btnTransfer); 
        
        btnContra=new JButton("Contra Accounts");
        btnContra.setBounds(20, 190, 150, 50);
        btnContra.setActionCommand(ACT_CONTRA);
        btnContra.addActionListener(this);
        dlgTreasury.add(btnContra); 
        
        btnExpenses=new JButton("Petty Cash");
        btnExpenses.setBounds(20, 260, 150, 50);
        btnExpenses.setActionCommand(ACT_PETTYCASH);
        btnExpenses.addActionListener(this);
        dlgTreasury.add(btnExpenses); 
        
        btnClose=new JButton("Back");
        btnClose.setBounds(20, 330, 150, 50);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgTreasury.add(btnClose);
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgTreasury.setVisible(false);
            return;
	}
        else  if(e.getActionCommand().equals(ACT_PETTYCASH))
        {
            ContraExpensesDialog.createAndShowGUI(); 
            return;
	}
        else  if(e.getActionCommand().equals(ACT_BANK))
        {
            AccountDialog.createAndShowGUI(); 
            return;
	}
        else  if(e.getActionCommand().equals(ACT_CONTRA))
        {
            ContraAccountsDialog.createAndShowGUI(); 
            return;
	}
        else if(e.getActionCommand().equals(ACT_SOURCE))
        {
            System.out.println("Testing Source Accounts........");
            txt_availableAmount.setText("");
            if (cbo_source.getSelectedIndex()==1)
            {
                txt_availableAmount.setText("");
                cashDAO=new CashDAO();
                availBalance=cashDAO.getAvailableCashCollection();
                txt_availableAmount.setText(df.format(availBalance));
                
               
            }
            else if (cbo_source.getSelectedIndex()==2)
            {
                txt_availableAmount.setText("");
                cashDAO=new CashDAO();
                availBalance=cashDAO.getAvailableBankCollection();
                txt_availableAmount.setText(df.format(availBalance));                
            }
            else if (cbo_source.getSelectedIndex()==3)
            {
                txt_availableAmount.setText("");
                cashDAO=new CashDAO();
                availBalance=cashDAO.getAvailableMobileMoney();
                txt_availableAmount.setText(df.format(availBalance));              
            }
             else if (cbo_source.getSelectedIndex()>3)
             {
                 //check the indivual accounts using the accounts map
                 
                int selAccount=-1;
                System.out.println("Testing Account Map........");
                if(!accountMap.isEmpty())
                {
                    if (cbo_source.getItemCount()>3)
                    {
                       selAccount=accountMap.get(cbo_source.getSelectedItem());
                       txt_availableAmount.setText("");
                       cashDAO=new CashDAO();
                       availBalance=cashDAO.getAccountBalanceById(selAccount);
                       txt_availableAmount.setText(df.format(availBalance));                     
                    }  

                } 

             }          
	}
        else if(e.getActionCommand().equals(ACT_TRANSFER))
        {
            
            if (btnUpdate.getText().equalsIgnoreCase("Start Transfer"))
            {
                txt_transferAmount.setText("");
                cbo_source.setSelectedIndex(0);
                cbo_destination.setSelectedIndex(0);
                btnUpdate.setText("Save Transfer");
                return;
            }
            else if (btnUpdate.getText().equalsIgnoreCase("Save Transfer"))
            {
                if(cbo_source.getSelectedIndex()==0)
                {
                    JOptionPane.showMessageDialog(null, "Select the Source Account First... "); 
                    return;
                }

                if(cbo_destination.getSelectedIndex()==0)
                {
                    JOptionPane.showMessageDialog(null, "Select the Destination Account First... "); 
                    return;
                }

                if (!isNumeric(txt_transferAmount.getText())&& (txt_transferAmount.getText().isEmpty()))
                {
                    JOptionPane.showMessageDialog(null, "Enter the amount to be transferred first... "); 
                    return;
                }

                if (isNumeric(txt_transferAmount.getText())&& (isNumeric(txt_availableAmount.getText())))
                {
                    if(Double.parseDouble(txt_transferAmount.getText())>Double.parseDouble(txt_availableAmount.getText()))
                    {
                        JOptionPane.showMessageDialog(null, "You don't have enough Cash to Transfer to the specified account... "); 
                        return;
                    }

                    
                }

                if(cbo_destination.getSelectedIndex()<=3)
                {
                    JOptionPane.showMessageDialog(null, "Money Cannot be transferred to another collection Account... "); 
                    return;
                }

                if(cbo_source.getSelectedIndex()==cbo_destination.getSelectedIndex())
                {
                    JOptionPane.showMessageDialog(null, "The Source Account and Destination Account should not be the same... "); 
                    return;
                }

                System.out.println("Not Getting Here");
                //get the source account details
                //tranfer from cash/bank/mobile money collection accounts
                destinationAcc=new CashTransfer();
                destinationAcc.setTransactionType("DR");
                destinationAcc.setTransactionCode(1); //deposit
                destinationAcc.setTransferDate(dp_transferDate.getDate());
                destinationAcc.setDescription(txt_description.getText());

                //
                sourceAcc=new CashTransfer();
                sourceAcc.setTransactionType("CR");
                sourceAcc.setTransactionCode(7); //withdraw cash from account
                sourceAcc.setTransferDate(dp_transferDate.getDate());
                sourceAcc.setDescription(txt_description.getText());
                int selAccount=1; //assume cash account is selected
                Account source=new Account();
                Account destination=new Account();

                if(cbo_source.getSelectedIndex()==1)//cash collection account
                {
                    source.setAccount_name("Cash");
                    source.setAccount_id(1);
                }
                else if(cbo_source.getSelectedIndex()==2)//cheque collection account
                {
                    source.setAccount_name("Bank");
                    source.setAccount_id(0);
                }
                else if(cbo_source.getSelectedIndex()==3)//Mobile money collection account
                {
                    source.setAccount_name("Mobile Money");
                    source.setAccount_id(0);
                }
                else if(cbo_source.getSelectedIndex()>3)//Other Accounts
                {
                    selAccount=1; //assume cash account is selected
                    selAccount=accountMap.get(cbo_source.getSelectedItem());
                    source=companyService.getAccountById(selAccount);
                }

                if (isNumeric(txt_transferAmount.getText()))
                {
                    sourceAcc.setAmount(Double.parseDouble(txt_transferAmount.getText()));
                    destinationAcc.setAmount(Double.parseDouble(txt_transferAmount.getText()));
                }
                sourceAcc.setAccount(source);
                //Get the destination account details
                if(cbo_destination.getSelectedIndex()>3)//Other Accounts
                {
                    selAccount=1; //assume cash account is selected
                    selAccount=accountMap.get(cbo_destination.getSelectedItem());
                    destination=companyService.getAccountById(selAccount);
                }

                destinationAcc.setAccount(destination);
                //due deligence b4 posting transfer
                if(source.getAccount_id()==destination.getAccount_id())
                {
                    JOptionPane.showMessageDialog(null, "The Source Account and Destination Account should not be the same... "); 
                    return;
                }



                System.out.println("Testing Simple cash Transfer");
                System.out.println("Testing Source Account:");
                System.out.println("Source Amount: "+ sourceAcc.getAmount());
                System.out.println("Source Account: "+ sourceAcc.getAccount().getAccount_name());

                System.out.println("Testing Destination Account:");
                System.out.println("Destination Amount: "+ destinationAcc.getAmount());
                System.out.println("Destination Account: "+ destinationAcc.getAccount().getAccount_name());

                //Transfer Funds
                try
                {
                    cashDAO=new CashDAO();
                    cashDAO.transferMoney(sourceAcc, destinationAcc);
                    btnUpdate.setText("Start Transfer");
                }
                catch(SQLException ex) 
                {
                        Logger.getLogger(ReceivePaymentDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
            
            
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
         AccountsManagement.logger.info("Loading Treasury Dialog");
         TreasuryDialog treasuryDialog = new TreasuryDialog();      
         
         dlgTreasury.setVisible(true);          
         dlgTreasury.dispose(); //close the app once done
     }
    
}
