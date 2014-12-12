/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.lown.consultancy.accounts.SalesItem;
import org.lown.consultancy.accounts.SalesTransaction;


/**
 *
 * @author LENOVO USER
 */
public class InvoicePrinter {
    String filename = "invoice_template.xls";
    String finalfile = "";
    String invoicefile = "Invoice";
    String workingDir = System.getProperty("user.dir");
    private static Workbook workbook;
    private SimpleDateFormat datef = new SimpleDateFormat("dd-MMM-yyyy");  
    private static DecimalFormat df = new DecimalFormat("#0.00");
        
        public InvoicePrinter()
        {
           
        }
        
        public void generateInvoice(SalesTransaction salesTx,java.util.List<SalesItem> itemsList)
        {
           try
           {        
               finalfile = workingDir + "\\" + filename; 
                FileInputStream file = new FileInputStream(new File(finalfile));
                workbook = new HSSFWorkbook(file);
                Sheet sheet = workbook.getSheet("invoice");
                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next(); // Skip the header row 1.
                
                //Create cell styles
                CellStyle style= workbook.createCellStyle();
                //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

                CellStyle style2= workbook.createCellStyle();
                //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style2.setAlignment(CellStyle.ALIGN_RIGHT);
                
                CellStyle style3= workbook.createCellStyle();                
                style3.setAlignment(CellStyle.ALIGN_RIGHT);
                
                
                
                //write the invoice header
                //start at row w            
                Row curRow=sheet.getRow(1);
                Cell cell=curRow.createCell(6);                 
                cell.setCellValue(datef.format(salesTx.getTxSalesDate())); 
                
                curRow=sheet.getRow(2);
                cell=curRow.createCell(6);
                cell.setCellValue(salesTx.getCustomer().getCustomerNumber()+"-"+salesTx.getTx_summary_id()); 
                
                curRow=sheet.getRow(3);
                cell=curRow.createCell(6);
                cell.setCellValue(datef.format(salesTx.getTxSalesDueDate())); 
                
                curRow=sheet.getRow(4);
                cell=curRow.createCell(6);
                cell.setCellValue(salesTx.getCustomer().getCustomerNumber());
                
                curRow=sheet.getRow(5);
                cell=curRow.createCell(6);
                cell.setCellValue(salesTx.getSalesRep());
                
                //Bill to details
                curRow=sheet.getRow(10);
                cell=curRow.createCell(2);
                cell.setCellValue(salesTx.getCustomer().getCustomerName());
                
                curRow=sheet.getRow(11);
                cell=curRow.createCell(2);
                cell.setCellValue(salesTx.getCustomer().getPhone());
                
                curRow=sheet.getRow(12);
                cell=curRow.createCell(2);
                cell.setCellValue(salesTx.getCustomer().getAddress());
                
                curRow=sheet.getRow(13);
                cell=curRow.createCell(2);
                cell.setCellValue("Contact Person: "+salesTx.getCustomer().getContactPerson());
                
                //Invoice Details
                int startRow=20;
                curRow=sheet.getRow(startRow);
                for(SalesItem s:itemsList)
                {
                   cell=curRow.createCell(1);
                   cell.setCellStyle(style);
                   cell.setCellValue(s.getQuantity()); //qty
                   
                   cell=curRow.createCell(2);
                   cell.setCellStyle(style);
                   cell.setCellValue(s.getProduct().getProductCode()); //code
                   
                   cell=curRow.createCell(3);
                   cell.setCellStyle(style);
                   cell.setCellValue(s.getProduct().getProductName()); //descr
                   
                   
                   cell=curRow.createCell(4);
                   cell.setCellStyle(style2);
                   cell.setCellValue(df.format(s.getSellPrice())); //sp
                   
                   
                   cell=curRow.createCell(5);
                   cell.setCellStyle(style2);
                   cell.setCellValue(df.format(s.getDiscount())); //discount
                   
                   
                   cell=curRow.createCell(6);
                   cell.setCellStyle(style2);
                   cell.setCellValue(df.format(s.getAmount())); //amount
                   
                                     
                   startRow++;
                   curRow=sheet.getRow(startRow);
                }
                
                //Invoice Total Amount
                curRow=sheet.getRow(40);
                
                cell=curRow.createCell(6);
                cell.setCellStyle(style3);
                cell.setCellValue("Kes "+df.format(salesTx.getTxSalesAmount()));
                
                
//                while (rowIterator.hasNext()) 
//                {
//                    Row row = rowIterator.next();
//                    //
//                }
                invoicefile=workingDir + "\\Invoice\\" +salesTx.getCustomer().getCustomerNumber()+"_"+invoicefile+"_"+datef.format(salesTx.getTxSalesDate())+"_"+salesTx.getTx_summary_id()+".xls";
                 // Write the output to a file
            //Save the Changes
            //generate new file and save
                sheet.protectSheet("password");
            FileOutputStream fileOut = new FileOutputStream(invoicefile);
            workbook.write(fileOut);
            fileOut.close();
            displayFile();
           }
           catch(IOException ex)
           {
               Logger.getLogger(InvoicePrinter.class.getName()).log(Level.SEVERE, null, ex); 
           } 
        }
        
//        public void convert_to_pdf()
//        {
//            
//            try
//            {
//                FileInputStream fis = new FileInputStream(new File("test.doc")); 
//                FileOutputStream fos = new FileOutputStream(new File("test.pdf"));
// 
//                OfficeFile f = new OfficeFile(fis,"localhost","8100", false);
// 
//            f.convert(fos,"pdf");
//            }
//            catch(IOException ex)
//           {
//               Logger.getLogger(InvoicePrinter.class.getName()).log(Level.SEVERE, null, ex); 
//           }
//            
//        }
        
        public void displayFile()
        {
             try
                {
                File testFile=new File(invoicefile);
                if (testFile.exists())
                {
                    if(Desktop.isDesktopSupported())
                    {
                        Desktop.getDesktop().open(testFile);
                    }
                    else
                    {
                        System.out.println("AWT Desktop is not supported");
                    }
                }
                else
                {
                   System.out.println("File not Found"); 
                }
                System.out.println("Done");        
            }
            catch(IOException ex)
           {
               Logger.getLogger(InvoicePrinter.class.getName()).log(Level.SEVERE, null, ex); 
           }
        }
}
