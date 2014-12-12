/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.lown.consultancy.accounts.SalesItem;
import org.lown.consultancy.accounts.SalesTransaction;

/**
 *
 * @author LENOVO USER
 */
public class PDFWriter {
    private String filename = "FosterInvoice2.pdf";
    private String finalfile = "";
    private String workingDir = System.getProperty("user.dir");
    
    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
    private static Document document;
    private static Paragraph paragraph;
    private static DecimalFormat doubleF = new DecimalFormat("#0.00");
    
    public PDFWriter()
    {
        try 
        {
            document = new Document();
            finalfile = workingDir + "\\" + filename;
            PdfWriter.getInstance(document, new FileOutputStream(finalfile));
            document.open();
            addMetaData(document);
            addInvoiceTitle(document);
            addContent();
            closeDocument();//drop this after testing
            
            //adding an image
//            Image image;
//            try {
//		image = Image.getInstance(workingDir+"\\src\\org\\lown\\consultancy\\accounts\\images\\forster2.jpg");
//		image.setAlignment(Image.MIDDLE);
//		document.add(image);
//		} 
//            catch (MalformedURLException e) {
//		e.printStackTrace();
//		} 
//            catch (IOException e) {
//		e.printStackTrace();
//			}
            //document.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void closeDocument()
    {
        document.close();
    }
	
    public void printInvoice(SalesTransaction salesTx,java.util.List<SalesItem> itemsList) throws BadElementException, DocumentException
    {
        
        paragraph=new Paragraph();
        createTable2(salesTx,itemsList);
        document.add(paragraph);
        closeDocument();
    }
    
    private static void createTable2(SalesTransaction salesTx,java.util.List<SalesItem> itemsList)throws BadElementException 
    {
      PdfPTable table = new PdfPTable(7); //number of columns

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");   
        
        SalesItem item=itemsList.get(0);
        Phrase invoice=new Phrase("Invoice Date: "+df.format(item.getSalesDate()));
        invoice.add(new Phrase("\n Invoice Number: "));
        invoice.add(new Phrase("\n Due Date: "+df.format(salesTx.getTxSalesDueDate())));
        invoice.add(new Phrase("\n Customer Number: "+ item.getCustomer().getCustomerNumber()));
        invoice.add(new Phrase("\n Sales Rep: "+salesTx.getSalesRep()));
        invoice.add(new Phrase("\n "));
        
        PdfPCell cellInvoice = new PdfPCell(new Phrase(invoice));           
        cellInvoice.setColspan(7);
        cellInvoice.setRowspan(2);
        cellInvoice.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellInvoice.setBorder(0);
        table.addCell(cellInvoice);
    
        Phrase billTo=new Phrase("Bill To: ");
        billTo.add(new Phrase("\n \t " +item.getCustomer().getCustomerName()));
        billTo.add(new Phrase("\n \t " +item.getCustomer().getPhone()));
        billTo.add(new Phrase("\n \t "+ item.getCustomer().getAddress()));
        billTo.add(new Phrase("\n \t "+ item.getCustomer().getContactPerson()));
        billTo.add(new Phrase("\n "));
        
        Phrase shipTo=new Phrase("Ship To: ");
        //shipTo.add(new Phrase("\n \t \t Customer Name"));
        //shipTo.add(new Phrase("\n \t \t Phone Number"));
        //shipTo.add(new Phrase("\n \t \t Address"));
        //shipTo.add(new Phrase("\n \t \t Contact Person"));
        shipTo.add(new Phrase("\n "));
        
        PdfPCell customer = new PdfPCell(new Phrase(billTo));           
        customer.setColspan(3);
        customer.setRowspan(2);
        //customer.setBorder(0); //remove the cell border
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(customer);
        
        PdfPCell blank_cell = new PdfPCell(new Phrase(""));        
        blank_cell.setRowspan(2);
        blank_cell.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_cell);
        
        PdfPCell customer2 = new PdfPCell(new Phrase(shipTo));           
        customer2.setColspan(3);
        customer2.setRowspan(2);
        //customer2.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(customer2);
        
        PdfPCell blank_row = new PdfPCell(new Phrase("\n"));        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_row);
        
        Phrase payTerms=new Phrase("Payment Terms");
        payTerms.add(new Phrase("\n Days"));
        
        
    
        PdfPCell c2 = new PdfPCell(new Phrase(payTerms));           
        c2.setColspan(3);
        c2.setRowspan(2);
        //c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c2);
        
        blank_cell = new PdfPCell(new Phrase(""));        
        blank_cell.setRowspan(2);
        blank_cell.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_cell);
        
        
        c2 = new PdfPCell(new Phrase("LPO NO")); 
        c2.setColspan(3);
        c2.setRowspan(2);
        table.addCell(c2);
    
        blank_row = new PdfPCell(new Phrase("\n"));        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        table.addCell(blank_row);
        
        
        PdfPCell c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Code"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description"));
        c1.setColspan(2);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unit Price"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Discount"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        //table.setHeaderRows(1);
        int i=0;
        for (SalesItem it:itemsList)
        {
            i++;
            c1 = new PdfPCell(new Phrase(Integer.toString(it.getQuantity())));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(it.getProduct().getProductCode()));
            //c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(it.getProduct().getProductName()));
            c1.setColspan(2);
            //c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(doubleF.format(Double.toString(it.getSellPrice()))));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(doubleF.format(Double.toString(it.getDiscount()))));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(doubleF.format(Double.toString(it.getAmount()))));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorderWidthBottom(0);
            table.addCell(c1);
        }
        blank_row = new PdfPCell(new Phrase("\n"));        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        table.addCell(blank_row);
        
        
        paragraph.add(table);
        

  }
    
    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) 
    {
        document.addTitle("Invoice");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lown Consultancy");
        document.addCreator("Lown Consultancy");
  }
  
  private static void addInvoiceTitle(Document document)throws DocumentException 
  {
    Paragraph invoiceTitle = new Paragraph();
    // We add one empty line
    //addEmptyLine(invoiceTitle, 1);
    // Lets write a big header
    invoiceTitle.add(new Paragraph("FORSTER TRADING (K) LTD", titleFont));
    invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
    
    addEmptyLine(invoiceTitle, 1);
//    // Lets write a big header
//    invoiceTitle.add(new Paragraph("Invoice Number:\t", titleFont));
//    invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
//    
//    invoiceTitle.add(new Paragraph("Due Date:\t"  + new Date(), titleFont));
//    invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
//    
//    //addEmptyLine(invoiceTitle, 1);
//    // Will create: Report generated by: _name, _date
//    invoiceTitle.add(new Paragraph("Customer Number: " + System.getProperty("user.name"), smallBold));
//    invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
//    invoiceTitle.add(new Paragraph("Sales Rep: " + System.getProperty("user.name") + ", " , smallBold));
//    invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
    
    document.add(invoiceTitle);
    // Start a new page
    //document.newPage();
  }
 private static void addContent() throws DocumentException {

    Paragraph invoiceDetails = new Paragraph("CUSTOMER INVOICE", subFont); 

    Paragraph paragraph = new Paragraph();
    addEmptyLine(paragraph, 2);
//    subCatPart.add(paragraph);

    // add a table
    createTable(invoiceDetails);

    document.add(invoiceDetails);
    document.add(paragraph);
    
  }

  private static void createTable(Paragraph paragraph)throws BadElementException 
  {
      PdfPTable table = new PdfPTable(7); //number of columns

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
      
        Phrase invoice=new Phrase("Invoice Date: ");
        invoice.add(new Phrase("\n Invoice Number: "));
        invoice.add(new Phrase("\n Due Date: "));
        invoice.add(new Phrase("\n Customer Number: "));
        invoice.add(new Phrase("\n Sales Rep: "));
        invoice.add(new Phrase("\n "));
        
        PdfPCell cellInvoice = new PdfPCell(new Phrase(invoice));           
        cellInvoice.setColspan(7);
        cellInvoice.setRowspan(2);
        cellInvoice.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellInvoice.setBorder(0);
        table.addCell(cellInvoice);
    
        Phrase billTo=new Phrase("Bill To: ");
        billTo.add(new Phrase("\n \t Customer Name"));
        billTo.add(new Phrase("\n \t Phone Number"));
        billTo.add(new Phrase("\n \t Address"));
        billTo.add(new Phrase("\n \t Contact Person"));
        billTo.add(new Phrase("\n "));
        
        Phrase shipTo=new Phrase("Ship To: ");
        //shipTo.add(new Phrase("\n \t \t Customer Name"));
        //shipTo.add(new Phrase("\n \t \t Phone Number"));
        //shipTo.add(new Phrase("\n \t \t Address"));
        //shipTo.add(new Phrase("\n \t \t Contact Person"));
        shipTo.add(new Phrase("\n "));
        
        PdfPCell customer = new PdfPCell(new Phrase(billTo));           
        customer.setColspan(3);
        customer.setRowspan(2);
        //customer.setBorder(0); //remove the cell border
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(customer);
        
        PdfPCell blank_cell = new PdfPCell(new Phrase(""));        
        blank_cell.setRowspan(2);
        blank_cell.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_cell);
        
        PdfPCell customer2 = new PdfPCell(new Phrase(shipTo));           
        customer2.setColspan(3);
        customer2.setRowspan(2);
        //customer2.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(customer2);
        
        PdfPCell blank_row = new PdfPCell(new Phrase("\n"));        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_row);
        
        Phrase payTerms=new Phrase("Payment Terms");
        payTerms.add(new Phrase("\n Days"));
        
        
    
        PdfPCell c2 = new PdfPCell(new Phrase(payTerms));           
        c2.setColspan(3);
        c2.setRowspan(2);
        //c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c2);
        
        blank_cell = new PdfPCell(new Phrase(""));        
        blank_cell.setRowspan(2);
        blank_cell.setBorder(0);
        //customer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(blank_cell);
        
        
        c2 = new PdfPCell(new Phrase("LPO NO")); 
        c2.setColspan(3);
        c2.setRowspan(2);
        table.addCell(c2);
    
        blank_row = new PdfPCell(new Phrase("\n"));        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        table.addCell(blank_row);
        
        
        PdfPCell c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Code"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description"));
        c1.setColspan(2);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unit Price"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Discount"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        //table.setHeaderRows(1);
        for (int i=1;i<=3;i++)
        {
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setColspan(2);
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
        }
        
        for(int i=1;i<5;i++)
        {
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setColspan(2);
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("\n"));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setBorderWidthLeft(Float.parseFloat("0.09"));
            c1.setBorderWidthRight(Float.parseFloat("0.09"));
            table.addCell(c1);
        }
        
        
        blank_row = new PdfPCell(new Phrase());        
        blank_row.setColspan(7);
        blank_row.setBorder(0);
        blank_row.setBorderWidthLeft(Float.parseFloat("0.09"));
        blank_row.setBorderWidthRight(1);
        blank_row.setBorderWidthBottom(1);
        table.addCell(blank_row);
        paragraph.add(table);

  }

  private static void createList(Section subCatPart) {
    List list = new List(true, false, 10);
    list.add(new ListItem("First point"));
    list.add(new ListItem("Second point"));
    list.add(new ListItem("Third point"));
    subCatPart.add(list);
  }

  private static void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }
}
