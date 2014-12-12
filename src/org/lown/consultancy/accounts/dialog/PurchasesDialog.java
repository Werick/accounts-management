/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Purchase;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.dao.ProductService;
import org.lown.consultancy.accounts.dao.PurchasesService;
import org.lown.consultancy.accounts.tables.PurchasesList;
import org.lown.consultancy.accounts.tables.PurchasesTransactions;
import org.lown.consultancy.accounts.tables.SupplierList;

/**
 *
 * @author LENOVO USER
 */
public class PurchasesDialog extends JPanel implements ActionListener{
    
    //Action Listeners constants
    public static final String ACT_ADD="Add_Transaction";
    public static final String ACT_PRICE="get_price";
    public static final String ACT_PRINT="print_invoice_receipt";
    public static final String ACT_SAVE="post_transaction";
    public static final String ACT_DELETE="remove_purchaseItem";
    public static final String ACT_CANCEL="cancel_update";
    public static final String ACT_BACK="exit";
    public static final String ACT_PAYTERM="cash_credit";
    public static final String ACT_FIND="filter_product";
    
     //Display panel
    private static JDialog dlgPurchase;
    //Sections Panels
    JPanel pTransaction, pPurchaseItems,pShipTo,pHeader;
    
     //Invoice Details Panel
    private JLabel lbl_txDate;    
    private JLabel lbl_phone;
    private JLabel lbl_txNumber;
    private JLabel lbl_supplierId;    
    private JLabel lbl_invoiceNetTotal;
    private JLabel lbl_invoiceVatTotal;
    private JLabel lbl_invoiceTotal; 
    private JLabel lbl_supplierName;
    private JLabel lbl_address;
    private JLabel lbl_txQty;
    private JLabel lbl_curPrice;
    private JLabel lbl_txCategory;
    private JLabel lbl_txDes;
    private JLabel lbl_txVat;
    private JLabel lbl_txNetAmount;
    private JLabel lbl_txDueDate;
    private JLabel lbl_days; 
    private JLabel lbl_txQtyAvailable;
    
    private JTextField txt_txNetAmount;    
    private JTextField txt_txQtyAvailable;    
    private JTextField txt_txQty;
    private JTextField txt_curPrice;
    private JTextField txt_txNumber;    
    private JTextField txt_invoiceNetTotal;
    private JTextField txt_days;
    private JTextField txt_invoiceVatTotal;
    private JTextField txt_invoiceTotal;
    
    private JCheckBox chk_txVat;
    private JComboBox cbo_productCategory;
    private JComboBox cbo_product;
    private JXDatePicker txDatePicker;
    private JXDatePicker txDueDatePicker;
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font title1Font = new Font("Times New Roman", Font.BOLD, 18);
    public static final Font title2Font = new Font("Times New Roman", Font.ITALIC, 14);
    private static Supplier selectedSupplier=null;
    private PurchasesList itemsList;
    
    //Buttons
    private JButton btnAdd;
    private JButton btnBack;
    private JButton btnPostTx;
    private JButton btnPrintInvoice;
    private JButton btnDelete;
    
    private Map<String,Integer>categoryList;
    private Map<String,Integer>productList;   
    private ProductService productService; 
    private PurchasesService purchasesService ;
    private Purchase purchaseItem;
    private double totalAmount,vatTotal,invoiceTotal;
    private DecimalFormat df = new DecimalFormat("#0.00"); 
    private String payTerms[]={"Cash","Credit","Select"};
    private boolean txVat;
    private TitledBorder supplierTitle = new TitledBorder("Supplier Information: ");
    private TitledBorder itemsTitle = new TitledBorder("Purchased Items: ");
    private TitledBorder txTitle = new TitledBorder("Purchase Information: ");
   
    public PurchasesDialog(Supplier supplier)
    {
        AccountsManagement.logger.info("Creating Purchases UI...");
        totalAmount=0.0;
        vatTotal=0.0;
        invoiceTotal=0.0;
        setSelectedSupplier(supplier);
        dlgPurchase= new JDialog((JDialog)null, "PURCHASES FORM", true);
        dlgPurchase.setLayout(null);
        dlgPurchase.setSize(950, 750);
        dlgPurchase.setLocationRelativeTo(null);//center the Sales tx on the screen
        
        purchasesService=new PurchasesService();
        productService=new ProductService();
        categoryList=productService.getCategoryMap();
        
        pHeader=new JPanel();
        pHeader.setBounds(20, 10, 400, 130);
        pHeader.setLayout(null);
        pHeader.setBorder(supplierTitle);
        dlgPurchase.add(pHeader);
        
        lbl_supplierName=new JLabel();
        lbl_supplierName.setBounds(20, 25, 300, 20);         
        lbl_supplierName.setText("Name: "+selectedSupplier.getSupplierName());
        lbl_supplierName.setFont(titleFont);
        pHeader.add(lbl_supplierName);
        
        lbl_supplierId=new JLabel();
        lbl_supplierId.setBounds(20, 50, 200, 20);         
        lbl_supplierId.setText("Supplier #: "+selectedSupplier.getSupplierNumber());
        lbl_supplierId.setFont(titleFont);
        pHeader.add(lbl_supplierId);
        
        lbl_address=new JLabel();
        lbl_address.setBounds(20, 75, 200, 20);         
        lbl_address.setText("Address: "+ selectedSupplier.getAddress());  
        lbl_address.setFont(titleFont); 
        pHeader.add(lbl_address);
        
        lbl_phone=new JLabel();
        lbl_phone.setBounds(20, 100, 300, 20);         
        lbl_phone.setText("Phone #: "+selectedSupplier.getPhone());  
        lbl_phone.setFont(titleFont); 
        pHeader.add(lbl_phone);
        
         //Create Invoice Details Panel
        pTransaction=new JPanel();
        pTransaction.setBounds(500, 10, 400, 130);
        pTransaction.setLayout(null);
        pTransaction.setBorder(txTitle);
        
        lbl_txDate=new JLabel();
        lbl_txDate.setBounds(20, 25, 150, 20);         
        lbl_txDate.setText("Invoice Date:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_txDate);
        
        txDatePicker=new JXDatePicker();
        txDatePicker.setDate(new Date());
        txDatePicker.setFormats(new String[] { "dd-MMM-yyyy" });
        txDatePicker.setBounds(170, 25, 150, 20);
        pTransaction.add(txDatePicker);
        
        
        lbl_days=new JLabel();
        lbl_days.setBounds(20, 50, 100, 20);         
        lbl_days.setText("Credit Period: ");
        lbl_days.setFont(titleFont);
        pTransaction.add(lbl_days);
        
        txt_days=new JTextField();
        txt_days.setBounds(170, 50, 100, 20);         
        txt_days.setHorizontalAlignment(JTextField.RIGHT);
        
        //txt_days.setEditable(false);
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
        pTransaction.add(txt_days);
        
        lbl_txDueDate=new JLabel();
        lbl_txDueDate.setBounds(20, 75, 150, 20);         
        lbl_txDueDate.setText("Invoice Due Date:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_txDueDate); 
        
        txDueDatePicker=new JXDatePicker();
        txDueDatePicker.setDate(new Date());
        txDueDatePicker.setFormats(new String[] { "dd-MMM-yyyy" });
        txDueDatePicker.setBounds(170, 75, 150, 20);
        txDueDatePicker.setEditable(false);
        pTransaction.add(txDueDatePicker);
        
        lbl_txNumber=new JLabel();
        lbl_txNumber.setBounds(20, 100, 150, 20);         
        lbl_txNumber.setText("Invoice Number:");
        //lbl_title.setFont(MainMenu.titleFont);         
        pTransaction.add(lbl_txNumber); 
        
         
        txt_txNumber=new JTextField();
        txt_txNumber.setBounds(170, 100, 150, 20);         
        txt_txNumber.setText("");
        pTransaction.add(txt_txNumber); 
        
        pPurchaseItems=new JPanel();
        pPurchaseItems.setBounds(20, 150, 878, 530);
        pPurchaseItems.setLayout(null);
        pPurchaseItems.setBorder(itemsTitle);
        dlgPurchase.add(pPurchaseItems);
        
        lbl_txCategory=new JLabel();
        lbl_txCategory.setBounds(20, 25, 80, 25);         
        lbl_txCategory.setText("Catgory:");        
        pPurchaseItems.add(lbl_txCategory);        
               
        cbo_productCategory=new JComboBox(categoryList.keySet().toArray());
        cbo_productCategory.addItem("Select");
        cbo_productCategory.setSelectedItem("Select");
        cbo_productCategory.setBounds(20, 50, 100, 25);  
        cbo_productCategory.setActionCommand(ACT_FIND);
        cbo_productCategory.addActionListener(this);
        AutoCompleteDecorator.decorate(this.cbo_productCategory);
        //System.out.println("Is editable - " + this.cbo_productCategory.isEditable() + ". Surprise!");
        pPurchaseItems.add(cbo_productCategory);
        
        lbl_txDes=new JLabel();
        lbl_txDes.setBounds(130, 25, 150, 25);         
        lbl_txDes.setText("Description:");        
        pPurchaseItems.add(lbl_txDes);            
        
        
        cbo_product=new JComboBox();
        cbo_product.setBounds(130, 50, 170, 25);
        cbo_product.setActionCommand(ACT_PRICE);
        cbo_product.addActionListener(this);
        AutoCompleteDecorator.decorate(this.cbo_product);
        //System.out.println("Is editable - " + this.cbo_productCategory.isEditable() + ". Surprise!");
        pPurchaseItems.add(cbo_product);
        
        lbl_txQty=new JLabel();
        lbl_txQty.setBounds(320, 25, 80, 25);         
        lbl_txQty.setText("Quantity:");        
        pPurchaseItems.add(lbl_txQty);
        
        txt_txQty=new JTextField();
        txt_txQty.setBounds(320, 50, 70, 25);         
        txt_txQty.setText("");
        txt_txQty.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_txQty);
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
                    if(isNumeric(txt_txQty.getText()) )
                    {
                        dblQty=Double.parseDouble(txt_txQty.getText());
                        int selProduct=productList.get(cbo_product.getSelectedItem());                        
                        double buyprice=productService.getProductBuyingPrice(selProduct);
                        dblPrice=dblQty*buyprice;
                    }
                    
                    txt_txNetAmount.setText(df.format(dblPrice));                           
                }
                else
                {
                    txt_txNetAmount.setText("");
                }
            }
           
        });
        
        lbl_txQtyAvailable=new JLabel();
        lbl_txQtyAvailable.setBounds(400, 25, 80, 25);         
        lbl_txQtyAvailable.setText("Avail. Qty:");
        pPurchaseItems.add(lbl_txQtyAvailable);
        
        txt_txQtyAvailable=new JTextField();
        txt_txQtyAvailable.setBounds(400, 50, 80, 25);         
        txt_txQtyAvailable.setText("");
        txt_txQtyAvailable.setEditable(false);
        txt_txQtyAvailable.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_txQtyAvailable);
        
        lbl_curPrice=new JLabel();
        lbl_curPrice.setBounds(500, 25, 80, 25);         
        lbl_curPrice.setText("Buy Price:");
        pPurchaseItems.add(lbl_curPrice);
        
        txt_curPrice=new JTextField();
        txt_curPrice.setBounds(500, 50, 80, 25);         
        txt_curPrice.setText("");        
        txt_curPrice.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_curPrice);
         /*
         * Add a document Listener method to track changes in the text box and calls a calculate method
         */
        txt_curPrice.getDocument().addDocumentListener(new DocumentListener() {
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
                if (txt_curPrice.getText().length()>0)
                {                   
                    
                    dblQty=0.0;
                    double buyprice;
                    if(isNumeric(txt_curPrice.getText())&& (isNumeric(txt_txQty.getText())) )
                    {
                        dblQty=Double.parseDouble(txt_txQty.getText());                        
                        buyprice=Double.parseDouble(txt_curPrice.getText());
                        dblPrice=dblQty*buyprice;
                    }                    
                    txt_txNetAmount.setText(df.format(dblPrice));                           
                }
                else
                {
                    txt_txNetAmount.setText("");
                }
            }
           
        });
        
        
        lbl_txNetAmount=new JLabel();
        lbl_txNetAmount.setBounds(600, 25, 150, 25);         
        lbl_txNetAmount.setText("Price Inclusive:");
        pPurchaseItems.add(lbl_txNetAmount);
        
        txt_txNetAmount=new JTextField();
        txt_txNetAmount.setBounds(600, 50, 120, 25);         
        txt_txNetAmount.setText("");
        txt_txNetAmount.setEditable(false);
        txt_txNetAmount.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_txNetAmount);
        
        lbl_txVat=new JLabel();
        lbl_txVat.setBounds(750, 25, 100, 25);         
        lbl_txVat.setText("VAT:");
        pPurchaseItems.add(lbl_txVat);
        
        chk_txVat=new JCheckBox(MainMenu.companyDetails.get("company.vat").toString()+" %");
        chk_txVat.setBounds(750, 50, 120, 25);
        chk_txVat.setEnabled(false);
        chk_txVat.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) 
                {
                    //label.setVisible(true);
                    txVat=true;        
                } 
                else 
                {
                    //label.setVisible(false);
                    txVat=false;
                }
            }
        });        
        pPurchaseItems.add(chk_txVat);
        
        //Item List Table
        itemsList=new PurchasesList();
        itemsList.setBounds(20,100,700, 280);
        pPurchaseItems.add(itemsList);
        
        lbl_invoiceNetTotal=new JLabel();
        lbl_invoiceNetTotal.setBounds(350, 400, 150, 25);         
        lbl_invoiceNetTotal.setText("NET TOTAL:");  
        lbl_invoiceNetTotal.setFont(titleFont);
        pPurchaseItems.add(lbl_invoiceNetTotal);
        
        txt_invoiceNetTotal=new JTextField();
        txt_invoiceNetTotal.setBounds(520, 400, 150, 25);         
        txt_invoiceNetTotal.setText("");
        txt_invoiceNetTotal.setFont(titleFont);
        txt_invoiceNetTotal.setEditable(false);
        txt_invoiceNetTotal.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_invoiceNetTotal);
        
        lbl_invoiceVatTotal=new JLabel();
        lbl_invoiceVatTotal.setBounds(350, 430, 150, 25);         
        lbl_invoiceVatTotal.setText("VAT:");  
        lbl_invoiceVatTotal.setFont(titleFont);
        pPurchaseItems.add(lbl_invoiceVatTotal);
        
        txt_invoiceVatTotal=new JTextField();
        txt_invoiceVatTotal.setBounds(520, 430, 150, 25);         
        txt_invoiceVatTotal.setText("");
        txt_invoiceVatTotal.setFont(titleFont);   
        txt_invoiceVatTotal.setEditable(false);
        txt_invoiceVatTotal.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_invoiceVatTotal);
        
        lbl_invoiceTotal=new JLabel();
        lbl_invoiceTotal.setBounds(350, 460, 150, 25);         
        lbl_invoiceTotal.setText("INVOICE AMOUNT:");  
        lbl_invoiceTotal.setFont(titleFont);
        pPurchaseItems.add(lbl_invoiceTotal);
        
        txt_invoiceTotal=new JTextField();
        txt_invoiceTotal.setBounds(520, 460, 150, 25);         
        txt_invoiceTotal.setText("");
        txt_invoiceTotal.setFont(titleFont);  
        txt_invoiceTotal.setEditable(false);  
        txt_invoiceTotal.setHorizontalAlignment(JTextField.RIGHT);
        pPurchaseItems.add(txt_invoiceTotal);
        
        //buttons
        btnAdd=new JButton("Add Item");
        btnAdd.setBounds(730, 100, 100, 25);
        btnAdd.setActionCommand(ACT_ADD);
        btnAdd.addActionListener(this);
        pPurchaseItems.add(btnAdd);
        
        btnDelete=new JButton("Drop Item");
        btnDelete.setBounds(730, 140, 100, 25);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        pPurchaseItems.add(btnDelete);
        
        btnPostTx=new JButton("Post Purchase");
        btnPostTx.setBounds(50, 400, 140, 45);  //615
        btnPostTx.setActionCommand(ACT_SAVE);
        btnPostTx.addActionListener(this);
        pPurchaseItems.add(btnPostTx);
        
        btnPrintInvoice=new JButton("Cancel Purchase");
        btnPrintInvoice.setBounds(50, 460, 140, 45); //645
        btnPrintInvoice.setActionCommand(ACT_CANCEL);
        btnPrintInvoice.addActionListener(this);
        pPurchaseItems.add(btnPrintInvoice);
        
        btnBack=new JButton("Close");
        btnBack.setBounds(720, 460, 140, 45); //675
        btnBack.setActionCommand(ACT_BACK);
        btnBack.addActionListener(this);
        pPurchaseItems.add(btnBack);
        
        
        
        dlgPurchase.add(pTransaction);//add the invoice details panel
        
        //load details if supplier and invoice is selected
        displayInvoice();
        
        dlgPurchase.setVisible(true);          
        dlgPurchase.dispose(); //close the app once done
    }
    
   
     @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgPurchase.setVisible(false);
            return;
	}
        else  if(e.getActionCommand().equals(ACT_CANCEL))
        {
            itemsList.removeAllRows();
            txt_invoiceNetTotal.setText("");
            txt_invoiceVatTotal.setText("");
            txt_invoiceTotal.setText("");
             
                //reset the controls
                //cbo_product.setSelectedItem("Select Product");
            cbo_productCategory.setSelectedItem("Select");
            txt_txQty.setText("");            
            txt_txNetAmount.setText("");
            chk_txVat.setSelected(false);
               
            btnPostTx.setText("Post Purchase");
            btnPrintInvoice.setText("Cancel Transaction");
            totalAmount=0.0;
            vatTotal=0.0;
            invoiceTotal=0.0;
            return;
	}
        else if(e.getActionCommand().equals(ACT_PRICE))
        {
            txt_txQtyAvailable.setText(df.format(0));
            double dblqty;
            try
            {
                int selProduct=productList.get(cbo_product.getSelectedItem());
                dblqty=purchasesService.getAvailableQuantity(selProduct);
                double buyprice=productService.getProductBuyingPrice(selProduct);
                txt_txQtyAvailable.setText(df.format(dblqty));
                txt_curPrice.setText(df.format(buyprice));
                chk_txVat.setSelected(purchasesService.hasVAT(selProduct));
            }
            catch(Exception ex)
            {
                //ignore exception
            }
	}
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnPostTx.getText().equalsIgnoreCase("Add Transaction"))
            {
               itemsList.removeAllRows();
               txt_invoiceNetTotal.setText("");
               txt_invoiceVatTotal.setText("");
               txt_invoiceTotal.setText("");
             
                //reset the controls
                //cbo_product.setSelectedItem("Select Product");
                cbo_productCategory.setSelectedItem("Select");
                txt_txQty.setText("");                
                txt_txNetAmount.setText("");
                chk_txVat.setSelected(false);
               
               btnPostTx.setText("Post Purchase");
               btnPrintInvoice.setText("Cancel Transaction");
               totalAmount=0.0;
               vatTotal=0.0;
               invoiceTotal=0.0;
               return;
           }
           else  if(btnPostTx.getText().equalsIgnoreCase("Post Purchase"))
           {
               //check if there is any record to post
                List<Purchase> purchaseList=itemsList.getPurchasesItemList();
                if (purchaseList.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "No Items are available to be Posted...");
                    return;
                }
                if (txt_days.getText().equalsIgnoreCase(""))
                {
                    JOptionPane.showMessageDialog(null, "Enter The Credit days then proceed...");
                    return; 
                }
                if (txt_txNumber.getText().equalsIgnoreCase(""))
                {
                    JOptionPane.showMessageDialog(null, "Enter The Invoice Number then proceed...");
                    return; 
                }
                
                //update the purchase list with missing variables
                if (!purchaseList.isEmpty())
                {
                    for(Purchase p:purchaseList)
                    {
                        p.setSupplier(selectedSupplier);//add supplier                            
                        p.setDate(txDatePicker.getDate());//add transaction date
                        p.setDueDate(txDueDatePicker.getDate());//add invoice due date
                        p.setInvoiceNumber(txt_txNumber.getText()); //invoice number                            
                            
                    }
                    
                    //post the purchase items
                    //call posting method to post all the transactions
                        System.out.println("Test Posting transaction");
                        try {                
                                purchasesService.postPurchases(purchaseList);    
                                
                                // update supplier dashboard
                                updateSupplierDashboard();
                                dlgPurchase.setVisible(false);                               
                                //btnPostTx.setText("Add Transaction");                            
                        } catch (SQLException ex) {
                            Logger.getLogger(SalesDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        return;
                }
           }
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
            
            for(String i:productList.keySet())
            {
                cbo_product.addItem(i);
            } 
            cbo_product.addItem("Select Product");
            cbo_product.setSelectedItem("Select Product");
            return; 
        }
        else if(e.getActionCommand().equals(ACT_ADD))
        {
            if(cbo_product.getSelectedItem().equals("Select Product"))
            {
                 JOptionPane.showMessageDialog(null, "No Product is Selected...");
                 return;
            }
            
            if(txt_txQty.getText().equalsIgnoreCase(""))
            {
                 JOptionPane.showMessageDialog(null, "Purchased Quantity Missing...");
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
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            if (PurchasesList.selectedRowIndex!=-1)
            {
                double delAmount=PurchasesList.selectedAmount;     
                double delVat=PurchasesList.selectedVat; 
                double delNet=PurchasesList.selectedNet;     
                totalAmount=totalAmount-delNet;
                vatTotal=vatTotal-delVat;
                invoiceTotal=invoiceTotal-delAmount;
                
                txt_invoiceTotal.setText(df.format(invoiceTotal));
                txt_invoiceVatTotal.setText(df.format(vatTotal));
                txt_invoiceNetTotal.setText(df.format(totalAmount));
                itemsList.removeSelectedRow();
            }            
            return;
	}
    }
     
     private void addItemToTill()
     {
        int selProduct=productList.get(cbo_product.getSelectedItem());
        Product product=productService.getProductById(selProduct);
        double vat=0.0;
        double vatRate=0.0;
        if(txVat==true)
        {
            vatRate=Double.parseDouble(MainMenu.companyDetails.get("company.vat").toString());
        }


        double netAmount=0.0;
        if(!txt_txNetAmount.getText().isEmpty()&& isNumeric(txt_txNetAmount.getText()) )
        {
            netAmount=Double.parseDouble(txt_txNetAmount.getText());
        }

        int qty=0;
        if(!txt_txQty.getText().isEmpty()&& isNumeric(txt_txQty.getText()) )
        {
            qty=Integer.parseInt(txt_txQty.getText());
        }
        
        double unitPrice=0.0;
        if(!txt_curPrice.getText().isEmpty()&& isNumeric(txt_curPrice.getText()) )
        {
            unitPrice=Double.parseDouble(txt_curPrice.getText());
        }
        
        vat=netAmount*(vatRate/100.0);
        double amount=(netAmount-vat);
        totalAmount=totalAmount+amount;//accumulate the total as items are added on the list
        vatTotal=vatTotal+vat;
        invoiceTotal=invoiceTotal+netAmount;

        purchaseItem=new Purchase();
        purchaseItem.setAmount(netAmount);
        purchaseItem.setQty(qty);
        purchaseItem.setInvoiceNumber(txt_txNumber.getText());
        purchaseItem.setProduct(product);
        purchaseItem.setNetAmount(amount);
        purchaseItem.setVat(vat);
        purchaseItem.setDate(txDatePicker.getDate());
        purchaseItem.setSupplier(null); //add supplier
        purchaseItem.setUnitPrice(unitPrice);
        
         itemsList.insertRow(purchaseItem);  
         txt_invoiceNetTotal.setText(df.format(totalAmount));
         txt_invoiceVatTotal.setText(df.format(vatTotal));
         txt_invoiceTotal.setText(df.format(invoiceTotal));

         //reset the controls
        //cbo_product.setSelectedItem("Select Product");
        cbo_productCategory.setSelectedItem("Select");
        txt_txQty.setText("");        
        txt_txNetAmount.setText("");
        chk_txVat.setSelected(false);
        txt_curPrice.setText("");
        txt_txQtyAvailable.setText("");
         
     }
     private void updateSupplierDashboard()
    {
        
        try
        {
            double totalPurchases=purchasesService.getTotalPurchasesBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
            double totalPayments=purchasesService.getTotalPaymentsBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
            double balance=totalPurchases-totalPayments;
            double lastPayment=purchasesService.getLastPaymentMade(selectedSupplier);
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
     
     public static void createAndShowGUI(Supplier supplier)
     {
         
         AccountsManagement.logger.info("Loading Transactions");
         PurchasesDialog purchasesForm = new PurchasesDialog(supplier);         
     }
     public static void setSelectedSupplier(Supplier supplier)
     {
         selectedSupplier=supplier;
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
     
     private void displayInvoice()
     {
         if (SupplierList.selectedSupplier!=null && PurchasesTransactions.selectedInvoice!=null)
         {
             itemsList.displayInvoice(SupplierList.selectedSupplier, PurchasesTransactions.selectedInvoice);
             List<Purchase> purchaseList=itemsList.getPurchasesItemList();
             for(Purchase p:purchaseList)
             {
                 
                 totalAmount=totalAmount+p.getNetAmount();//accumulate the total as items are added on the list
                 vatTotal=vatTotal+p.getVat();
                 invoiceTotal=invoiceTotal+p.getAmount();
             }
             txt_invoiceNetTotal.setText(df.format(totalAmount));
             txt_invoiceVatTotal.setText(df.format(vatTotal));
             txt_invoiceTotal.setText(df.format(invoiceTotal));
             txDatePicker.setDate(PurchasesList.purchasesItemList2.get(0).getDate());
             txDueDatePicker.setDate(PurchasesList.purchasesItemList2.get(0).getDueDate());
             txt_txNumber.setText(PurchasesList.purchasesItemList2.get(0).getInvoiceNumber());
             System.out.println(PurchasesList.purchasesItemList2.get(0).getInvoiceNumber());
             btnAdd.setEnabled(false);
             btnDelete.setEnabled(false);
             btnPostTx.setEnabled(false);
             btnPrintInvoice.setEnabled(false);
         }
     }
}
