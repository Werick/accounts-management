/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.util.Date;
import org.lown.consultancy.accounts.Customer;

/**
 *
 * @author LENOVO USER
 */
public class Invoice {
    private Integer invoiceNumber;
    private Integer invoice_id;
    private double invoiceAmount;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private String invoiceDescription;
    private Customer customer;
}
