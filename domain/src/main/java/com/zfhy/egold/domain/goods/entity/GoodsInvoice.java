package com.zfhy.egold.domain.goods.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "goods_goods_invoice")
public class GoodsInvoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "order_id")
    private Integer orderId;

    
    @Column(name = "order_sn")
    private String orderSn;

    
    @Column(name = "invoice_amount")
    private Double invoiceAmount;

    
    @Column(name = "invoice_item")
    private String invoiceItem;

    
    @Column(name = "invoice_title")
    private String invoiceTitle;

    
    @Column(name = "invoice_type")
    private Integer invoiceType;

    
    @Column(name = "tax_no")
    private String taxNo;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;


    
    @Column(name = "member_id")
    private Integer memberId;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getOrderId() {
        return orderId;
    }

    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    
    public String getOrderSn() {
        return orderSn;
    }

    
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    
    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    
    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    
    public String getInvoiceItem() {
        return invoiceItem;
    }

    
    public void setInvoiceItem(String invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    
    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    
    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    
    public Integer getInvoiceType() {
        return invoiceType;
    }

    
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    
    public String getTaxNo() {
        return taxNo;
    }

    
    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public Date getUpdateDate() {
        return updateDate;
    }

    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    
    public String getDelFlag() {
        return delFlag;
    }

    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
