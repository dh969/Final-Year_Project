package com.example.babble.Models;

public class MessageModel {
    String uId,message,messageId;
    Long timestamp;


    public MessageModel(String uId,String message,Long timestamp) {
        this.message=message;
        this.timestamp=timestamp;
        this.uId = uId;
    }
    public MessageModel(String uId,String message){
        this.uId=uId;
        this.message=message;
    }

    public MessageModel(){

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
