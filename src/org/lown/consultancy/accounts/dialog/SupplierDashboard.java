/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.lown.consultancy.accounts.dao.PurchasesService;
import org.lown.consultancy.accounts.dao.SalesService;
import org.lown.consultancy.accounts.tables.PurchasesList;
import org.lown.consultancy.accounts.tables.PurchasesTransactions;
import org.lown.consultancy.accounts.tables.SupplierList;

/**
 *
 * @author LENOVO USER
 */
public class SupplierDashboard extends JPanel implements ActionListener{
    private static final String ACT_NEW="new_transaction";
    private static final String ACT_PAY="create_payment";
    private static final String ACT_FILTER="filter";
    private static final String ACT_DATERANGE="filter_by_dates";
    private static final String ACT_CREATE="add_supplier";
    private static final String ACT_EDIT="update_supplier";
    private static final String ACT_DELETE="Print_Statement";
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_supplier";
    private static final Font title2Font = new Font("Times New Roman", Font.BOLD, 14);
    private static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
    private static final String filter[]={"Select","Last 7 days","Last 30 days","Last 60 days","Last 90 days","Date Range","All"};
    
    private JLabel lbl_supplierName;
    private JLabel lbl_supplierNumber;
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
    private JLabel lbl_totalPurchases;
    private JLabel lbl_lastPayment;
    
    private JComboBox cbo_filter;
    
    private JXDatePicker dp_startDate;
    private JXDatePicker dp_endDate;
    
    public static JTextField txt_lastPayment;
    public static JTextField txt_balance;
    public static JTextField txt_totalPurchases;
    private JTextField txt_Search;
    public static JTextField txt_supplierName;
    public static JTextField txt_supplierNumber;
    public static JTextField txt_address;
    public static JTextField txt_phone;
    public static JTextField txt_contact;
    
    private JButton btnPurchase;
    private JButton btnUpdate;
    private JButton btnClose;
    private JButton btnStatement;
    private JButton btnEdit;
    private JButton btnCreate;
    private TitledBorder titled = new TitledBorder("Find/Create Supplier (s)");
    private TitledBorder titled2 = new TitledBorder("Supplier Information");
    private TitledBorder titled3 = new TitledBorder("Supplier Invoices");
    private static JDialog dlgsupplierDashboard;
    private JButton btnView;
    private JButton btnGo;
            
    private JPanel pSupplier;
    private JPanel pFind_Create;
    private JPanel pTransactions;
    private SupplierList supplierListTable;
    public static PurchasesTransactions txListTable;
    
    public static DecimalFormat df = new DecimalFormat("#0.00");
    double totalPurchases;
    double totalPayments;
    public static double balance;
    
    SalesService ss;
    PurchasesService ps;
    
    public SupplierDashboard()
    {
        totalPurchases=0.0;
        balance=0.0;
        totalPayments=0.0;
        ss=new SalesService();
        ps=new PurchasesService();
        
        SupplierList.selectedSupplier=null;
        PurchasesTransactions.selectedInvoice=null;
        
        dlgsupplierDashboard= new JDialog((JDialog)null, "Supplier Center", true);
        dlgsupplierDashboard.setLayout(null);
        dlgsupplierDashboard.setSize(1200, 700);//Width size, Height size
        dlgsupplierDashboard.setLocationRelativeTo(null);//center the invoice on the screen
        
        titled2.setTitleFont(title2Font);
        titled3.setTitleFont(title2Font);
        titled.setTitleFont(title2Font);
        
        pTransactions=new JPanel();
        pTransactions.setBounds(600, 230, 570, 400);
        pTransactions.setBorder(titled3);  
        pTransactions.setLayout(null);
        dlgsupplierDashboard.add(pTransactions);
        
        txListTable=new PurchasesTransactions();
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
        dlgsupplierDashboard.add(pFind_Create);
        
        btnView=new JButton("View Supplier");
        btnView.setBounds(200, 370, 150, 35); 
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this); 
        btnView.setToolTipText("Click to View supplier/company Dashboard.");
        //pFind_Create.add(btnView);
       
        
        btnCreate=new JButton("Create Supplier");
        btnCreate.setBounds(395, 40, 135, 25); 
        btnCreate.setActionCommand(ACT_CREATE);
        btnCreate.addActionListener(this);  
        btnCreate.setToolTipText("Click to Create/Add a new supplier/company.");
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
                    supplierListTable.insertRow(txt_Search.getText());
                }
            }
           
        });
        pFind_Create.add(txt_Search);
        
        supplierListTable=new SupplierList();
        supplierListTable.setBounds(20,80,520, 300);        
        pFind_Create.add(supplierListTable);
        
        pSupplier=new JPanel();
        pSupplier.setBounds(600, 10, 570, 200);
        pSupplier.setBorder(titled2);  
        pSupplier.setLayout(null);
        dlgsupplierDashboard.add(pSupplier);
        
        lbl_title=new JLabel();
        lbl_title.setBounds(20, 10, 300, 25);         
        lbl_title.setText("Supplier Center");    
        lbl_title.setFont(MainMenu.titleFont);
        dlgsupplierDashboard.add(lbl_title); 
        
        lbl_supplierNumber=new JLabel();
        lbl_supplierNumber.setBounds(10, 20, 100, 25);         
        lbl_supplierNumber.setText("Number:"); 
        lbl_supplierNumber.setFont(title2Font);
        pSupplier.add(lbl_supplierNumber);
        
        txt_supplierNumber=new JTextField();
        txt_supplierNumber.setBounds(100, 20, 150, 25);         
        txt_supplierNumber.setText("");     
        txt_supplierNumber.setEditable(false);
        txt_supplierNumber.setFont(title2Font);
        pSupplier.add(txt_supplierNumber);
        
        lbl_supplierName=new JLabel();
        lbl_supplierName.setBounds(10, 50, 100, 25);         
        lbl_supplierName.setText("Name:"); 
        lbl_supplierName.setFont(title2Font);
        pSupplier.add(lbl_supplierName);
        
        txt_supplierName=new JTextField();
        txt_supplierName.setBounds(100, 50, 200, 25);         
        txt_supplierName.setText("");    
        txt_supplierName.setFont(title2Font);
        txt_supplierName.setEditable(false);
        pSupplier.add(txt_supplierName);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(330, 20, 100, 25);         
        lbl_address.setText("Address:"); 
        lbl_address.setFont(title2Font);
        pSupplier.add(lbl_address);
        
        txt_address=new JTextField();
        txt_address.setBounds(400, 20, 150, 25);         
        txt_address.setText("");     
        txt_address.setEditable(false);
        txt_address.setFont(title2Font);
        pSupplier.add(txt_address);
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(330, 50, 100, 25);         
        lbl_phone.setText("Phone#:"); 
        lbl_phone.setFont(title2Font);
        pSupplier.add(lbl_phone);
        
        txt_phone=new JTextField();
        txt_phone.setBounds(400, 50, 150, 25);         
        txt_phone.setText("");    
        txt_phone.setFont(title2Font);
        txt_phone.setEditable(false);
        pSupplier.add(txt_phone);
        
        lbl_contact=new JLabel();
        lbl_contact.setBounds(10, 80, 150, 25);         
        lbl_contact.setText("Person:"); 
        lbl_contact.setFont(title2Font);
        pSupplier.add(lbl_contact);
        
        txt_contact=new JTextField();
        txt_contact.setBounds(100, 80, 150, 25);         
        txt_contact.setText("");    
        txt_contact.setFont(title2Font);
        txt_contact.setEditable(false);
        pSupplier.add(txt_contact);
        
        lbl_balance=new JLabel();
        lbl_balance.setBounds(250, 110, 150, 25);         
        lbl_balance.setText("OutStanding Balance:"); 
        lbl_balance.setFont(title2Font);
        pSupplier.add(lbl_balance);
        
        txt_balance=new JTextField();
        txt_balance.setBounds(400, 110, 150, 25);         
        txt_balance.setText("");    
        txt_balance.setFont(title2Font);
        txt_balance.setEditable(false);
        txt_balance.setHorizontalAlignment(JTextField.RIGHT);
        pSupplier.add(txt_balance);
        
        lbl_lastPayment=new JLabel();
        lbl_lastPayment.setBounds(250, 140, 150, 25);         
        lbl_lastPayment.setText("Last Payment:"); 
        lbl_lastPayment.setFont(title2Font);
        pSupplier.add(lbl_lastPayment);
        
        txt_lastPayment=new JTextField();
        txt_lastPayment.setBounds(400, 140, 150, 25);         
        txt_lastPayment.setText("");    
        txt_lastPayment.setFont(title2Font);
        txt_lastPayment.setEditable(false);
        txt_lastPayment.setHorizontalAlignment(JTextField.RIGHT);
        pSupplier.add(txt_lastPayment);
        
        
        lbl_alert=new JLabel();
        lbl_alert.setBounds(10, 110, 150, 25);         
        lbl_alert.setText(""); 
        lbl_alert.setFont(title2Font);
        pSupplier.add(lbl_alert);
        
        lbl_totalPurchases=new JLabel();
        lbl_totalPurchases.setBounds(330, 80, 100, 20);         
        lbl_totalPurchases.setText("Purchases:");
        lbl_totalPurchases.setFont(title3Font);     
        pSupplier.add(lbl_totalPurchases);
        
        txt_totalPurchases=new JTextField();
        txt_totalPurchases.setBounds(400, 80, 150, 25);         
        txt_totalPurchases.setText("");    
        txt_totalPurchases.setFont(title2Font);
        txt_totalPurchases.setEditable(false);
        txt_totalPurchases.setHorizontalAlignment(JTextField.RIGHT);
        pSupplier.add(txt_totalPurchases);
        
        btnEdit=new JButton("Edit");
        btnEdit.setBounds(260, 20, 60, 25);
        btnEdit.setActionCommand(ACT_EDIT);
        btnEdit.addActionListener(this);
        pSupplier.add(btnEdit);
        
             
        btnPurchase=new JButton("Enter Purchases");
        btnPurchase.setBounds(10, 450, 200, 50);
        btnPurchase.setActionCommand(ACT_NEW);
        btnPurchase.addActionListener(this);
        dlgsupplierDashboard.add(btnPurchase);
        
        btnUpdate=new JButton("Pay Supplier");
        btnUpdate.setBounds(300, 450, 200, 50);
        btnUpdate.setActionCommand(ACT_PAY);
        btnUpdate.addActionListener(this);
        dlgsupplierDashboard.add(btnUpdate);
        
        btnStatement=new JButton("Print Statement");
        btnStatement.setBounds(10, 550, 200, 50);
        btnStatement.setActionCommand(ACT_DELETE);
        btnStatement.addActionListener(this);
        //btnStatement.setEnabled(false);
        dlgsupplierDashboard.add(btnStatement);
        
        btnClose=new JButton("Close");
        btnClose.setBounds(300, 550, 200, 50);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgsupplierDashboard.add(btnClose);

        
        dlgsupplierDashboard.setVisible(true);          
        dlgsupplierDashboard.dispose(); //close the app once done
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            AccountsManagement.logger.info("Closing Supplier Dashboard Dialog"); 
            dlgsupplierDashboard.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_DATERANGE))
        {
            Date startDate=dp_startDate.getDate();            
            Date endDate=dp_endDate.getDate();
            txListTable.insertRow(SupplierList.selectedSupplier, startDate, endDate);
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
                txListTable.insertRow(SupplierList.selectedSupplier);
            }
            
	}
        else if(e.getActionCommand().equals(ACT_PAY))
        {
            if(SupplierList.selectedSupplier!=null)
            {
//               CustomerDialog customerDialog=new CustomerDialog();
                PaySupplierDialog.createAndShowGUI();
                return;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Supplier first, then proceed...");
                return;
            }
	}
       
        else if(e.getActionCommand().equals(ACT_NEW))
        {
            if(SupplierList.selectedSupplier!=null)
            {
                PurchasesTransactions.selectedInvoice=null;
                PurchasesDialog.createAndShowGUI(SupplierList.selectedSupplier);  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Supplier first, then proceed...");
                return;
            }
            
        }
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            
        }
        else if(e.getActionCommand().equals(ACT_EDIT))
        {
            if(SupplierList.selectedSupplier!=null)            {
             
                SupplierDialog.createAndShowGUI();
                return;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select a Supplier first, then proceed...");
                return;
            }
             
        }
        else if(e.getActionCommand().equals(ACT_CREATE))
        {
              SupplierList.selectedSupplier=null; 
              clearText();
              
              //load registration form              
              SupplierDialog.createAndShowGUI();
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
        txListTable.insertRow(SupplierList.selectedSupplier, startDate, endDate);
        System.out.println("Start Date: "+startDate+"\t End Date:"+endDate);
    }
    
   
    
    private void clearText()
    {
        txt_supplierName.setText("");
        txt_supplierNumber.setText("");
        txt_phone.setText("");
        txt_address.setText("");
        txt_contact.setText("");
        txt_totalPurchases.setText("");
        txt_balance.setText("");
        supplierListTable.deleteRows();
        txListTable.deleteRows();
    }
     public static void createAndShowGUI()
     {
         
         AccountsManagement.logger.info("Loading CustomerDashboard Dialog");
         SupplierDashboard supplierDashboard = new SupplierDashboard();
         
         
     }
}
