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
public class SalesTransaction {
    private Customer customer;
    private Date txSalesDate;
    private Date txSalesDueDate;
    private double txSalesAmount;
    private String txType; //Cash or Credit sales
    private int salesRep;
    private int tx_summary_id; 
    private boolean paid;
    private double balance;
    private double allocation;

    
    
    public boolean isPaid() {
        return paid;
    }

    public double getAllocation() {
        return allocation;
    }

    public void setAllocation(double allocation) {
        this.allocation = allocation;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getTxSalesDate() {
        return txSalesDate;
    }

    public void setTxSalesDate(Date txSalesDate) {
        this.txSalesDate = txSalesDate;
    }

    public double getTxSalesAmount() {
        return txSalesAmount;
    }

    public void setTxSalesAmount(double txSalesAmount) {
        this.txSalesAmount = txSalesAmount;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public int getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(int salesRep) {
        this.salesRep = salesRep;
    }

    public int getTx_summary_id() {
        return tx_summary_id;
    }

    public void setTx_summary_id(int tx_summary_id) {
        this.tx_summary_id = tx_summary_id;
    }

    public Date getTxSalesDueDate() {
        return txSalesDueDate;
    }

    public void setTxSalesDueDate(Date txSalesDueDate) {
        this.txSalesDueDate = txSalesDueDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
         
    
}
