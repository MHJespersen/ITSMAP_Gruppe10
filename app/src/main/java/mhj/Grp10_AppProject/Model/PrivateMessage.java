package mhj.Grp10_AppProject.Model;

import java.io.Serializable;

public class PrivateMessage implements Serializable{

    private int messageId, recipientId, senderId, itemId;
    private String messageBody, messageDate;
    private Boolean messageRead;

    public PrivateMessage(){
    }

    public PrivateMessage(int messageId, int recipientId, int senderId, int itemId, String messageBody, String messageDate, Boolean messageRead) {
        this.messageId = messageId;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.itemId = itemId;
        this.messageBody = messageBody;
        this.messageDate = messageDate;
        this.messageRead = messageRead;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public Boolean getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Boolean messageRead) {
        this.messageRead = messageRead;
    }

}

