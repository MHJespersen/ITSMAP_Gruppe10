package mhj.Grp10_AppProject.Model;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class PrivateMessage implements Serializable{

    private String messageBody, messageDate, receiver, sender;
    private Boolean messageRead;

    public PrivateMessage(){
    }

    public PrivateMessage(String receiver, String sender, String messageBody, String messageDate, Boolean messageRead) {
        this.receiver = receiver;
        this.sender = sender;
        this.messageBody = messageBody;
        this.messageDate = messageDate;
        this.messageRead = messageRead;
    }

    public static PrivateMessage fromSnapshot(DocumentSnapshot d) {
        PrivateMessage message = new PrivateMessage(d.get("Receiver").toString(),
                d.get("Sender").toString(),
                d.get("MessageBody").toString(),
                d.get("MessageDate").toString(),
                false);
        return message;
    }

    public String getReceiver(){return receiver;}

    public void setReceiver(String receiver){this.receiver = receiver;}

    public String getSender(){return sender;}

    public void setSender(String sender){this.sender = sender;}

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

