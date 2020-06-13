package com.hfad.afrigas.Model;
//Add token to this class
public class Products
{
    private String orderId, orderTime, orderQuantity, orderHostel, customerName, orderBlock, orderRoom, orderPhoneNo,
    status, bFillImage, aFillImage, token;

    public Products()
    {

    }

    public Products(String orderId, String orderTime, String orderQuantity, String orderHostel, String customerName, String orderBlock, String orderRoom, String orderPhoneNo,
                    String status, String bFillImage, String aFillImage, String token) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderQuantity = orderQuantity;
        this.orderHostel = orderHostel;
        this.customerName = customerName;
        this.orderBlock = orderBlock;
        this.orderRoom = orderRoom;
        this.orderPhoneNo = orderPhoneNo;
        this.status = status;
        this.bFillImage = bFillImage;
        this.aFillImage = aFillImage;
        this.token = token;
    }

    //GETTERS
    public String getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public String getOrderHostel() {
        return orderHostel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderBlock() {
        return orderBlock;
    }

    public String getOrderRoom() {
        return orderRoom;
    }

    public String getOrderPhoneNo() {
        return orderPhoneNo;
    }

    public String getStatus() {
        return status;
    }

    public String getbFillImage() {
        return bFillImage;
    }

    public String getaFillImage() {
        return aFillImage;
    }

    public String getToken() {return token;}


    //SETTERS
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public void setOrderHostel(String orderHostel) {
        this.orderHostel = orderHostel;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderBlock(String orderBlock) {
        this.orderBlock = orderBlock;
    }

    public void setOrderRoom(String orderRoom) {
        this.orderRoom = orderRoom;
    }

    public void setOrderPhoneNo(String orderPhoneNo) {
        this.orderPhoneNo = orderPhoneNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setbFillImage(String bFillImage) {
        this.bFillImage = bFillImage;
    }

    public void setaFillImage(String aFillImage) {
        this.aFillImage = aFillImage;
    }

    public void setToken(String token) {this.token = token; }

}

