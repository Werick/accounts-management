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
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.Prepayment;
import org.lown.consultancy.accounts.Purchase;
import org.lown.consultancy.accounts.api.CashService;
import org.lown.consultancy.accounts.api.CompanyService;
import org.lown.consultancy.accounts.api.PurchasesService;
import org.lown.consultancy.accounts.tables.SupplierInvoiceList;
import org.lown.consultancy.accounts.tables.SupplierList;

/**
 *
 * @author LENOVO USER
 */
public class PaySupplierDialog extends JPanel implements ActionListener{
    
    private static final String ACT_BACK="close";
    private static final String ACT_PAY="pay_supplier";
    private static final String ACT_ALLOT="allot_payment";
    private static final String ACT_PAYMETHOD="payment_method";
    private static final String ACT_ACCOUNT="Account_balance";
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 16);
    public static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
    
    //buttons
    private JButton btnAllocate;
    private JButton btnPost;
    private JButton btnClose;
    
    private static JDialog dlgPaySupplier;
    
     //Panels
    private JPanel pSupplier;
    private JPanel pTransactions;
    private JPanel pPayments;
    private JPanel pBalance;
    
    //Border Titles
    private TitledBorder paymentTitle = new TitledBorder("Payment Details");
    private TitledBorder balanceTitle = new TitledBorder("Available Cash/Bank Balance");
    private TitledBorder SupplierTitle = new TitledBorder("Make Payment To: ");
    private TitledBorder txTitle = new TitledBorder("Invoices and Outstanding Transactions");
    
    //Labels
    private JLabel lbl_supplierName;
    private JLabel lbl_supplierNumber;
    private JLabel lbl_address;
    private JLabel lbl_phone;
    private JLabel lbl_chequeNumber;
    private JLabel lbl_paymentDate;
    private JLabel lbl_payAmount;
    private JLabel lbl_payMode;
    private JLabel lbl_balance;    
    private JLabel lbl_prepay;
    
    private JXDatePicker dp_paymentDate;
    
    //Text Boxes
    private JTextField txt_prepay;
    private JTextField txt_availBalance;
    private JTextField txt_balance;
    private JTextField txt_suppliererName;
    private JTextField txt_supplierNumber;
    private JTextField txt_address;
    private JTextField txt_phone;
    private JTextField txt_chequeNumber;
    private JTextField txt_payAmount;
    
    
    private String payOptions[]={"Select","Cash","Bank","Mobile Money","Other"};
    private JComboBox cbo_payMode;
    private JComboBox cbo_bank_mobile;
    
    private SupplierInvoiceList invoiceList;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private List<Cash> cashList;
    private List<Account> accountList;
    private List<Prepayment> prepayAllotList;
    private Prepayment prepayment;
    private double totalPrePaymentBF=0.0;
    private double totalPrePaymentCF=0.0;
    private PurchasesService ps;
    private CashService cs;
    private CompanyService companyService;
    double availBalance;
    private static Map<String, Integer> accountMap;
    public PaySupplierDialog()
    {
        dlgPaySupplier= new JDialog((JDialog)null, "Pay Supplier", true);
        dlgPaySupplier.setLayout(null);
        dlgPaySupplier.setSize(850, 600);//Width size, Height size
        dlgPaySupplier.setLocationRelativeTo(null);//center the invoice on the screen
        
        accountMap=new HashMap<String,Integer>();
        accountList=new ArrayList<Account>();
        companyService=new CompanyService();
        availBalance=0.0;
        
        pBalance=new JPanel();
        pBalance.setBounds(10, 160, 370, 60);
        pBalance.setBorder(balanceTitle);  
        pBalance.setLayout(null);
        dlgPaySupplier.add(pBalance);
        
        txt_availBalance=new JTextField();
        txt_availBalance.setBounds(50, 25, 300, 25);         
        txt_availBalance.setText("");    
        txt_availBalance.setFont(title2Font);
        txt_availBalance.setEditable(false);
        txt_availBalance.setHorizontalAlignment(JTextField.RIGHT);
        pBalance.add(txt_availBalance);
        
        
        pSupplier=new JPanel();
        pSupplier.setBounds(10, 10, 370, 150);
        pSupplier.setBorder(SupplierTitle);  
        pSupplier.setLayout(null);
        //dlgPaySupplier.add(pCustomer);
        
        lbl_supplierNumber=new JLabel();
        lbl_supplierNumber.setBounds(10, 20, 100, 25);         
        lbl_supplierNumber.setText("Number:"); 
        lbl_supplierNumber.setFont(title2Font);
        pSupplier.add(lbl_supplierNumber);
        
        txt_supplierNumber=new JTextField();
        txt_supplierNumber.setBounds(80, 20, 200, 25);         
        txt_supplierNumber.setText("");     
        txt_supplierNumber.setEditable(false);
        txt_supplierNumber.setFont(title2Font);
        pSupplier.add(txt_supplierNumber);
        
        lbl_supplierName=new JLabel();
        lbl_supplierName.setBounds(10, 50, 100, 25);         
        lbl_supplierName.setText("Name:"); 
        lbl_supplierName.setFont(title2Font);
        pSupplier.add(lbl_supplierName);
        
        txt_suppliererName=new JTextField();
        txt_suppliererName.setBounds(80, 50, 250, 25);         
        txt_suppliererName.setText("");    
        txt_suppliererName.setFont(title2Font);
        txt_suppliererName.setEditable(false);
        pSupplier.add(txt_suppliererName);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(10, 80, 100, 25);         
        lbl_address.setText("Address:"); 
        lbl_address.setFont(title2Font);
        pSupplier.add(lbl_address);
        
        txt_address=new JTextField();
        txt_address.setBounds(80, 80, 250, 25);         
        txt_address.setText("");     
        txt_address.setEditable(false);
        txt_address.setFont(title2Font);
        pSupplier.add(txt_address);
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(10, 110, 100, 25);         
        lbl_phone.setText("Phone#:"); 
        lbl_phone.setFont(title2Font);
        pSupplier.add(lbl_phone);
        
        txt_phone=new JTextField();
        txt_phone.setBounds(80, 110, 200, 25);         
        txt_phone.setText("");    
        txt_phone.setFont(title2Font);
        txt_phone.setEditable(false);
        pSupplier.add(txt_phone);
        
        dlgPaySupplier.add(pSupplier);
        
        //payment Information
        pPayments=new JPanel();
        pPayments.setBounds(400, 10, 400, 210);
        pPayments.setBorder(paymentTitle);  
        pPayments.setLayout(null);
        dlgPaySupplier.add(pPayments);
        
        lbl_paymentDate=new JLabel();
        lbl_paymentDate.setBounds(10, 20, 100, 25);         
        lbl_paymentDate.setText("Date:"); 
        lbl_paymentDate.setFont(title2Font);
        pPayments.add(lbl_paymentDate);
        
        dp_paymentDate=new JXDatePicker();
        dp_paymentDate.setDate(new Date());
        dp_paymentDate.setFormats(new String[] { "dd-MMM-yyyy" });
        dp_paymentDate.setBounds(150, 20, 150, 20);
        dp_paymentDate.setEditable(false);
        pPayments.add(dp_paymentDate);
        
        lbl_payMode=new JLabel();
        lbl_payMode.setBounds(10, 50, 130, 20);         
        lbl_payMode.setText("Payment Account:");
        lbl_payMode.setFont(title2Font);     
        pPayments.add(lbl_payMode);
        
        cbo_payMode=new JComboBox(payOptions);
        cbo_payMode.setBounds(150, 50, 150, 20);         
        cbo_payMode.setActionCommand(ACT_PAYMETHOD);
        cbo_payMode.addActionListener(this);          
        pPayments.add(cbo_payMode);
        
        lbl_chequeNumber=new JLabel();
        lbl_chequeNumber.setBounds(10, 80, 100, 25);         
        lbl_chequeNumber.setText("Bank Account:"); 
        lbl_chequeNumber.setFont(title2Font);
        pPayments.add(lbl_chequeNumber);
        
        txt_chequeNumber=new JTextField();
        txt_chequeNumber.setBounds(150, 80, 200, 25);         
        txt_chequeNumber.setText("");            
        txt_chequeNumber.setEditable(false);
        //pPayments.add(txt_chequeNumber);
        
        cbo_bank_mobile=new JComboBox();
        cbo_bank_mobile.setBounds(150, 80, 200, 25);          
        //cbo_bank_mobile.setFont(title2Font);
        cbo_bank_mobile.setActionCommand(ACT_ACCOUNT);
        cbo_bank_mobile.addActionListener(this);
        cbo_bank_mobile.setEditable(false);
        pPayments.add(cbo_bank_mobile);
        
        lbl_payAmount=new JLabel();
        lbl_payAmount.setBounds(10, 110, 100, 25);         
        lbl_payAmount.setText("Amount Paid:"); 
        lbl_payAmount.setFont(title2Font);
        pPayments.add(lbl_payAmount);
        
        txt_payAmount=new JTextField();
        txt_payAmount.setBounds(150, 110, 200, 25);         
        txt_payAmount.setText("");    
        txt_payAmount.setFont(title2Font);
        txt_payAmount.setEditable(false);
        txt_payAmount.setHorizontalAlignment(JTextField.RIGHT);
        pPayments.add(txt_payAmount);
        
        lbl_prepay=new JLabel();
        lbl_prepay.setBounds(10, 140, 100, 25);         
        lbl_prepay.setText("Prepayment:"); 
        lbl_prepay.setFont(title2Font);
        pPayments.add(lbl_prepay);
        
        txt_prepay=new JTextField();
        txt_prepay.setBounds(150, 140, 200, 25);         
        txt_prepay.setText("");    
        txt_prepay.setFont(title2Font);
        txt_prepay.setEditable(false);
        txt_prepay.setHorizontalAlignment(JTextField.RIGHT);
        pPayments.add(txt_prepay);
        
        lbl_balance=new JLabel();
        lbl_balance.setBounds(10, 170, 100, 25);         
        lbl_balance.setText("Balance:"); 
        lbl_balance.setFont(title2Font);
        pPayments.add(lbl_balance);
        
        txt_balance=new JTextField();
        txt_balance.setBounds(150, 170, 200, 25);         
        txt_balance.setText("");    
        txt_balance.setFont(title2Font);
        txt_balance.setEditable(false);
        txt_balance.setHorizontalAlignment(JTextField.RIGHT);
        pPayments.add(txt_balance);
        
        
        //Transactions
        
        pTransactions=new JPanel();
        pTransactions.setBounds(10, 230, 650, 320);
        pTransactions.setBorder(txTitle);  
        pTransactions.setLayout(null);
        dlgPaySupplier.add(pTransactions);
        
        invoiceList=new SupplierInvoiceList();
        invoiceList.setBounds(20,20,620, 280);        
        pTransactions.add(invoiceList);
        
        btnAllocate=new JButton("Allocate Cash");
        btnAllocate.setBounds(670, 240, 150, 40);
        btnAllocate.setActionCommand(ACT_ALLOT);
        btnAllocate.addActionListener(this);
        //dlgPaySupplier.add(btnAllocate);
        
        
        btnPost=new JButton("Post");
        btnPost.setBounds(670, 300, 150, 40);
        btnPost.setActionCommand(ACT_PAY);
        btnPost.addActionListener(this);
        dlgPaySupplier.add(btnPost);
        
        
        
        btnClose=new JButton("Close");
        btnClose.setBounds(670, 360, 150, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgPaySupplier.add(btnClose);
        
        if(SupplierList.selectedSupplier!=null)
         {
             invoiceList.insertRow(SupplierList.selectedSupplier);             
             txt_suppliererName.setText(SupplierList.selectedSupplier.getSupplierName());
             txt_supplierNumber.setText(SupplierList.selectedSupplier.getSupplierNumber());
             txt_address.setText(SupplierList.selectedSupplier.getAddress());
             txt_phone.setText(SupplierList.selectedSupplier.getPhone());
             
             if (SupplierDashboard.balance>=0)
             {
                txt_balance.setText(df.format(SupplierDashboard.balance));
             }
             else
             {                        
                txt_balance.setText("("+df.format(SupplierDashboard.balance)+")");  
             }
             ps=new PurchasesService();
             totalPrePaymentBF=ps.getTotalPrepaymentBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
             txt_prepay.setText(df.format(totalPrePaymentBF));
         }
        dlgPaySupplier.setVisible(true);          
        dlgPaySupplier.dispose(); //close the app once done
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
         
         AccountsManagement.logger.info("Loading Supplier Payment Dialog");              
         PaySupplierDialog payment = new PaySupplierDialog();              
     }
     
     
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            updateSupplierDashboard();
            dlgPaySupplier.setVisible(false);
            return;
	}
        else if (e.getActionCommand().equals(ACT_PAY))
        {
            if (allocate())
            {
              postTransaction();  
            }
            
        }
        else  if(e.getActionCommand().equals(ACT_ALLOT))
        {
//            allocate();
                
        }
        else if(e.getActionCommand().equals(ACT_ACCOUNT))
        {
            int selAccount=-1;
            System.out.println("Testing Account Map........");
            if(!accountMap.isEmpty())
            {
                if (cbo_bank_mobile.getItemCount()>1)
                {
                   selAccount=accountMap.get(cbo_bank_mobile.getSelectedItem());
                    txt_availBalance.setText("");
                    cs=new CashService();
                    availBalance=cs.getAccountBalanceById(selAccount);
                    txt_availBalance.setText(df.format(availBalance)); 
                    txt_payAmount.setEditable(true);
                }  
                
            }           
	}
        else if(e.getActionCommand().equals(ACT_PAYMETHOD))
        {
            System.out.println("Testing Payment Methods........");
            txt_availBalance.setText("");
            if (cbo_payMode.getSelectedIndex()==1)
            {
                txt_availBalance.setText("");
                cs=new CashService();
                availBalance=cs.getAvailableCashCollection();
                txt_availBalance.setText(df.format(availBalance));
                cbo_bank_mobile.removeAllItems();
                cbo_bank_mobile.setEnabled(false);
                txt_payAmount.setEditable(true);
               
            }
            else if ((cbo_payMode.getSelectedIndex()!=1)||(cbo_payMode.getSelectedIndex()!=0))
            {
                txt_availBalance.setText("");
                cbo_bank_mobile.removeAllItems();
                cbo_bank_mobile.setEnabled(true);
                System.out.println(cbo_payMode.getSelectedItem().toString());
                accountList=companyService.getAccountByCategory(cbo_payMode.getSelectedItem().toString());
                
                accountMap.clear();
                cbo_bank_mobile.addItem("Select");
                for (Account acc:accountList)
                {
                    cbo_bank_mobile.addItem(acc.getBank_name()+" - "+acc.getBranch());
                    String accName=acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
                    accountMap.put(accName, acc.getAccount_id());
                }
            }
           
	}
    }
    
    private void postTransaction()
    {
        if (!SupplierInvoiceList.transactions.isEmpty())//ensure that the list is not empty
        {              
             cashList=new ArrayList<Cash>();
             prepayAllotList=new ArrayList<Prepayment>();
             System.out.println("Total Prepayment Amount BF: "+totalPrePaymentBF);
             for(Purchase tx:SupplierInvoiceList.transactions)
             {
                     
                 //post only if there is allocation amount assigned to a transaction
                 if(tx.getAllocation()<=0.0)
                 {
                     continue; //skip the current transaction and check the next transaction
                 }
                 Cash cash=new Cash();    
                 Prepayment prepayAllocation=new Prepayment();
                 int selAccount=1; //assume cash account is selected
                 if((cbo_payMode.getSelectedIndex()==2)||(cbo_payMode.getSelectedIndex()==3))
                 {
                     selAccount=accountMap.get(cbo_bank_mobile.getSelectedItem());
                 }
                 
                 Account acc=new Account();
                 acc=companyService.getAccountById(selAccount);

                 System.out.println("Selected Account Id: "+ selAccount);
                 tx.setAccount(acc);//get account info to allow sucessful posting to right account

                  //creating the cash object to be posted to the database
                 cash.setDate(dp_paymentDate.getDate());                     
                 cash.setPurchase(tx);
                 if(totalPrePaymentBF>=tx.getAllocation())
                 {                       

                    cash.setPrepayment(tx.getAllocation());
                    cash.setAmount(0.0);
                    totalPrePaymentBF=totalPrePaymentBF-tx.getAllocation();                        
                    System.out.println("Prepayment Amount: "+ tx.getAllocation());
                    System.out.println("Prepayment Balance Amount: "+ totalPrePaymentBF);
                    tx.setPaid(true);   

                    //creating prepayment allocation object
                    prepayAllocation.setDate(dp_paymentDate.getDate());
                    prepayAllocation.setAmountAllocated(tx.getAllocation());
                    //prepayAllocation.setCustomer(tx.getCustomer());
                    prepayAllocation.setPurchase(tx);
                    prepayAllotList.add(prepayAllocation);
                  }
                 else
                 {
                    cash.setPrepayment(totalPrePaymentBF);
                    cash.setAmount(tx.getAllocation()-totalPrePaymentBF);                        
                     //creating prepayment allocation object
                    if (totalPrePaymentBF>0.0)
                    {
                        prepayAllocation.setDate(dp_paymentDate.getDate());
                        prepayAllocation.setAmountAllocated(totalPrePaymentBF);
                        //prepayAllocation.setCustomer(tx.getCustomer());
                        prepayAllocation.setPurchase(tx);
                        prepayAllotList.add(prepayAllocation);
                    }
                    totalPrePaymentBF=0;
                    if (tx.getAllocation()==tx.getBalance())
                    {
                        tx.setPaid(true); 
                    }
                    else
                    {
                        tx.setPaid(false); 
                    }                        

                    System.out.println("Amount: "+ tx.getAllocation());
                    System.out.println("Balance Amount: " + totalPrePaymentBF);
                 }                  
                     
                     
                 if(cbo_payMode.getSelectedItem().equals("Cash"))
                 {
                    cash.setAccount(cbo_payMode.getSelectedItem().toString());
                    cash.setTxCode(4);
                    cash.setTxType("CR");
                 }
                 else if(cbo_payMode.getSelectedItem().equals("Bank"))
                 {
                    cash.setAccount("Bank");
                    cash.setTxCode(5);
                    cash.setChequeNumber(txt_chequeNumber.getText());

                    cash.setTxType("CR");
                 }
                 else if(cbo_payMode.getSelectedItem().equals("Mobile Money"))
                 {
                    cash.setAccount("Mobile Money");
                    cash.setTxCode(16);
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("CR");
                 }
                 else
                 {
                    //assume cash account
                    cash.setAccount("Cash");
                    cash.setTxCode(4);
                    cash.setTxType("CR");
                 }
                     cashList.add(cash);
             }   
                 
             /*
              * check if there is any pending prepayment Balance to be carried forward and post it a prepayment
              * That is not tied to any invoice transaction
              */
             if (totalPrePaymentCF>0.0)
             {
                 Cash cash=new Cash();                     
                  //creating the cash object to be posted to the database
                 cash.setDate(dp_paymentDate.getDate());
                 cash.setPurchase(SupplierInvoiceList.transactions.get(0));
                 cash.setPrepayment(totalPrePaymentCF);
                 cash.setAmount(totalPrePaymentCF);
                 if(cbo_payMode.getSelectedItem().equals("Cash"))
                 {
                    cash.setAccount(cbo_payMode.getSelectedItem().toString());
                    cash.setTxCode(4);
                    cash.setTxType("CR");
                 }
                 else if(cbo_payMode.getSelectedItem().equals("Cheque"))
                 {
                    cash.setAccount("Bank");
                    cash.setTxCode(5);
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("CR");
                 }
                 else if(cbo_payMode.getSelectedItem().equals("Mobile Money"))
                 {
                    cash.setAccount("Mobile Money");
                    cash.setTxCode(16);
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("CR");
                 }
                 else
                 {
                    //assume cash account
                    cash.setAccount("Cash");
                    cash.setTxCode(4);
                    cash.setTxType("CR");
                 }
                 cashList.add(cash);

                 //create prepayment object
                 prepayment=new Prepayment();
                 prepayment.setAmount(totalPrePaymentCF);
                 //prepayment.setCustomer(InvoiceList.transactions.get(0).getCustomer());
                 prepayment.setDate(dp_paymentDate.getDate());
                 prepayment.setPurchase(SupplierInvoiceList.transactions.get(0));

                 //Saving a prepayment object
             }
             System.out.println("Testing Cash TX");
             testPostPay(cashList,SupplierInvoiceList.transactions);
             ps=new PurchasesService();
             try {
                if (btnPost.getText().equalsIgnoreCase("Post"))
                {
                   //post payments
                    ps.postSupplierPayment(cashList,SupplierInvoiceList.transactions);
                    if (prepayment!=null)
                    {
                        ps.postPrepayment(prepayment);
                    }
                    if (!prepayAllotList.isEmpty())
                    {
                        ps.postPrePaymentAllocation(prepayAllotList);
                    }
                    
                    //update supplier dialog dash board
                    updateSupplierDashboard();
                    dlgPaySupplier.setVisible(false);
                    //btnPost.setText("Done");                   
                }
                else if (btnPost.getText().equalsIgnoreCase("Done"))
                {
                    //code to generate payment receipt
                    invoiceList.deleteRows();
                    btnPost.setText("Done");
                }

            } catch (SQLException ex) {
                Logger.getLogger(ReceivePaymentDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }
    
    public void updateSupplierDashboard()
    {
        SupplierDashboard.txListTable.insertRow(SupplierList.selectedSupplier);
        try
        {
            double totalPurchases=ps.getTotalPurchasesBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
            double totalPayments=ps.getTotalPaymentsBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
            double balance=totalPurchases-totalPayments;
            double lastPayment=ps.getLastPaymentMade(SupplierList.selectedSupplier);
            SupplierDashboard.txt_totalPurchases.setText(SupplierDashboard.df.format(totalPurchases));
            if (balance>=0)
            {
                SupplierDashboard.txt_balance.setText(SupplierDashboard.df.format(balance));
            }
            else
            {                        
                SupplierDashboard.txt_balance.setText("("+SupplierDashboard.df.format(balance)+")");  
            }
            SupplierDashboard.txt_lastPayment.setText(SupplierDashboard.df.format(lastPayment));
        }
        catch(Exception ex)
        {
            //ignore error
        }
    }
    
    /*
     * This function allocates cash to various pending resources
     * It will return true if the allocation is sucessfull.
     * Otherwise it should return false
     */
    private boolean allocate()
    {
        boolean ok=false;
        if((cbo_payMode.getSelectedIndex()!=0)&&(cbo_bank_mobile.getSelectedIndex()!=0))
        {
                if(!txt_payAmount.getText().isEmpty())//proceed if amount paid is specified
                {
                    if(isNumeric(txt_payAmount.getText()))
                    {
                        /*
                         * get what we want to pay the supplier and allocate according to the pending/outstanding
                         * Purchase transactions/Invoices
                         * This should be done before the transaction is posted
                         * If there any previous prepayments we need to consider this prepayment and allocate it appropriately
                         */
                        double amtPaid=Double.parseDouble(txt_payAmount.getText());                        
                        double amtAvailable=0.0;
                        amtAvailable=amtPaid+totalPrePaymentBF;                        
                       
                        if (availBalance<amtAvailable)
                        {
                            //The selected account does not have enough balance to allocate the pending Invoices
                            //Advice the user to ensuer that there is enough cash to execute this transaction
                             JOptionPane.showMessageDialog(null, "You don't have Enough Balance to Execute this Transaction... ");
                             //invoiceList.deleteRows();
                             ok=false;
                             return  ok;
                        }
                        System.out.println("Amount Paid: "+ amtPaid);
                        double amtBalance=amtAvailable;
                        double amtAllocated=0.0;
                        if (!SupplierInvoiceList.transactions.isEmpty())//ensure that the list is not empty
                        {
                            for(Purchase tx:SupplierInvoiceList.transactions)
                            {                                
                                if(amtBalance>=tx.getBalance())
                                {
                                    amtAllocated=tx.getBalance();
                                    amtBalance=amtBalance-amtAllocated;
                                    tx.setAllocation(amtAllocated);
                                    System.out.println("Alloted Amount: "+ amtAllocated);
                                    System.out.println("Balance Amount: "+ amtBalance);
                                    tx.setPaid(true);                          
                                }
                                else
                                {
                                    amtAllocated=amtBalance;
                                    amtBalance=amtBalance-amtAllocated;
                                    tx.setAllocation(amtAllocated);
                                    System.out.println("Alloted Amount: "+ amtAllocated);
                                    System.out.println("Balance Amount: "+ amtBalance);
                                }
                                
                            }
                            //generate the new table with allocations
                            totalPrePaymentCF=amtBalance;
                            SupplierInvoiceList.getAllocatedList();
                            ok=true;
                        }
                        
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Specify the Amount then proceed... ");
                }
            }
            else
            {
                 JOptionPane.showMessageDialog(null, "Select the Payment Account and/or Bank first... ");
            }
        return ok;
    }
    
    
    private void testPostPay(List<Cash> cashTx, List<Purchase> tx)
    {
        if(!cashTx.isEmpty())
        {
           
            for(Cash cash:cashTx)
            {
                System.out.println(cash.getDate()+"\t"+cash.getAmount()+"\t"+cash.getPrepayment());
            }
        }
        
        for(Purchase p:tx)
        {
            System.out.println(p.getDueDate()+"\t");
            System.out.println(p.isPaid()+"\t");
            System.out.println(p.getInvoiceNumber());
            //System.out.println(p.getAccount().getAccount_id());
        }
        
    }
    
}
