/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXDatePicker;
import org.lown.consultancy.accounts.Account;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.dao.CashService;
import org.lown.consultancy.accounts.dao.CompanyService;

/**
 *
 * @author LENOVO USER
 */
public class TreasuryDialog extends JPanel implements ActionListener{
    
    private static final String ACT_BANK="manage_Bank";    ;
    private static final String ACT_SOURCE="source_account";  
    private static final String ACT_DESTINATION="destination_account";  
    private static final String ACT_BACK="close";
    private static final String ACT_TRANSFER="transfer_money";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    
    private String payOptions[]={"Select","Cash Collection","Bank/Cheque Collection","Mobile Money"};
    private static JDialog dlgTreasury;
    
    private JButton btnBank;
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
    private CashService cs;
    private CompanyService companyService;
    
    
    private TitledBorder titled2 = new TitledBorder("Cash Transfer");
    private double availBalance;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private static Map<String, Integer> accountMap;
    private List<Account> accountList;
    
    public TreasuryDialog()
    {
        accountMap=new HashMap<String,Integer>();
        accountList=new ArrayList<Account>();
        
        companyService=new CompanyService();
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
        cbo_source.setBounds(250, 100, 200, 25);
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
            cbo_source.addItem(acc.getBank_name()+" - "+acc.getBranch());
            String accName=acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
            accountMap.put(accName, acc.getAccount_id());
        }
        
        lbl_availableAmount=new JLabel();
        lbl_availableAmount.setBounds(20, 150, 200, 25);         
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
        cbo_destination.setBounds(250, 200, 200, 25);
        cbo_destination.setFont(titleFont);
        pTransfers.add(cbo_destination);
        
        for (Account acc:accountList)
        {
            if(acc.getBank_name().equalsIgnoreCase("Cash"))
            {
                continue;
            }
            cbo_destination.addItem(acc.getBank_name()+" - "+acc.getBranch());
            String accName=acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
           // accountMap.put(accName, acc.getAccount_id());
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
        
        btnUpdate=new JButton("Transfer");
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
        btnTransfer.setBounds(20, 150, 150, 50);
        btnTransfer.setActionCommand(ACT_TRANSFER);
        btnTransfer.addActionListener(this);
        dlgTreasury.add(btnTransfer);    
        
        btnClose=new JButton("Back");
        btnClose.setBounds(20, 250, 150, 50);
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
        else  if(e.getActionCommand().equals(ACT_BANK))
        {
            AccountDialog.createAndShowGUI(); 
            return;
	}
        else if(e.getActionCommand().equals(ACT_SOURCE))
        {
            System.out.println("Testing Source Accounts........");
            txt_availableAmount.setText("");
            if (cbo_source.getSelectedIndex()==1)
            {
                txt_availableAmount.setText("");
                cs=new CashService();
                availBalance=cs.getAvailableCashCollection();
                txt_availableAmount.setText(df.format(availBalance));
                
               
            }
            else if (cbo_source.getSelectedIndex()==2)
            {
                txt_availableAmount.setText("");
                cs=new CashService();
                availBalance=cs.getAvailableBankCollection();
                txt_availableAmount.setText(df.format(availBalance));
                
//                System.out.println(cbo_payMode.getSelectedItem().toString());
//                accountList=companyService.getAccountByCategory(cbo_payMode.getSelectedItem().toString());
//                
//                accountMap.clear();
//                cbo_bank_mobile.addItem("Select");
//                for (Account acc:accountList)
//                {
//                    cbo_bank_mobile.addItem(acc.getBank_name()+" - "+acc.getBranch());
//                    String accName=acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
//                    accountMap.put(accName, acc.getAccount_id());
//                }
            }
            else if (cbo_source.getSelectedIndex()==3)
            {
                txt_availableAmount.setText("");
                cs=new CashService();
                availBalance=cs.getAvailableMobileMoney();
                txt_availableAmount.setText(df.format(availBalance));              
            }
             else if (cbo_source.getSelectedIndex()>3)
             {
                 //check the indivual accounts using the accounts map
                 
             }          
	}
        else if(e.getActionCommand().equals(ACT_TRANSFER))
        {
            //get the source account details
            Cash source=new Cash();
            source.setTxType("CR");
            source.setTxCode(7); //withdraw cash from account
            
            source.setAccount(ACT_BANK);
            //Get the destination account details
        }
        
    }
    
     public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Treasury Dialog");
         TreasuryDialog treasuryDialog = new TreasuryDialog();      
         
         dlgTreasury.setVisible(true);          
         dlgTreasury.dispose(); //close the app once done
     }
    
}
