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
import org.lown.consultancy.accounts.CashTransfer;
import org.lown.consultancy.accounts.Prepayment;
import org.lown.consultancy.accounts.SalesTransaction;
import org.lown.consultancy.accounts.dao.CashDAO;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.dao.SalesDAO;
import org.lown.consultancy.accounts.tables.CustomerListTable;
import org.lown.consultancy.accounts.tables.InvoiceList;

/**
 *
 * @author LENOVO USER
 */
public class ReceivePaymentDialog extends JPanel implements ActionListener{
    
    private static final String ACT_BACK="close";
    private static final String ACT_RECEIVE="receive_payment";
    private static final String ACT_ALLOT="allot_payment";
    private static final String ACT_PAYMETHOD="payment_method";
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 16);
    public static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
    
    //buttons
    private JButton btnAllocate;
    private JButton btnPost;
    private JButton btnClose;
    
    private static JDialog dlgReceivePayment;
    
    //Panels
    private JPanel pCustomer;
    private JPanel pTransactions;
    private JPanel pPayments;
    
    //Titles
    private TitledBorder paymentTitle = new TitledBorder("Payment Details");
    private TitledBorder customerTitle = new TitledBorder("Receive Payment From: ");
    private TitledBorder txTitle = new TitledBorder("Invoices and Outstanding Transactions");
    
    //Labels
    private JLabel lbl_customerName;
    private JLabel lbl_customerNumber;
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
    private JTextField txt_balance;
    private JTextField txt_customerName;
    private JTextField txt_customerNumber;
    private JTextField txt_address;
    private JTextField txt_phone;
    private JTextField txt_chequeNumber;
    private JTextField txt_payAmount;
    
    
    private String payOptions[]={"Select","Cash","Mobile Money","Cheque","Direct Transfer"};
    private JComboBox cbo_payMode;
    private JComboBox cbo_accounts;
    
    private InvoiceList invoiceList;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private List<Cash> cashList;
    private List<Prepayment> prepayAllotList;
    private Prepayment prepayment;
    private double totalPrePaymentBF=0.0;
    private double totalPrePaymentCF=0.0;
    private SalesDAO ss;
    private CompanyDAO companyService;
    private List<Account> accountList;
    private static Map<String, Integer> accountMap;
    
    private CashTransfer sourceAcc;
    private CashTransfer destinationAcc;
    private CashDAO cs;
    
    public ReceivePaymentDialog()
    {
        
        accountMap=new HashMap<String,Integer>();
        accountList=new ArrayList<Account>();
        
        companyService=new CompanyDAO();
        accountList=companyService.getAllAccounts();
        
        
        dlgReceivePayment= new JDialog((JDialog)null, "Receive Customer Payment", true);
        dlgReceivePayment.setLayout(null);
        dlgReceivePayment.setSize(850, 600);//Width size, Height size
        dlgReceivePayment.setLocationRelativeTo(null);//center the invoice on the screen
        
        
        pCustomer=new JPanel();
        pCustomer.setBounds(10, 10, 350, 150);
        pCustomer.setBorder(customerTitle);  
        pCustomer.setLayout(null);
        //dlgReceivePayment.add(pCustomer);
        
        lbl_customerNumber=new JLabel();
        lbl_customerNumber.setBounds(10, 20, 100, 25);         
        lbl_customerNumber.setText("Number:"); 
        lbl_customerNumber.setFont(title2Font);
        pCustomer.add(lbl_customerNumber);
        
        txt_customerNumber=new JTextField();
        txt_customerNumber.setBounds(80, 20, 200, 25);         
        txt_customerNumber.setText("");     
        txt_customerNumber.setEditable(false);
        txt_customerNumber.setFont(title2Font);
        pCustomer.add(txt_customerNumber);
        
        lbl_customerName=new JLabel();
        lbl_customerName.setBounds(10, 50, 100, 25);         
        lbl_customerName.setText("Name:"); 
        lbl_customerName.setFont(title2Font);
        pCustomer.add(lbl_customerName);
        
        txt_customerName=new JTextField();
        txt_customerName.setBounds(80, 50, 200, 25);         
        txt_customerName.setText("");    
        txt_customerName.setFont(title2Font);
        txt_customerName.setEditable(false);
        pCustomer.add(txt_customerName);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(10, 80, 100, 25);         
        lbl_address.setText("Address:"); 
        lbl_address.setFont(title2Font);
        pCustomer.add(lbl_address);
        
        txt_address=new JTextField();
        txt_address.setBounds(80, 80, 200, 25);         
        txt_address.setText("");     
        txt_address.setEditable(false);
        txt_address.setFont(title2Font);
        pCustomer.add(txt_address);
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(10, 110, 100, 25);         
        lbl_phone.setText("Phone#:"); 
        lbl_phone.setFont(title2Font);
        pCustomer.add(lbl_phone);
        
        txt_phone=new JTextField();
        txt_phone.setBounds(80, 110, 200, 25);         
        txt_phone.setText("");    
        txt_phone.setFont(title2Font);
        txt_phone.setEditable(false);
        pCustomer.add(txt_phone);
        
        dlgReceivePayment.add(pCustomer);
        
        //payment Information
        pPayments=new JPanel();
        pPayments.setBounds(400, 10, 400, 210);
        pPayments.setBorder(paymentTitle);  
        pPayments.setLayout(null);
        dlgReceivePayment.add(pPayments);
        
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
        lbl_payMode.setText("Payment Method:");
        lbl_payMode.setFont(title2Font);     
        pPayments.add(lbl_payMode);
        
        cbo_payMode=new JComboBox(payOptions);
        cbo_payMode.setBounds(150, 50, 150, 20);         
        cbo_payMode.setActionCommand(ACT_PAYMETHOD);
        cbo_payMode.addActionListener(this);
        cbo_payMode.setFont(title3Font);     
        pPayments.add(cbo_payMode);
        
        lbl_chequeNumber=new JLabel();
        lbl_chequeNumber.setBounds(10, 80, 100, 25);         
        lbl_chequeNumber.setText("Cheque#:"); 
        lbl_chequeNumber.setFont(title2Font);
        pPayments.add(lbl_chequeNumber);
        
        txt_chequeNumber=new JTextField();
        txt_chequeNumber.setBounds(150, 80, 200, 25);         
        txt_chequeNumber.setText("");    
        txt_chequeNumber.setFont(title2Font);
        txt_chequeNumber.setEditable(false);
        pPayments.add(txt_chequeNumber);
        
        cbo_accounts=new JComboBox();
        cbo_accounts.setBounds(150, 80, 200, 25);     
        cbo_accounts.setFont(title2Font);
        //cbo_accounts.setEditable(false);
        cbo_accounts.setVisible(false);
        pPayments.add(cbo_accounts);
        
        for (Account acc:accountList)
        {
            if(acc.getBank_name().equalsIgnoreCase("Cash")) //skip cash account
            {
                continue;
            }
            cbo_accounts.addItem(acc.getAccount_name()+" - "+ acc.getBank_name()+" - "+acc.getBranch());
            String accName=acc.getAccount_name()+" - "+ acc.getBank_name()+" - "+acc.getBranch();// this combination my require some change in future
            accountMap.put(accName, acc.getAccount_id());
        }
        
        lbl_payAmount=new JLabel();
        lbl_payAmount.setBounds(10, 110, 100, 25);         
        lbl_payAmount.setText("Amount Paid:"); 
        lbl_payAmount.setFont(title2Font);
        pPayments.add(lbl_payAmount);
        
        txt_payAmount=new JTextField();
        txt_payAmount.setBounds(150, 110, 200, 25);         
        txt_payAmount.setText("");    
        txt_payAmount.setFont(title2Font);
        txt_payAmount.setHorizontalAlignment(JTextField.RIGHT);
        txt_payAmount.setEditable(false);
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
        txt_prepay.setHorizontalAlignment(JTextField.RIGHT);
        txt_prepay.setEditable(false);
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
        txt_balance.setHorizontalAlignment(JTextField.RIGHT);
        txt_balance.setEditable(false);
        pPayments.add(txt_balance);
        
        
        //Transactions
        
        pTransactions=new JPanel();
        pTransactions.setBounds(10, 230, 650, 320);
        pTransactions.setBorder(txTitle);  
        pTransactions.setLayout(null);
        dlgReceivePayment.add(pTransactions);
        
        invoiceList=new InvoiceList();
        invoiceList.setBounds(20,20,620, 280);        
        pTransactions.add(invoiceList);
        
        btnAllocate=new JButton("Allocate Cash");
        btnAllocate.setBounds(670, 240, 150, 40);
        btnAllocate.setActionCommand(ACT_ALLOT);
        btnAllocate.addActionListener(this);
        //dlgReceivePayment.add(btnAllocate);
        
        
        btnPost=new JButton("Post");
        btnPost.setBounds(670, 300, 150, 40);
        btnPost.setActionCommand(ACT_RECEIVE);
        btnPost.addActionListener(this);
        btnPost.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgReceivePayment.add(btnPost);
        
        
        
        btnClose=new JButton("Close");
        btnClose.setBounds(670, 360, 150, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgReceivePayment.add(btnClose);
        
        if(CustomerListTable.selectedCustomer!=null)
         {
             invoiceList.insertRow(CustomerListTable.selectedCustomer);             
             txt_customerName.setText(CustomerListTable.selectedCustomer.getCustomerName());
             txt_customerNumber.setText(CustomerListTable.selectedCustomer.getCustomerNumber());
             txt_address.setText(CustomerListTable.selectedCustomer.getAddress());
             txt_phone.setText(CustomerListTable.selectedCustomer.getPhone());
             //txt_balance.setText(df.format(CustomerDashboard.balance));
             if (CustomerDashboard.balance>=0)
             {
                txt_balance.setText(df.format(CustomerDashboard.balance));
             }
             else
             {                        
                txt_balance.setText("("+df.format(CustomerDashboard.balance)+")");  
             }
             ss=new SalesDAO();
             totalPrePaymentBF=ss.getTotalPrepaymentByCustomerId(CustomerListTable.selectedCustomer.getCustomer_id());
             if(totalPrePaymentBF>0.0) {
                 txt_prepay.setText("(-"+df.format(totalPrePaymentBF)+")");
             }
             else {
                 txt_prepay.setText(df.format(totalPrePaymentBF));
             }
         }
        dlgReceivePayment.setVisible(true);          
        dlgReceivePayment.dispose(); //close the app once done
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            updateCustomerDassboard();
            dlgReceivePayment.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_RECEIVE))
        {
             if (cbo_payMode.getSelectedIndex()==0) //proceed allocation only if the payment method is selected
             { 
                 JOptionPane.showMessageDialog(null, "Select the Payment Method first... ");
                 return;
             }
            
            int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to Post this Transaction and Print a Receipt?","Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
                if (allocatePayment())
                {
                     postPayment();
                }
               
                return;
            }   
            
	}
        else  if(e.getActionCommand().equals(ACT_ALLOT))
        {
           
            return;
	}
        else  if(e.getActionCommand().equals(ACT_PAYMETHOD))
        {
            txt_chequeNumber.setEditable(false);
            txt_payAmount.setEditable(false);
            cbo_accounts.setVisible(false);
            txt_chequeNumber.setVisible(true);
            if (cbo_payMode.getSelectedItem().equals("Cheque"))
            {
                txt_chequeNumber.setEditable(true);
                txt_payAmount.setEditable(true);
                lbl_chequeNumber.setText("Cheque #:");
            }
            else if (cbo_payMode.getSelectedItem().equals("Mobile Money"))
            {
                txt_chequeNumber.setEditable(true);
                lbl_chequeNumber.setText("Transaction Code:");
                txt_payAmount.setEditable(true);
            }
            else if (cbo_payMode.getSelectedItem().equals("Direct Transfer"))
            {
                lbl_chequeNumber.setText("Bank Account:");
                cbo_accounts.setVisible(true);
                txt_payAmount.setEditable(true);
                txt_chequeNumber.setVisible(false);
            }
            else if (!cbo_payMode.getSelectedItem().equals("Select"))
            {
                txt_payAmount.setEditable(true);
            }
            return;
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
    
    /*
     * This procedure creates various objects that will used to post payment to the database
     * It uses the list generate after allotment to post payment
     */
    
    private void postPayment()
    {
        if (!InvoiceList.transactions.isEmpty())//ensure that the list is not empty
        {
            cashList=new ArrayList<Cash>(); //store various cash/bank/mobile money allocations that will be transferred to the cash/bank/mobile money collection account
            prepayAllotList=new ArrayList<Prepayment>();//tracks how prepayments were allocated to pending invoices
            System.out.println("Total Prepayment Amount BF: "+totalPrePaymentBF);
            
            for(SalesTransaction tx:InvoiceList.transactions)
            {
                //post only if there is allocation amount assigned to a transaction
                if(tx.getAllocation()<=0.0)
                {
                    continue; //skip the current transaction and check the next transaction
                }
                Cash cash=new Cash();    
                Prepayment prepayAllocation=new Prepayment();
                //creating the cash object to be posted to the database
                cash.setDate(dp_paymentDate.getDate());
                cash.setCustomer(tx.getCustomer());
                cash.setSalesTransaction(tx);
                if(totalPrePaymentBF>=tx.getAllocation())// the current invoice will be fully paid by the prepayment
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
                    prepayAllocation.setCustomer(tx.getCustomer());
                    prepayAllocation.setSalesTransaction(tx);
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
                        prepayAllocation.setCustomer(tx.getCustomer());
                        prepayAllocation.setSalesTransaction(tx);
                        prepayAllotList.add(prepayAllocation);
                    }
                    totalPrePaymentBF=0;
                    if (tx.getAllocation()==tx.getBalance())//check if the balance is fully allocated and mark it as fully paid or not paid. An invoice is marked as paid only of it is fully cleared otherwise it is not paid
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
                
                //continue building the cash object inorder to allocate
                if(cbo_payMode.getSelectedItem().equals("Cash"))
                {
                    cash.setAccount(cbo_payMode.getSelectedItem().toString());//cash collection account
                    cash.setTxCode(2); //cash receipt transaction code
                    cash.setTxType("DR"); // DR for debit
                }
                else if(cbo_payMode.getSelectedItem().equals("Cheque"))
                {
                    cash.setAccount("Bank"); //cheque collection account
                    cash.setTxCode(6); //cheque receipt transaction code
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("DR"); // DR for debit
                }
                else if(cbo_payMode.getSelectedItem().equals("Mobile Money"))
                {
                    cash.setAccount("Mobile Money");
                    cash.setTxCode(15); //mobile money collection account
                    cash.setChequeNumber(txt_chequeNumber.getText()); //Mpesa/Orange/Airtel transaction number. This number shld be generated from the text msg received
                    cash.setTxType("DR");
                }
                else if(cbo_payMode.getSelectedItem().equals("Direct Transfer"))
                {
                    //We need to get the selected bank account where the money was transferred to
                    //We need to set the Account that is receiving the money
                    //This does not go the cash/bank/cheque collection account
                    
                    /*
                     * In my mind wot the system will do is to receive this money to bank collection account then
                     * it should be automatically be transfered to the selected account
                     * this to me will be a slightly better option
                     * TBD soon
                     */
                    sourceAcc=new CashTransfer();
                    sourceAcc.setTransactionType("CR");
                    sourceAcc.setTransactionCode(7); //withdrawal
                    sourceAcc.setTransferDate(dp_paymentDate.getDate());
                    sourceAcc.setDescription("Direct Transfer");
                    
                    destinationAcc=new CashTransfer();
                    destinationAcc.setTransactionType("DR");
                    destinationAcc.setTransactionCode(1); //deposit
                    destinationAcc.setTransferDate(dp_paymentDate.getDate());
                    destinationAcc.setDescription("Direct Transfer");
                    
                    int selAccount=1; //assume cash account is selected
                    Account source=new Account();
                    Account destination=new Account();
                    selAccount=accountMap.get(cbo_accounts.getSelectedItem());
                    destination=companyService.getAccountById(selAccount);
                    
                    source.setAccount_id(0);
                    source.setAccount_name("Bank");
                    
                    if (isNumeric(txt_payAmount.getText()))
                    {
                        sourceAcc.setAmount(Double.parseDouble(txt_payAmount.getText()));
                        destinationAcc.setAmount(Double.parseDouble(txt_payAmount.getText()));
                    }
                    sourceAcc.setAccount(source);
                    destinationAcc.setAccount(destination);
                    
                    //cash object
                    cash.setAccount("Bank");
                    cash.setTxCode(17);
                    cash.setTxType("DR");
                    //cash.s
                }
                cashList.add(cash);
            }   
                 
            /*
                  * check if there is any pending prepayment Balance to be carried forward and post it as prepayment
                  * that is not tied to any invoice transaction
            */
            if (totalPrePaymentCF>0.0)//this is already calculated during the cash allocation
            {
                Cash cash=new Cash();                     
                //creating the cash object to be posted to the database
                cash.setDate(dp_paymentDate.getDate());
                cash.setCustomer(InvoiceList.transactions.get(0).getCustomer());
                cash.setPrepayment(totalPrePaymentCF);
                cash.setAmount(totalPrePaymentCF);
                if(cbo_payMode.getSelectedItem().equals("Cash"))
                {
                    cash.setAccount(cbo_payMode.getSelectedItem().toString());
                    cash.setTxCode(2);
                    cash.setTxType("DR");
                }
                else if(cbo_payMode.getSelectedItem().equals("Cheque"))
                {
                    cash.setAccount("Bank");
                    cash.setTxCode(6);
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("DR");                        
                }
                else if(cbo_payMode.getSelectedItem().equals("Mobile Money"))
                {
                    cash.setAccount("Mobile Money");
                    cash.setTxCode(15);
                    cash.setChequeNumber(txt_chequeNumber.getText());
                    cash.setTxType("DR");
                }
                else if(cbo_payMode.getSelectedItem().equals("Direct Transfer"))
                {
                    //assume cash account
                    cash.setAccount("Cash");
                    cash.setTxCode(2);
                    cash.setTxType("DR");
                }
                cashList.add(cash);
                     
               //create prepayment object
               prepayment=new Prepayment();
               prepayment.setAmount(totalPrePaymentCF);
               prepayment.setCustomer(InvoiceList.transactions.get(0).getCustomer());
               prepayment.setDate(dp_paymentDate.getDate());        
            }
            System.out.println("Testing Cash TX");
            testCashReceipt(cashList,InvoiceList.transactions);
            ss=new SalesDAO();
            try 
            {
                if (btnPost.getText().equalsIgnoreCase("Post"))
                {
                    //post payments
                    ss.postPaymentReceipt(cashList,InvoiceList.transactions);
                    if (prepayment!=null)
                    {
                        ss.postPrepayment(prepayment);
                    }
                    if (!prepayAllotList.isEmpty())
                    {
                        ss.postPrePaymentAllocation(prepayAllotList);
                    }
                    
                    //excecute only if direct transfer is selected
                    if(cbo_payMode.getSelectedItem().equals("Direct Transfer"))
                    {
                        cs=new CashDAO();
                        cs.transferMoney(sourceAcc, destinationAcc);
                    }
                    //Ideally We'll print the receipt and close this window
                    //Printing procedure will come in here
                    
                    //update customer dashboard
                    updateCustomerDassboard();
                    btnPost.setText("Print Receipt");
                    return;
                }
               else if (btnPost.getText().equalsIgnoreCase("Print Receipt"))
               {
                        //code to generate payment receipt
               }
                    
           } catch (SQLException ex) {
                    Logger.getLogger(ReceivePaymentDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
        }        
    }
    /*
     * This Preocdure allocates payment to various pending invoices from a debtor/customer based on their due dates
     * It also generates a list that will allocations that is finally posted to the database for persistence
     */
    private boolean allocatePayment()
    {
        boolean ok=false;       
        //System.out.println("Testing Allot Tx");
        if (cbo_payMode.getSelectedIndex()!=0) //proceed allocation only if the payment method is selected
        {
            System.out.println("Testing Payment Method-Sucess");
                
            if(!txt_payAmount.getText().isEmpty()) //proceed allocation only if the amount is specified
            {
                //validate if the value is numeric                                        
                System.out.println("Testing Cash Amount Not Empty");
                if(isNumeric(txt_payAmount.getText()))
                {
                    /*
                         * get what the customer/Debtor has offered to pay and allocate according to the peending/outstanding
                         * Sales transactions/invoices
                         * This should be done before the transaction is posted
                         * If there any previous prepayments we need to consider this prepayment and allocate it appropriately
                    */
                    double amtPaid=Double.parseDouble(txt_payAmount.getText());
                    double amtAvailable=0.0; //temporary variable to track what should be allocated to pending invoice
                    amtAvailable=amtPaid+totalPrePaymentBF;     //this amt paid by the customer + any pending prepayments                   
                       
                    System.out.println("Amount Paid: "+ amtPaid);
                    double amtBalance=amtAvailable;
                    double amtAllocated=0.0;
                    if (!InvoiceList.transactions.isEmpty())//ensure that the list is not empty
                    {
                        //loop through the pending invoices and allocate payment
                        for(SalesTransaction tx:InvoiceList.transactions)
                        {
                            /*
                             * if money availabe is greater than the pending balance  for the current invoice then we allocate and 
                             * note the balance which will be allocated to the next transaction/invoice
                             * This process is repeated until all the invoices are allocated or until when we do not have
                             * cash to allocate
                             * If the money availabe is less than the current invoice then we just allocate the available cash
                             * only and still note the pending balance for the invoice which may be settled when the cash is available
                             */
                            if(amtBalance>=tx.getBalance()) 
                            {
                                amtAllocated=tx.getBalance();
                                amtBalance=amtBalance-amtAllocated;
                                tx.setAllocation(amtAllocated);
                                System.out.println("Alloted Amount: "+ amtAllocated);
                                System.out.println("Balance Amount: "+ amtBalance);
                                tx.setPaid(true);     // mark the invoice as fully paid                                                                  
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
                            InvoiceList.getAllocatedList();
                            ok=true;
                    }                        
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Select the Payment Method first... ");
            
        }
        return ok;
    }
    private void updateCustomerDassboard()
    {
        double totalSales=ss.getTotalSalesByCustomerId(CustomerListTable.selectedCustomer.getCustomer_id());
        double totalCash=ss.getTotalCashByCustomerId(CustomerListTable.selectedCustomer.getCustomer_id());
        double lastPayment=ss.getLastCustomerPayment(CustomerListTable.selectedCustomer);
        double balance=totalSales-totalCash;
        CustomerDashboard.txt_totalSales.setText(CustomerDashboard.df.format(totalSales));
        if (balance>=0)
        {
            CustomerDashboard.txt_balance.setText(CustomerDashboard.df.format(balance));
        }
        else
        {                        
            CustomerDashboard.txt_balance.setText("("+CustomerDashboard.df.format(balance)+")");  
        }
        CustomerDashboard.txt_lastPayment.setText(CustomerDashboard.df.format(lastPayment));
    }
    
    private void testCashReceipt(List<Cash> cashTx, List<SalesTransaction> tx)
    {
        if(!cashTx.isEmpty())
        {
           
            for(Cash cash:cashTx)
            {
                System.out.println(cash.getDate()+"\t"+cash.getAmount()+"\t"+cash.getPrepayment());
            }
        }
        
        for(SalesTransaction s:tx)
        {
            System.out.println(s.getTxSalesDate()+"\t"+s.isPaid());
        }
        
    }
    
    public static void createAndShowGUI()
     {
         
         AccountsManagement.logger.info("Loading Receive Payment Dialog");              
         ReceivePaymentDialog receivePayment = new ReceivePaymentDialog();              
     }
}
