package com.example.log_up;

import java.io.Serializable;

public class Chat implements Serializable {
    private String sender;
    private String receiver;
    private String message;
    private int idProduct;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }


    public String getReceiver() {
        return receiver;
    }



    public String getMessage() {
        return message;
    }

    public int getIdProduct() {
        return idProduct;
    }
}
