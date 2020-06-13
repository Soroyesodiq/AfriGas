package com.hfad.afrigas.Model;

public class Feedbacks {
    private String Date, Id, PhoneNo, Message;


    public Feedbacks()
    {

    }

    public Feedbacks(String Date, String Id, String PhoneNo, String Message) {
        this.Date = Date;
        this.Id = Id;
        this.PhoneNo = PhoneNo;
        this.Message = Message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Id;
    }

    public void setTime(String Id) {
        this.Id = Id;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
