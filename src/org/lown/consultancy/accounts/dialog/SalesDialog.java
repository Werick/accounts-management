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
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.SalesItem;
import org.lown.consultancy.accounts.SalesTransaction;
import org.lown.consultancy.accounts.dao.InvoicePrinter;
import org.lown.consultancy.accounts.dao.ProductDAO;
import org.lown.consultancy.accounts.dao.SalesDAO;
import org.lown.consultancy.accounts.tables.CustomerListTable;
import org.lown.consultancy.accounts.tables.ItemListTable;
import org.lown.consultancy.accounts.tables.TransactionsTable;

/**
 *
 * @author LENOVO USER
 */
public class SalesDialog extends JPanel implements ActionListener{
    
    //Action Listeners constants
    public static final String ACT_ADD="Add_Transaction";
    public static final String ACT_DISCOUNT="clear_discount";
    public static final String ACT_PRINT="print_invoice_receipt";
    public static final String ACT_SAVE="post_transaction";
    public static final String ACT_DELETE="remove_salesItem";
    public static final String ACT_CANCEL="cancel";
    public static final String ACT_BACK="exit";
    public static final String ACT_PAYTERM="cash_credit";
    public static final String ACT_FIND="filter_product";
    
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font title1Font = new Font("Times New Roman", Font.BOLD, 18);
    public static final Font title2Font = new Font("Times New Roman", Font.ITALIC, 14);
    //public static final String ACT_FIND="filter_product";
    
    //Display panel
    private static JDialog dlgTransaction;
    //Sections Panels
    JPanel pTransaction, pPayTerms,pShipTo,pHeader;
    
    //Invoice Details Panel
    private JLabel lbl_txDate;
    private JLabel lbl_title2;
    private JLabel lbl_phone;
    private JLabel lbl_curPrice;
    private JLabel lbl_customerId;
    private JLabel lbl_salesRep;
    private JLabel lbl_title;
    private JLabel lbl_pin;
    private JLabel lbl_total;
    private JLabel lbl_payment;
    private JLabel lbl_balance; 
    private JLabel lbl_custName;
    private JLabel lbl_address;
    private JLabel lbl_txQty;
    private JLabel lbl_txCategory;
    private JLabel lbl_txDes;
    private JLabel lbl_txDiscount;
    private JLabel lbl_txCash;
    private JLabel lbl_txDueDate;
    private JLabel lbl_days;
    private JLabel lbl_paymentTerms;
    private JLabel lbl_discount;
    private JLabel lbl_changeDue;
    private JLabel lbl_txQtyAvailable;
    
    private JTextField txt_txCash;
    private JTextField txt_txDiscount;
    private JTextField txt_txQtyAvailable;    
    private JTextField txt_txQty;    
    private JTextField txt_customerId;
    private JTextField txt_total;
    private JTextField txt_days;
    private JTextField txt_payment;
    private JTextField txt_balance;
    private JTextField txt_changeDue;
    private JTextField txt_discount;
    private JTextField txt_curPrice;
    private JComboBox cbo_salesRep;
    private JComboBox cbo_paymentTerms;
    private JComboBox cbo_productCategory;
    private JComboBox cbo_product;
    private JXDatePicker txDatePicker;
    private JXDatePicker txDueDatePicker;
   
    private static Customer selectedCustomer=null;
    private ItemListTable itemListTable;
    
    
    //Buttons
    private JButton btnAdd;
    private JButton btnBack;
    private JButton btnPostTx;
    private JButton btnPrintInvoice;
    private JButton btnDelete;
    
    private Map<String,Integer>categoryList;
    private Map<String,Integer>productList;
    private Map<String,Integer>salesRepList;
    private ProductDAO productService; 
    private SalesDAO salesService; 
    private SalesItem salesItem;
    private double totalAmount;
    private double totalDiscount;
    private DecimalFormat df = new DecimalFormat("#0.00"); 
    private String payTerms[]={"Cash","Credit","Select"};
    private boolean txPosted;
    private TitledBorder customerTitle = new TitledBorder("Bill To: ");
    private TitledBorder termsTitle = new TitledBorder("Payment Terms: ");
    private TitledBorder txTitle = new TitledBorder("Sales Information: ");
    public SalesDialog(Customer customer)
    {
        
        AccountsManagement.logger.info("Creating Sales UI...");
        totalAmount=0.0;
        totalDiscount=0.0;
        txPosted=false;
        setSelectedCustomer(customer);
        dlgTransaction= new JDialog((JDialog)null, "SALES TRANSACTION", true);
        dlgTransaction.setLayout(null);
        dlgTransaction.setSize(950, 750);
        dlgTransaction.setLocationRelativeTo(null);//center the Sales tx on the screen
        
        productService=new ProductDAO();
        categoryList=productService.getCategoryMap();
        
        salesService=new SalesDAO();
        salesRepList=salesService.getSalesRepMap();
        
        Map companyDetails=MainMenu.cs.getCompanyDetails();
        
        //Create Invoice Details Panel
        pTransaction=new JPanel();
        pTransaction.setBounds(500, 10, 420, 180);
        pTransaction.setLayout(null);
        pTransaction.setBorder(txTitle);
        
        lbl_txDate=new JLabel();
        lbl_txDate.setBounds(20, 20, 150, 20);         
        lbl_txDate.setText("Transaction Date:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_txDate);
        
        txDatePicker=new JXDatePicker();
        txDatePicker.setDate(new Date());
        txDatePicker.setFormats(new String[] { "dd-MMM-yyyy" });
        txDatePicker.setBounds(170, 20, 150, 20);
        pTransaction.add(txDatePicker);
        
//        lbl_txNumber=new JLabel();
//        lbl_txNumber.setBounds(0, 35, 150, 20);         
//        lbl_txNumber.setText("Transaction Number:");
//        //lbl_title.setFont(MainMenu.titleFont);         
//        //pTransaction.add(lbl_txNumber);
//        
//        /*
//         * The Involce Number shld be changed to Autofill
//         */
//        txt_txNumber=new JTextField();
//        txt_txNumber.setBounds(150, 35, 120, 20);         
//        txt_txNumber.setText("");
//        //lbl_title.setFont(MainMenu.titleFont);         
//        //pTransaction.add(txt_txNumber);
        
        
        lbl_customerId=new JLabel();
        lbl_customerId.setBounds(20, 55, 100, 20);         
        lbl_customerId.setText("Customer ID:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_customerId);        
       
        txt_customerId=new JTextField();
        txt_customerId.setBounds(170, 55, 150, 20);         
        txt_customerId.setText(selectedCustomer.getCustomerNumber());
        txt_customerId.setEditable(false);
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(txt_customerId);
        
        lbl_salesRep=new JLabel();
        lbl_salesRep.setBounds(20, 85, 100, 20);         
        lbl_salesRep.setText("Rep ID:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_salesRep);   
        
        cbo_salesRep=new JComboBox(salesRepList.keySet().toArray());
        cbo_salesRep.setBounds(170, 85, 200, 20);              
        pTransaction.add(cbo_salesRep);
        cbo_salesRep.addItem("Select");
        cbo_salesRep.setSelectedItem("Select");
        
        lbl_txDueDate=new JLabel();
        lbl_txDueDate.setBounds(20, 120, 150, 20);         
        lbl_txDueDate.setText("Invoice Due Date:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_txDueDate); 
        
        txDueDatePicker=new JXDatePicker();
        txDueDatePicker.setDate(new Date());
        txDueDatePicker.setFormats(new String[] { "dd-MMM-yyyy" });
        txDueDatePicker.setBounds(170, 120, 150, 20);
        txDueDatePicker.setEditable(false);
        pTransaction.add(txDueDatePicker);
        
        dlgTransaction.add(pTransaction);//add the invoice details panel
        
        String title=(String)companyDetails.get("company.name");
        String title2=(String)companyDetails.get("company.vision");
        String pin="PIN: "+(String)companyDetails.get("company.pin");
        lbl_title=new JLabel();
        lbl_title.setBounds(10, 20, 300, 20);         
        lbl_title.setText(title);  
        lbl_title.setFont(title1Font);
        
        lbl_title2=new JLabel();
        lbl_title2.setBounds(10, 55, 300, 20);         
        lbl_title2.setText(title2);  
        lbl_title2.setFont(title2Font);
        
        lbl_pin=new JLabel();
        lbl_pin.setBounds(10, 80, 300, 20);         
        lbl_pin.setText(pin);  
        lbl_pin.setFont(title2Font);
        //pHeader.add(lbl_title);
        
        lbl_custName=new JLabel();
        lbl_custName.setBounds(20, 25, 300, 20);         
        lbl_custName.setText("Name: "+selectedCustomer.getCustomerName());
        lbl_custName.setFont(titleFont);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(20, 50, 300, 20);         
        lbl_address.setText("Address: "+ selectedCustomer.getAddress()+";");  
        lbl_address.setFont(titleFont); 
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(20, 75, 300, 20);         
        lbl_phone.setText("Phone: "+selectedCustomer.getPhone()+";");  
        lbl_phone.setFont(titleFont); 
        
        
         //Create Invoice Payment terms Panel
        pPayTerms=new JPanel();
        pPayTerms.setBounds(10, 150, 400, 100);
        pPayTerms.setLayout(null);
        pPayTerms.setBorder(termsTitle);
        dlgTransaction.add(pPayTerms);
        
        lbl_paymentTerms=new JLabel();
        lbl_paymentTerms.setBounds(20, 20, 150, 20);         
        lbl_paymentTerms.setText("Payment Method: ");
        lbl_paymentTerms.setFont(titleFont);
        pPayTerms.add(lbl_paymentTerms);
        
        cbo_paymentTerms=new JComboBox(payTerms);
        cbo_paymentTerms.setBounds(150, 20, 100, 20); 
        cbo_paymentTerms.setSelectedIndex(2);
        cbo_paymentTerms.setActionCommand(ACT_PAYTERM);
        cbo_paymentTerms.addActionListener(this);
        AutoCompleteDecorator.decorate(this.cbo_paymentTerms);
        //System.out.println("Is editable - " + this.cbo_productCategory.isEditable() + ". Surprise!");
        pPayTerms.add(cbo_paymentTerms);
        
        lbl_days=new JLabel();
        lbl_days.setBounds(20, 50, 100, 20);         
        lbl_days.setText("DAYS: ");
        lbl_days.setFont(titleFont);
        pPayTerms.add(lbl_days);
        
        txt_days=new JTextField();
        txt_days.setBounds(150, 50, 100, 20);         
        txt_days.setText("");
        txt_days.setEditable(false);
        txt_days.setHorizontalAlignment(JTextField.RIGHT);
        /*
         * Add a document Listener method to track changes in the text box and calls a calculate method
         */
        txt_days.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                calculateDueDate();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                calculateDueDate();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                calculateDueDate();
            }
            
            public void calculateDueDate() 
            {
                if(txt_days.getText().length()>0)
                {
                    int d=Integer.parseInt(txt_days.getText());
                    Date transDate=txDatePicker.getDate();
                    Calendar cal = Calendar.getInstance();  
                    cal.setTime(transDate);  
                    cal.add(Calendar.DATE, d); // add d days      
                    Date dueDate=cal.getTime();;
                    txDueDatePicker.setDate(dueDate);
                    
                }
                else
                {
                    txDueDatePicker.setDate(new Date());
                }
            }
           
        });
        pPayTerms.add(txt_days);
        
        lbl_txCash=new JLabel();
        lbl_txCash.setBounds(50, 615, 200, 20);         
        lbl_txCash.setText("CASH PAYMENT (KSH): ");
        lbl_txCash.setFont(titleFont);
        
        txt_txCash=new JTextField();
        txt_txCash.setBounds(255, 615, 135, 20);         
        txt_txCash.setText("");
        txt_txCash.setHorizontalAlignment(JTextField.RIGHT);
        /*
         * Add a document Listener method to track changes in the text box and calls a calculate method
         */
        txt_txCash.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                calculateBalance();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                calculateBalance();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                calculateBalance();
            }
            
            public void calculateBalance() 
            {
                double dblCashAmount=0.0;
                if (txt_txCash.getText().length()>0)
                {                   
                    
                    dblCashAmount=0;
                    if(isNumeric(txt_txCash.getText()) )
                    {
                        dblCashAmount=Double.parseDouble(txt_txCash.getText());
                    }
                    
                    txt_payment.setText(df.format(dblCashAmount));
                    double dblBalance=totalAmount-dblCashAmount;
                    txt_balance.setText(df.format(dblBalance));
                    
                    if (cbo_paymentTerms.getSelectedIndex()==0)
                    {
                        if(dblCashAmount>totalAmount)
                        {
                            txt_changeDue.setText(df.format(dblCashAmount-totalAmount));
                        }
                    }
                }
                else
                {
                    txt_payment.setText(df.format(dblCashAmount));
                    txt_balance.setText(df.format(totalAmount));
                }
            }
           
        });
        //txt_txCash.setFont(titleFont);
        
        lbl_discount=new JLabel();
        lbl_discount.setBounds(50, 675, 200, 20);         
        lbl_discount.setText("TOTAL DISCOUNT (KSH): ");
        lbl_discount.setFont(titleFont);
        
        txt_discount=new JTextField();
        txt_discount.setBounds(255, 675, 135, 20);         
        txt_discount.setText("");
        txt_discount.setEditable(false);
        txt_discount.setFont(titleFont);
        txt_discount.setHorizontalAlignment(JTextField.RIGHT);
        
        lbl_changeDue=new JLabel();
        lbl_changeDue.setBounds(50, 645, 200, 20);         
        lbl_changeDue.setText("CHANGE DUE (KSH): ");
        lbl_changeDue.setFont(titleFont);
        
        txt_changeDue=new JTextField();
        txt_changeDue.setBounds(255, 645, 135, 20);         
        txt_changeDue.setText("");
        txt_changeDue.setFont(titleFont);
        txt_changeDue.setHorizontalAlignment(JTextField.RIGHT);
        txt_changeDue.setEditable(false);
        
         //Create Invoice Header Panel      
        
        pHeader=new JPanel();
        pHeader.setBounds(10, 10, 400, 120);
        pHeader.setLayout(null);
        pHeader.setBorder(customerTitle);
        dlgTransaction.add(pHeader);
        
        pHeader.add(lbl_custName);
        pHeader.add(lbl_address);
        pHeader.add(lbl_phone);
       
        
        dlgTransaction.add(pHeader);     
        dlgTransaction.add(pPayTerms);         
        dlgTransaction.add(lbl_txCash);//add the invoice Header panel
        dlgTransaction.add(txt_txCash);//add the invoice Header panel
        dlgTransaction.add(lbl_changeDue);//add the invoice Header panel
        dlgTransaction.add(txt_changeDue);//add the invoice Header panel
        dlgTransaction.add(lbl_discount);//add the invoice Header panel
        dlgTransaction.add(txt_discount);//add the invoice Header panel
        
        
        
        //Item List Table
        itemListTable=new ItemListTable();
        itemListTable.setBounds(35,320,700, 280);
        dlgTransaction.add(itemListTable);
        
        lbl_total=new JLabel();
        lbl_total.setBounds(450, 615, 150, 20);         
        lbl_total.setText("TOTAL:");  
        lbl_total.setFont(titleFont);
        dlgTransaction.add(lbl_total);
        
        txt_total=new JTextField();
        txt_total.setBounds(595, 615, 150, 20);         
        txt_total.setText("");
        txt_total.setFont(titleFont);
        txt_total.setEditable(false);
        txt_total.setHorizontalAlignment(JTextField.RIGHT);
        dlgTransaction.add(txt_total);
        
        lbl_payment=new JLabel();
        lbl_payment.setBounds(450, 645, 150, 20);         
        lbl_payment.setText("PAYMENTS:");  
        lbl_payment.setFont(titleFont);
        dlgTransaction.add(lbl_payment);
        
        txt_payment=new JTextField();
        txt_payment.setBounds(595, 645, 150, 20);         
        txt_payment.setText("");
        txt_payment.setFont(titleFont);   
        txt_payment.setEditable(false);
        txt_payment.setHorizontalAlignment(JTextField.RIGHT);
        dlgTransaction.add(txt_payment);
        
        lbl_balance=new JLabel();
        lbl_balance.setBounds(450, 675, 150, 20);         
        lbl_balance.setText("BALANCE DUE:");  
        lbl_balance.setFont(titleFont);
        dlgTransaction.add(lbl_balance);
        
        txt_balance=new JTextField();
        txt_balance.setBounds(595, 675, 150, 20);         
        txt_balance.setText("");
        txt_balance.setFont(titleFont);  
        txt_balance.setHorizontalAlignment(JTextField.RIGHT);
        txt_balance.setEditable(false);
        
        dlgTransaction.add(txt_balance);
        
       
        btnPostTx=new JButton("Post Transaction");
        btnPostTx.setBounds(750, 400, 150, 45);  //615
        btnPostTx.setActionCommand(ACT_SAVE);
        btnPostTx.addActionListener(this);
        btnPostTx.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgTransaction.add(btnPostTx);
        
        btnPrintInvoice=new JButton("Cancel Transaction");
        btnPrintInvoice.setBounds(750, 460, 150, 45); //645
        btnPrintInvoice.setActionCommand(ACT_PRINT);
        btnPrintInvoice.addActionListener(this);
        btnPrintInvoice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgTransaction.add(btnPrintInvoice);
        
        btnBack=new JButton("Close");
        btnBack.setBounds(750, 520, 150, 45); //675
        btnBack.setActionCommand(ACT_BACK);
        btnBack.addActionListener(this);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgTransaction.add(btnBack);
        
        
        
        lbl_txQty=new JLabel();
        lbl_txQty.setBounds(350, 270, 80, 25);         
        lbl_txQty.setText("Quantity:");       
        dlgTransaction.add(lbl_txQty);
        
        
        txt_txQty=new JTextField();
        txt_txQty.setBounds(350, 295, 50, 20);         
        txt_txQty.setText("");
        //lbl_custName.setFont(titleFont);
        dlgTransaction.add(txt_txQty);
         /*
         * Add a document Listener method to track changes in the text box and calls a calculate method
         */
        txt_txQty.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                calculatePrice();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                calculatePrice();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                calculatePrice();
            }
            
            public void calculatePrice() 
            {
                double dblQty=0.0;
                double dblPrice=0.0;
                if (txt_txQty.getText().length()>0)
                {                   
                    
                    dblQty=0.0;
                    //int selProduct=productList.get(cbo_product.getSelectedItem());
                    //System.out.println(cbo_product.getSelectedItem().toString());                
                    
                    //dblQty=productService.getAvailableQuantity(selProduct);
                    //txt_txQtyAvailable.setText(df.format(dblQty));
                    if(!txt_txQtyAvailable.getText().isEmpty()&& isNumeric(txt_txQtyAvailable.getText()) )
                    {
                        dblQty=Double.parseDouble(txt_txQtyAvailable.getText());
                    }
                    
                    double qty=0.0;
                    if(!txt_txQty.getText().isEmpty()&& isNumeric(txt_txQty.getText()) )
                    {
                        qty=Double.parseDouble(txt_txQty.getText());
                    }
                    if((dblQty==0.0)||(qty>dblQty))
                    {
                        btnAdd.setEnabled(false);
                    }
                    else
                    {
                        btnAdd.setEnabled(true);
                    }
                }                
            }
           
        });
        
        lbl_txQtyAvailable=new JLabel();
        lbl_txQtyAvailable.setBounds(420, 270, 80, 25);         
        lbl_txQtyAvailable.setText("Avail. Qty:");            
        dlgTransaction.add(lbl_txQtyAvailable);
        
        txt_txQtyAvailable=new JTextField();
        txt_txQtyAvailable.setBounds(420, 295, 70, 20);         
        txt_txQtyAvailable.setText("");
        txt_txQtyAvailable.setEditable(false);
        txt_txQty.setHorizontalAlignment(JTextField.RIGHT);
        dlgTransaction.add(txt_txQtyAvailable);
        
        lbl_txCategory=new JLabel();
        lbl_txCategory.setBounds(10, 270, 80, 25);         
        lbl_txCategory.setText("Catgory:");
        //lbl_custName.setFont(titleFont);
        dlgTransaction.add(lbl_txCategory);        
               
        cbo_productCategory=new JComboBox(categoryList.keySet().toArray());
        cbo_productCategory.addItem("Select");
        cbo_productCategory.setSelectedItem("Select");
        cbo_productCategory.setBounds(10, 295, 100, 20);  
        cbo_productCategory.setActionCommand(ACT_FIND);
        cbo_productCategory.addActionListener(this);
        AutoCompleteDecorator.decorate(this.cbo_productCategory);
        //System.out.println("Is editable - " + this.cbo_productCategory.isEditable() + ". Surprise!");
        dlgTransaction.add(cbo_productCategory);
        
        lbl_txDes=new JLabel();
        lbl_txDes.setBounds(130, 270, 200, 25);         
        lbl_txDes.setText("Description:");
        //lbl_custName.setFont(titleFont);
        dlgTransaction.add(lbl_txDes);            
        
        
        cbo_product=new JComboBox();
        cbo_product.setBounds(130, 295, 200, 20);
        cbo_product.setActionCommand(ACT_DISCOUNT);
        cbo_product.addActionListener(this);
        AutoCompleteDecorator.decorate(this.cbo_product);
        //System.out.println("Is editable - " + this.cbo_productCategory.isEditable() + ". Surprise!");
        dlgTransaction.add(cbo_product);
        
        lbl_curPrice=new JLabel();
        lbl_curPrice.setBounds(510, 270, 100, 25);         
        lbl_curPrice.setText("Unit Price:");       
        dlgTransaction.add(lbl_curPrice);
        
        txt_curPrice=new JTextField();
        txt_curPrice.setBounds(510, 295, 70, 20);         
        txt_curPrice.setText("");
        txt_curPrice.setEditable(false);
        txt_curPrice.setHorizontalAlignment(JTextField.RIGHT);
        dlgTransaction.add(txt_curPrice);
        
        lbl_txDiscount=new JLabel();
        lbl_txDiscount.setBounds(600, 270, 100, 25);         
        lbl_txDiscount.setText("Discount:");
        //lbl_custName.setFont(titleFont);
        dlgTransaction.add(lbl_txDiscount);
        
        txt_txDiscount=new JTextField();
        txt_txDiscount.setBounds(600, 295, 70, 20);         
        txt_txDiscount.setText("");
        txt_txDiscount.setHorizontalAlignment(JTextField.RIGHT);
        dlgTransaction.add(txt_txDiscount);
                       
        btnAdd=new JButton("Add Item");
        btnAdd.setBounds(680, 290, 100, 25);
        btnAdd.setActionCommand(ACT_ADD);
        btnAdd.addActionListener(this);
        dlgTransaction.add(btnAdd);
        
        btnDelete=new JButton("Drop Item");
        btnDelete.setBounds(790, 290, 100, 25);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        dlgTransaction.add(btnDelete);
        
        dlgTransaction.getRootPane().setDefaultButton(btnPostTx);
        
        displayInvoice();
        dlgTransaction.setVisible(true);          
        dlgTransaction.dispose(); //close the app once done
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgTransaction.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            if (ItemListTable.selectedRowIndex!=-1)
            {
                double delAmount=ItemListTable.selectedAmount;  
                double delDisc=ItemListTable.selectedDiscount;
                totalAmount=totalAmount-delAmount;
                totalDiscount=totalDiscount-delDisc;
                txt_total.setText(df.format(totalAmount));
                txt_balance.setText(df.format(totalAmount));
                txt_discount.setText(df.format(totalDiscount));
                
                itemListTable.removeSelectedRow();
            }            
            return;
	}
        else  if(e.getActionCommand().equals(ACT_DISCOUNT))
        {
            
            //clear the discount amount when the product changes
            txt_txDiscount.setText("");
            try
            {
               int selProduct=productList.get(cbo_product.getSelectedItem());
               int qty=productService.getAvailableQuantity(selProduct);
               txt_txQtyAvailable.setText(df.format(qty)); 
               if (qty<=0)
               {
                    JOptionPane.showMessageDialog(null, "The Available Quantity is not Enough to service this transaction...");
                    return;
                }
               double sellprice=productService.getProductSellingPrice(selProduct);
               txt_curPrice.setText(df.format(sellprice)); 
            }
            catch(Exception ex)
            {
                //ignore exception
            }                       
            return;
	}
        else if(e.getActionCommand().equals(ACT_ADD))
        {
            
            if (cbo_paymentTerms.getSelectedIndex()==2)
            {
                 JOptionPane.showMessageDialog(null, "Select the Payment Terms First, then proceed...");
                 return;
            }
            if(txt_txQty.getText().equalsIgnoreCase(""))
            {
                 JOptionPane.showMessageDialog(null, "Sales Quantity Missing...");
                 return;
            }
            
            if (btnPostTx.getText().equalsIgnoreCase("Add Transaction"))
            {
                 JOptionPane.showMessageDialog(null, "Click Add Transaction Button First, then proceed...");
                 return;
            }
            if (txDatePicker.getDate().compareTo(new Date())>0)
            {
                 JOptionPane.showMessageDialog(null, "The Invoice Date should be greater than the current Date...");
                 return;
            }
            
            addItemToTill();
            
            
        
        }
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            
           if(btnPostTx.getText().equalsIgnoreCase("Add Transaction"))
           {
               itemListTable.removeAllRows();
               txt_txCash.setText("");
               txt_total.setText("");
               txt_balance.setText("");
               txt_payment.setText("");
               btnPostTx.setText("Post Transaction");
               btnPrintInvoice.setText("Cancel Transaction");
               totalAmount=0.0;
               return;
           }
           else  if(btnPostTx.getText().equalsIgnoreCase("Post Transaction"))
           {
               
               postTransaction();              
           }
                  
            return;
        }
        else if(e.getActionCommand().equals(ACT_CANCEL))
        {
            return; 
        }
        else if(e.getActionCommand().equals(ACT_PAYTERM))
        {
            if(cbo_paymentTerms.getSelectedIndex()==0)
            {
                txt_days.setText("");
                txt_days.setEditable(false);
                txt_txCash.setEditable(true);
               //btnPrintInvoice.setText("Print Receipt");
            }
            else if(cbo_paymentTerms.getSelectedIndex()==1)
            {
                txt_days.setEditable(true);
                txt_txCash.setText("");
                txt_txCash.setEditable(false);
                //btnPrintInvoice.setText("Print Invoice");
            }
            else
            {
                txt_days.setEditable(false);
                txt_txCash.setEditable(false);
            }
                
             
            return; 
        }
        else if(e.getActionCommand().equals(ACT_PRINT))
        {
            
            if(btnPrintInvoice.getText().equalsIgnoreCase("Cancel Transaction"))
            {
                btnPostTx.setText("Add Transaction");
                if(cbo_paymentTerms.getSelectedIndex()==0)
                {
                    btnPrintInvoice.setText("Print Receipt");
                }
//                else if(cbo_paymentTerms.getSelectedIndex()==2)
//                {
//                    btnPrintInvoice.setText("Print Receipt");
//                }
                else
                {
                    btnPrintInvoice.setText("Print Invoice");
                }
            }
            
            
            return;            
        }
        else if(e.getActionCommand().equals(ACT_FIND))
        {
            cbo_product.removeAllItems();
            if(cbo_productCategory.getSelectedItem().equals("Select"))
            {
                return;
            }
            int selCategory=categoryList.get(cbo_productCategory.getSelectedItem());
            productList=productService.getProductMap(selCategory);           
            
            cbo_product.addItem("Select Product");
            for(String i:productList.keySet())
            {
                cbo_product.addItem(i);
            } 
           
            cbo_product.setSelectedItem("Select Product");
            return; 
        }
        
        
    }
    
    private void addItemToTill()
    {
        int selProduct=productList.get(cbo_product.getSelectedItem());
        Product product=productService.getProductById(selProduct);
        double sellprice=productService.getProductSellingPrice(selProduct);
        double discount=0.0;
        

        int qty=0;
        if(!txt_txQty.getText().isEmpty()&& isNumeric(txt_txQty.getText()) )
        {
            qty=Integer.parseInt(txt_txQty.getText());
        }
        
        if(!txt_txDiscount.getText().isEmpty()&& isNumeric(txt_txDiscount.getText()) )
        {
            discount=Double.parseDouble(txt_txDiscount.getText());
            discount=discount*qty;
        }


        double amount=(qty*sellprice)-discount;
        totalDiscount=totalDiscount+discount;
        txt_discount.setText(df.format(totalDiscount));
        totalAmount=totalAmount+amount;//accumulate the total as items are added on the list
        salesItem=new SalesItem();
        salesItem.setCustomer(selectedCustomer);
        salesItem.setProduct(product);            
        salesItem.setQuantity(qty);
        salesItem.setSellPrice(sellprice);
        salesItem.setAmount(amount);
        if(cbo_paymentTerms.getSelectedIndex()==0)
        {
            salesItem.setTxType(2);
        }
        else
        {
            salesItem.setTxType(11);
        }

        salesItem.setSalesDate(txDatePicker.getDate());
        salesItem.setDiscount(discount);
        itemListTable.insertRow(salesItem);  
        txt_total.setText(df.format(totalAmount));

        if (txt_txCash.getText().length()>0)
        {
            double dblCashAmount=0;
            if(isNumeric(txt_txCash.getText()) )
            {
                dblCashAmount=Double.parseDouble(txt_txCash.getText());
            }

            txt_payment.setText(df.format(dblCashAmount));
            double dblBalance=totalAmount-dblCashAmount;
            txt_balance.setText(df.format(dblBalance));
        }
        else
        {
            txt_balance.setText(df.format(totalAmount));
        }
        //reset the controls
        //cbo_product.setSelectedItem("Select Product");
        cbo_productCategory.setSelectedItem("Select");
        txt_txQty.setText("");
        txt_txQtyAvailable.setText("");
        txt_txDiscount.setText("");
    }
    
    /*
     * this method is for posting/persisting a transaction to the db
     */
    private void postTransaction()
    {
        txPosted=false;
        
        if (cbo_salesRep.getSelectedItem().equals("Select"))
        {
            JOptionPane.showMessageDialog(null, "Select the Sales Representative First, then proceed...");
            return;
        }
        
        if(cbo_paymentTerms.getSelectedIndex()==1 && (txt_days.getText().equalsIgnoreCase("")))
        {
            JOptionPane.showMessageDialog(null, "Enter The Credit days then proceed...");
            return; 
        }
        
        //check if there is any record to post
        List<SalesItem> itemsList=itemListTable.getSalesItemList();
        if (itemsList.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "No Items are available on the Till to be Posted...");
            return;
        }
        
        
        
            
        /*
                * Start by checking the type of Transaction
                * If CASH transaction is selected then append data to cash_bank and Transaction and Transaction summary tables
        */
            
               
        double listTotal=0.0;
        double payCash=0.0;
        if (!itemsList.isEmpty())
        {
            for(SalesItem sl:itemsList)
            {
                sl.setCustomer(selectedCustomer);//add customer
                if(cbo_paymentTerms.getSelectedIndex()==0)
                {
                    sl.setTxType(2); //transaction code for a cash transaction
                }
                else
                {
                    sl.setTxType(11); //transaction code for a credit transaction
                }
                           
                sl.setSalesDate(txDatePicker.getDate());//add transaction date
                listTotal=listTotal+sl.getAmount();//try get the total and compare with the cah total before posting the transaction
                System.out.println("Product:" + sl.getProduct().getProductCode()+ " Tx Amount: "+sl.getAmount());
                //coment after debugging
            }
        }
                
        //create transaction summary object
        if(cbo_paymentTerms.getSelectedIndex()!=1)
        {
            if(txt_txCash.getText().equalsIgnoreCase(""))
            {
                JOptionPane.showMessageDialog(null, "Cash Amount Missing...");
                return;
            }
        }
                    
        if(isNumeric(txt_txCash.getText()))
        {
            payCash=Double.parseDouble(txt_txCash.getText()); 
        }
                        
        if((payCash<listTotal)&&(cbo_paymentTerms.getSelectedIndex()==0))
        {
            JOptionPane.showMessageDialog(null, "Amount is not Enough to execute this transaction...");
            return; 
        }
        else
        {
            SalesTransaction salesTx=new SalesTransaction();
            salesTx.setCustomer(selectedCustomer);
            salesTx.setTxType(cbo_paymentTerms.getSelectedItem().toString());
            salesTx.setTxSalesAmount(totalAmount);
            salesTx.setSalesRep(salesRepList.get(cbo_salesRep.getSelectedItem()));
            salesTx.setTxSalesDate(txDatePicker.getDate());
            salesTx.setTxSalesDueDate(txDueDatePicker.getDate());
                       

            //create cash sale object if cheque or cash is selected
            //Only cash transactions are posted to the cash collection account
            //all other transactions are treated as credit transactions and therefore the 
            //cash object will not be created
            Cash cashTx=new Cash();
            if(cbo_paymentTerms.getSelectedIndex()!=1)                        
            {

                if (cbo_paymentTerms.getSelectedIndex()==0)
                {
                    cashTx.setAccount("Cash");
                    cashTx.setTxCode(2);//kindly change the code to be selected from the database after successfull test

                }
                //I ignored this since this shld be treated as credit transaction then the cash can be collected later
                //or immediately by posting a credit transaction then using the receive payment option to collect the cash
                //receipt either as cheque or mobile money
//                            else if (cbo_paymentTerms.getSelectedIndex()==2)
//                            {
//                                cashTx.setAccount("Bank");
//                                cashTx.setTxCode(6);//kindly change the code to be selected from the database after successfull test
//                                salesTx.setPaid(true);
//                            }

                cashTx.setAmount(totalAmount);
                cashTx.setCustomer(selectedCustomer);
                cashTx.setTxType("DR");
                cashTx.setDate(txDatePicker.getDate());
            }


            //call posting method to post all the transactions
            System.out.println("Test Posting transaction");
            try {

                if (cbo_paymentTerms.getSelectedIndex()==1)
                {
                    //posting credit trans
                    salesTx.setPaid(false);
                    System.out.println("Test Posting Credit transaction");
                    salesService.postTransaction(salesTx, itemsList);
                    InvoicePrinter invoicePrinter=new InvoicePrinter();
                    invoicePrinter.generateInvoice(salesTx, itemsList);
                    
                    //Update customer dashboard
                    updateCustomerDashboard();
                    //close dialog
                    dlgTransaction.setVisible(false);
                     return;
                    
                }
                else
                {
                    //posting cash/cheque trans
                    System.out.println("Test Posting Cash transaction");
                    salesTx.setPaid(true);
                    salesService.postTransaction(salesTx, itemsList, cashTx);  
                    CustomerDashboard.txListTable.insertRow(selectedCustomer);
                    txPosted=true;
                    
                    //Update customer dashboard
                    updateCustomerDashboard();
                    //close dialog
                    dlgTransaction.setVisible(false);
                }


                //btnPostTx.setText("Add Transaction");
                if(cbo_paymentTerms.getSelectedIndex()==0)
                {
                    //procedure for printing a receipt should be called here
                    btnPrintInvoice.setText("Print Receipt");
                }
                else
                {
                    //procedure for printing an invoice should be called here
                    btnPrintInvoice.setText("Print Invoice");
                }

            } catch (SQLException ex) {
                Logger.getLogger(SalesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            return;
        } 
    }
    
    private void displayInvoice()
    {
        if (CustomerListTable.selectedCustomer!=null && TransactionsTable.selectedInvoice!=null)
         {
             itemListTable.displayInvoice(CustomerListTable.selectedCustomer, TransactionsTable.selectedInvoice);
             List<SalesItem> itemsList=itemListTable.getSalesItemList();
             SalesTransaction salesTx=new SalesTransaction();
             salesTx=salesService.getTransactionsById(CustomerListTable.selectedCustomer, TransactionsTable.selectedInvoice);
             for(SalesItem s:itemsList)
             {
                totalDiscount=totalDiscount+s.getDiscount();
         
             }
             txt_total.setText(df.format(salesTx.getTxSalesAmount()));
             txt_discount.setText(df.format(totalDiscount));
             
             txDatePicker.setDate(salesTx.getTxSalesDate());
             txDueDatePicker.setDate(salesTx.getTxSalesDueDate());
             //txt_txNumber.setText(PurchasesList.purchasesItemList2.get(0).getInvoiceNumber());
             if(salesTx.isPaid())
             {
                txt_payment.setText(df.format(salesTx.getTxSalesAmount()));
             }
             else
             {
                 txt_balance.setText(df.format(salesTx.getTxSalesAmount()));
             }
            
             
             btnAdd.setEnabled(false);
             btnDelete.setEnabled(false);
             btnPostTx.setEnabled(false);
             btnPrintInvoice.setEnabled(false);
         }
     
    }
    
    private void updateCustomerDashboard()
    {
        double totalSales=salesService.getTotalSalesByCustomerId(selectedCustomer.getCustomer_id());
        double totalCash=salesService.getTotalCashByCustomerId(selectedCustomer.getCustomer_id());
        double lastPayment=salesService.getLastCustomerPayment(selectedCustomer);
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
            
    
    /*
     * This method if for checking is string is a number
     */
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
    
    
     public static void createAndShowGUI(Customer customer)
     {
         
         AccountsManagement.logger.info("Loading Transactions");
         SalesDialog invoiceForm = new SalesDialog(customer);
         
         
     }
     
     public static void setSelectedCustomer(Customer customer)
     {
         selectedCustomer=customer;
     }
}
