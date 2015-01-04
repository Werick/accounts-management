/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Calendar;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXDatePicker;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.ReportDescriptor;
import org.lown.consultancy.accounts.dao.ReportsDAO;
import org.lown.consultancy.accounts.dao.SalesDAO;
import org.lown.consultancy.accounts.tables.CustomerListTable;
import org.lown.consultancy.accounts.tables.ReportStatementTable;
import org.lown.consultancy.accounts.tables.TransactionsTable;

/**
 *
 * @author LENOVO USER
 */
public class CustomerDashboard extends JPanel implements ActionListener{
    private static final String ACT_NEW="new_transaction";
    private static final String ACT_FILTER="filter_transaction";
    private static final String ACT_DATERANGE="filter_by_date";
    private static final String ACT_PAY="receive_pay";
    private static final String ACT_CREATE="add_customer";
    private static final String ACT_EDIT="update_customer";
    private static final String ACT_REPORT="Print_Statement";
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_customer";
    private static final String ACT_HIDE="hide_customer";
    private static final String ACT_EXPORT="exportReport";
    
    private static final String filter[]={"Select","Last 7 days","Last 30 days","Last 60 days","Last 90 days","Date Range","All"};
    
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 16);
    public static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
    
    private JLabel lbl_customerName;
    private JLabel lbl_customerNumber;
    private JLabel lbl_title;
    private JLabel lbl_address;
    private JLabel lbl_phone;
    private JLabel lbl_contact;
    private JLabel lbl_Search;
    private JLabel lbl_balance;
    private JLabel lbl_alert;
    private JLabel lbl_filter;
    private JLabel lbl_startDate;
    private JLabel lbl_endDate;
    private JLabel lbl_totalSales;
    private JLabel lbl_status;
    private JLabel lbl_lastPayment;
    
    private JComboBox cbo_filter;
    
    private JXDatePicker dp_startDate;
    private JXDatePicker dp_endDate;
    
    public static JTextField txt_lastPayment;
    public static JTextField txt_balance;
    public static JTextField txt_totalSales;
    public static JTextField txt_Search;
    public static JTextField txt_customerName;
    public static JTextField txt_customerNumber;
    public static JTextField txt_address;
    public static JTextField txt_phone;
    public static JTextField txt_contact;
    
    private JButton btnSale;
    private JButton btnUpdate;
    private JButton btnClose;
    private JButton btnStatement;
    private JButton btnEdit;
    private JButton btnHide;
    private JButton btnCreate;
    private JButton btnExport;
    
    private TitledBorder titled = new TitledBorder("Find/Create Customer (s)");
    private TitledBorder titled1 = new TitledBorder("Customer Statement");
    private TitledBorder titled2 = new TitledBorder("Customer Information");
    private TitledBorder titled3 = new TitledBorder("Customer Transactions");
    
    private static JDialog dlgCustomerDashboard;
    private JButton btnView;
     private JButton btnGo;
            
    private JPanel pCustomer;
    private JPanel pFind_Create;
    private JPanel pTransactions;
    private JPanel pStatement;
    private CustomerListTable customerListTable;
    private ReportStatementTable statementTable;
    public static TransactionsTable txListTable;
    
    public static DecimalFormat df = new DecimalFormat("#0.00");
    double totalSales;
    double totalCash;
    public static double balance;
    
    SalesDAO ss;
    ReportsDAO reportDao;
    List<ReportDescriptor> customerReport;
    
    
    public CustomerDashboard()
    {
        totalSales=0.0;
        balance=0.0;
        totalCash=0.0;
        ss=new SalesDAO();
        CustomerListTable.selectedCustomer=null; 
        //TransactionsTable.selectedInvoice=null;
        
        dlgCustomerDashboard= new JDialog((JDialog)null, "Customer Center", true);
        dlgCustomerDashboard.setLayout(null);
        dlgCustomerDashboard.setSize(1200, 700);//Width size, Height size
        dlgCustomerDashboard.setLocationRelativeTo(null);//center the invoice on the screen
        
        titled2.setTitleFont(title2Font);
        titled3.setTitleFont(title2Font);
        titled.setTitleFont(title2Font);
        
        pTransactions=new JPanel();
        pTransactions.setBounds(600, 230, 570, 400);
        pTransactions.setBorder(titled3);  
        pTransactions.setLayout(null);
        dlgCustomerDashboard.add(pTransactions);
        
        txListTable=new TransactionsTable();
        txListTable.setBounds(20,80,520, 300);        
        pTransactions.add(txListTable);
        
        lbl_filter=new JLabel();
        lbl_filter.setBounds(20, 20, 100, 20);         
        lbl_filter.setText("Select Filter:");
        lbl_filter.setFont(title3Font);     
        pTransactions.add(lbl_filter);
        
        cbo_filter=new JComboBox(filter);
        cbo_filter.setBounds(20, 50, 150, 20);         
        cbo_filter.setActionCommand(ACT_FILTER);
        cbo_filter.addActionListener(this);
        cbo_filter.setFont(title3Font);     
        pTransactions.add(cbo_filter);
        
        lbl_startDate=new JLabel();
        lbl_startDate.setBounds(200, 20, 100, 20);         
        lbl_startDate.setText("Start Date:");
        lbl_startDate.setFont(title3Font);     
        pTransactions.add(lbl_startDate);
        
        dp_startDate=new JXDatePicker();
        dp_startDate.setDate(new Date());
        dp_startDate.setFormats(new String[] { "dd-MMM-yyyy" });
        dp_startDate.setBounds(200, 50, 130, 20);
        dp_startDate.setEnabled(false);
        pTransactions.add(dp_startDate);
        
        lbl_endDate=new JLabel();
        lbl_endDate.setBounds(350, 20, 100, 20);         
        lbl_endDate.setText("End Date:");
        lbl_endDate.setFont(title3Font);     
        pTransactions.add(lbl_endDate);
        
        dp_endDate=new JXDatePicker();
        dp_endDate.setDate(new Date());
        dp_endDate.setFormats(new String[] { "dd-MMM-yyyy" });
        dp_endDate.setBounds(350, 50, 130, 20);
        dp_endDate.setEnabled(false);        
        pTransactions.add(dp_endDate);
        
        btnGo=new JButton("Go");
        btnGo.setBounds(500, 50, 50, 20);
        btnGo.setActionCommand(ACT_DATERANGE);
        btnGo.setEnabled(false);
        btnGo.addActionListener(this);
        pTransactions.add(btnGo);   
        
        pFind_Create=new JPanel();
        pFind_Create.setBounds(10, 10, 570, 400);
        pFind_Create.setBorder(titled);  
        pFind_Create.setLayout(null);
        dlgCustomerDashboard.add(pFind_Create);
        
        btnView=new JButton("View Customer");
        btnView.setBounds(200, 370, 150, 35); 
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this); 
        btnView.setToolTipText("Click to View customer/company Dashboard.");
        //pFind_Create.add(btnView);
       
        
        btnCreate=new JButton("Create Customer");
        btnCreate.setBounds(395, 40, 135, 25); 
        btnCreate.setActionCommand(ACT_CREATE);
        btnCreate.addActionListener(this);  
        btnCreate.setToolTipText("Click to Create/Add a new customer/company.");       
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pFind_Create.add(btnCreate);
       
        lbl_Search=new JLabel();
        lbl_Search.setBounds(10, 40, 200, 20);         
        lbl_Search.setText("Enter Name or Number:");
        lbl_Search.setFont(title3Font);     
        pFind_Create.add(lbl_Search);
        
        txt_Search=new JTextField();
        txt_Search.setBounds(180, 40, 200, 25);         
        txt_Search.setText("");         
        txt_Search.setToolTipText("Enter atleast three characters to Search.");
        txt_Search.setFont(title3Font);
        /*
         * Add a document Listener method to track changes in the text box and fire th search event
         */
        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            public void doSearch() 
            {
                if (txt_Search.getText().length()>=3)
                {
                    //Call the search method
                    customerListTable.insertRow(txt_Search.getText());
                }
            }
           
        });
        pFind_Create.add(txt_Search);
        
        customerListTable=new CustomerListTable();
        customerListTable.setBounds(20,80,520, 300);        
        pFind_Create.add(customerListTable);
        
        pCustomer=new JPanel();
        pCustomer.setBounds(600, 10, 570, 200);
        pCustomer.setBorder(titled2);  
        pCustomer.setLayout(null);
        dlgCustomerDashboard.add(pCustomer);
        
        lbl_title=new JLabel();
        lbl_title.setBounds(20, 10, 300, 25);         
        lbl_title.setText("Customer Center");    
        lbl_title.setFont(MainMenu.titleFont);
        dlgCustomerDashboard.add(lbl_title); 
        
        lbl_customerNumber=new JLabel();
        lbl_customerNumber.setBounds(10, 20, 100, 25);         
        lbl_customerNumber.setText("Number:"); 
        lbl_customerNumber.setFont(title2Font);
        pCustomer.add(lbl_customerNumber);
        
        txt_customerNumber=new JTextField();
        txt_customerNumber.setBounds(100, 20, 150, 25);         
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
        txt_customerName.setBounds(100, 50, 200, 25);         
        txt_customerName.setText("");    
        txt_customerName.setFont(title2Font);
        txt_customerName.setEditable(false);
        pCustomer.add(txt_customerName);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(330, 20, 100, 25);         
        lbl_address.setText("Address:"); 
        lbl_address.setFont(title2Font);
        pCustomer.add(lbl_address);
        
        txt_address=new JTextField();
        txt_address.setBounds(400, 20, 150, 25);         
        txt_address.setText("");     
        txt_address.setEditable(false);
        txt_address.setFont(title2Font);
        pCustomer.add(txt_address);
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(330, 50, 100, 25);         
        lbl_phone.setText("Phone#:"); 
        lbl_phone.setFont(title2Font);
        pCustomer.add(lbl_phone);
        
        txt_phone=new JTextField();
        txt_phone.setBounds(400, 50, 150, 25);         
        txt_phone.setText("");    
        txt_phone.setFont(title2Font);
        txt_phone.setEditable(false);
        pCustomer.add(txt_phone);
        
        lbl_contact=new JLabel();
        lbl_contact.setBounds(10, 80, 150, 25);         
        lbl_contact.setText("Person:"); 
        lbl_contact.setFont(title2Font);
        pCustomer.add(lbl_contact);
        
        txt_contact=new JTextField();
        txt_contact.setBounds(100, 80, 150, 25);         
        txt_contact.setText("");    
        txt_contact.setFont(title2Font);
        txt_contact.setEditable(false);
        pCustomer.add(txt_contact);
        
        lbl_status=new JLabel();
        lbl_status.setBounds(10, 110, 150, 25);         
        lbl_status.setText("Status: "); 
        lbl_status.setFont(title2Font);
        pCustomer.add(lbl_status);
        
        
        lbl_balance=new JLabel();
        lbl_balance.setBounds(250, 110, 150, 25);         
        lbl_balance.setText("OutStanding Balance:"); 
        lbl_balance.setFont(title2Font);
        pCustomer.add(lbl_balance);
        
        txt_balance=new JTextField();
        txt_balance.setBounds(400, 110, 150, 25);         
        txt_balance.setText("");    
        txt_balance.setFont(title2Font);
        txt_balance.setEditable(false);
        txt_balance.setHorizontalAlignment(JTextField.RIGHT);
        pCustomer.add(txt_balance);
        
        lbl_lastPayment=new JLabel();
        lbl_lastPayment.setBounds(250, 140, 150, 25);         
        lbl_lastPayment.setText("Last Payment:"); 
        lbl_lastPayment.setFont(title2Font);
        pCustomer.add(lbl_lastPayment);
        
        txt_lastPayment=new JTextField();
        txt_lastPayment.setBounds(400, 140, 150, 25);         
        txt_lastPayment.setText("");    
        txt_lastPayment.setFont(title2Font);
        txt_lastPayment.setEditable(false);
        txt_lastPayment.setHorizontalAlignment(JTextField.RIGHT);
        pCustomer.add(txt_lastPayment);
        
        lbl_alert=new JLabel();
        lbl_alert.setBounds(10, 110, 150, 25);         
        lbl_alert.setText(""); 
        lbl_alert.setFont(title2Font);
        pCustomer.add(lbl_alert);
        
        lbl_totalSales=new JLabel();
        lbl_totalSales.setBounds(330, 80, 100, 20);         
        lbl_totalSales.setText("Sales:");
        lbl_totalSales.setFont(title3Font);          
        pCustomer.add(lbl_totalSales);
        
        txt_totalSales=new JTextField();
        txt_totalSales.setBounds(400, 80, 150, 25);         
        txt_totalSales.setText("");    
        txt_totalSales.setFont(title2Font);
        txt_totalSales.setEditable(false);
        txt_totalSales.setHorizontalAlignment(JTextField.RIGHT);
        pCustomer.add(txt_totalSales);
        
        btnEdit=new JButton("Edit");
        btnEdit.setBounds(260, 20, 60, 25);
        btnEdit.setActionCommand(ACT_EDIT);
        btnEdit.addActionListener(this);
        btnEdit.setToolTipText("Click to Edit/Update customer/company info");       
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pCustomer.add(btnEdit);       
        
        
        btnSale=new JButton("Sales Transaction");
        btnSale.setBounds(10, 450, 200, 50);
        btnSale.setActionCommand(ACT_NEW);
        btnSale.addActionListener(this);
        btnSale.setToolTipText("Click to add New Sales");       
        btnSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgCustomerDashboard.add(btnSale);
        
        btnUpdate=new JButton("Receive Payment");
        btnUpdate.setBounds(300, 450, 200, 50);
        btnUpdate.setActionCommand(ACT_PAY);
        btnUpdate.addActionListener(this);
        btnUpdate.setToolTipText("Click to Receive Payments from the selected Customer/Company");       
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgCustomerDashboard.add(btnUpdate);
        
        btnStatement=new JButton("Print Statement");
        btnStatement.setBounds(10, 550, 200, 50);
        btnStatement.setActionCommand(ACT_REPORT);
        btnStatement.addActionListener(this);      
        btnStatement.setToolTipText("Click to View the Statement of Account for the selected Customer/Company");       
        btnStatement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgCustomerDashboard.add(btnStatement);
        
        btnClose=new JButton("Close");
        btnClose.setBounds(300, 550, 200, 50);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        btnClose.setToolTipText("Click to go back to the Home/Main Menu Window");       
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgCustomerDashboard.add(btnClose);
        
        //customer statement's penel
        
        pStatement=new JPanel();
        pStatement.setBounds(600, 230, 570, 400);
        pStatement.setBorder(titled1);  
        pStatement.setVisible(false);
        pStatement.setLayout(null);
        dlgCustomerDashboard.add(pStatement);
        
        statementTable= new ReportStatementTable();
        statementTable.setBounds(20,80,520, 300);        
        pStatement.add(statementTable);
        
        btnHide=new JButton("Hide");
        btnHide.setBounds(380, 30, 150, 35);
        btnHide.setActionCommand(ACT_HIDE);
        btnHide.addActionListener(this);
        pStatement.add(btnHide);
        
        btnExport=new JButton("Export to Excel");
        btnExport.setBounds(20, 30, 150, 35);
        btnExport.setActionCommand(ACT_EXPORT);
        btnExport.addActionListener(this);
        pStatement.add(btnExport);
        
        dlgCustomerDashboard.setVisible(true);          
        dlgCustomerDashboard.dispose(); //close the app once done
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgCustomerDashboard.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_DATERANGE))
        {
            Date startDate=dp_startDate.getDate();            
            Date endDate=dp_endDate.getDate();
            txListTable.insertRow(CustomerListTable.selectedCustomer, startDate, endDate);
            System.out.println("Start Date: "+startDate+"\t End Date:"+endDate);
	}
        else if(e.getActionCommand().equals(ACT_FILTER))
        {         
            dp_endDate.setEnabled(false);
            dp_startDate.setEnabled(false);
            btnGo.setEnabled(false);
            
            if(cbo_filter.getSelectedItem().equals("Date Range"))
            {
                dp_endDate.setEnabled(true);
                dp_startDate.setEnabled(true);
                btnGo.setEnabled(true);
            }
            else if(cbo_filter.getSelectedItem().equals("Last 7 days"))
            {
                filterTransactions(7);
            }
            else if(cbo_filter.getSelectedItem().equals("Last 30 days"))
            {
                filterTransactions(30);
            }
            else if(cbo_filter.getSelectedItem().equals("Last 60 days"))
            {
                filterTransactions(60);
            }
            else if(cbo_filter.getSelectedItem().equals("Last 90 days"))
            {
                filterTransactions(90);
            }
            else if(cbo_filter.getSelectedItem().equals("All"))
            {
                txListTable.insertRow(CustomerListTable.selectedCustomer);
            }
            
	}
        else if(e.getActionCommand().equals(ACT_PAY))
        {
            if(CustomerListTable.selectedCustomer!=null)
            {                
                
                ReceivePaymentDialog.createAndShowGUI();                
                return;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Customer first, then proceed...");
                return;
            }
	}
       
        else if(e.getActionCommand().equals(ACT_NEW))
        {
            if(CustomerListTable.selectedCustomer!=null)
            {
               TransactionsTable.selectedInvoice=null;
                SalesDialog.createAndShowGUI(CustomerListTable.selectedCustomer);  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Customer first, then proceed...");
                return;
            }
            
        }
        else if(e.getActionCommand().equals(ACT_REPORT))
        {
            if(CustomerListTable.selectedCustomer!=null)
            {
                reportDao=new ReportsDAO();
                customerReport=reportDao.getCustomerReport(CustomerListTable.selectedCustomer);
                for(ReportDescriptor r:customerReport)
                {
                    System.out.println(r.getTransactionDate()+"\t"+r.getDescription()+"\t"+r.getDrAmount()+"\t"+r.getCrAmount()+"\t"+r.getBalance());
                }
                pTransactions.setVisible(false);
                pStatement.setVisible(true);
                statementTable.insertCustomerStatement(CustomerListTable.selectedCustomer);
                btnStatement.setEnabled(false);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No Customer is Selected");
            }
            
            
            return;
        }
        else if(e.getActionCommand().equals(ACT_HIDE))
        {
           
            pStatement.setVisible(false);
            pTransactions.setVisible(true);
            btnStatement.setEnabled(true);
        }
        else if(e.getActionCommand().equals(ACT_EDIT))
        {
            if(CustomerListTable.selectedCustomer!=null)
            {
//               CustomerDialog customerDialog=new CustomerDialog();
               CustomerDialog.createAndShowGUI();
                return;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Customer first, then proceed...");
                return;
            }
             
        }
        else if(e.getActionCommand().equals(ACT_CREATE))
        {
              CustomerListTable.selectedCustomer=null;       
              clearFields();   
              CustomerDialog.createAndShowGUI();
	}        
        
    }
    private void filterTransactions(int days)
    {
        Date startDate;
        Calendar cal;
        Date endDate;
        int d=days;
        endDate=new Date();
        cal = Calendar.getInstance();  
        cal.setTime(endDate);  
        cal.add(Calendar.DATE, -d); // add d days      
        startDate=cal.getTime();;
        txListTable.insertRow(CustomerListTable.selectedCustomer, startDate, endDate);
        System.out.println("Start Date: "+startDate+"\t End Date:"+endDate);
    }
    private void clearFields()
    {
        txt_customerName.setText("");
        txt_customerNumber.setText("");
        txt_phone.setText("");
        txt_address.setText("");
        txt_contact.setText("");                     
        txListTable.deleteRows();              
        txt_totalSales.setText("");                   
        txt_balance.setText("");
        customerListTable.deleteRows();
                    
    }
    
     public static void createAndShowGUI()
     {         
         AccountsManagement.logger.info("Loading CustomerDashboard Dialog");
         CustomerDashboard customerDashboard = new CustomerDashboard();       
     }
    
}
