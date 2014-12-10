/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.util.Date;

/**
 *
 * @author LENOVO USER
 */
public class Prepayment {
    private int prepay_id;
    private double amount;
    private Customer customer;
    private Purchase purchase;
    private Date date;
    private double balance;
    private boolean isAllocated;
    private double amountAllocated;
    private SalesTransaction salesTransaction;


    public double getAmountAllocated() {
        return amountAllocated;
    }

    public void setAmountAllocated(double amountAllocated) {
        this.amountAllocated = amountAllocated;
    }

    public SalesTransaction getSalesTransaction() {
        return salesTransaction;
    }

    public void setSalesTransaction(SalesTransaction salesTransaction) {
        this.salesTransaction = salesTransaction;
    }    
       
    public int getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(int prepay_id) {
        this.prepay_id = prepay_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
            
}
