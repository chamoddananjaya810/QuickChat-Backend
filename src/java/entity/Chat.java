/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name = "chat")
public class Chat extends BaseEntity{

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the sender_id
     */
    public User getSender_id() {
        return sender_id;
    }

    /**
     * @param sender_id the sender_id to set
     */
    public void setSender_id(User sender_id) {
        this.sender_id = sender_id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the recipient_id
     */
    public User getRecipient_id() {
        return recipient_id;
    }

    /**
     * @param recipient_id the recipient_id to set
     */
    public void setRecipient_id(User recipient_id) {
        this.recipient_id = recipient_id;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the status_id
     */
    public Status getStatus_id() {
        return status_id;
    }

    /**
     * @param status_id the status_id to set
     */
    public void setStatus_id(Status status_id) {
        this.status_id = status_id;
    }
      @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender_id;

    @Column(name = "message", columnDefinition = "LONGTEXT", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient_id;

    @Column(name = "file", columnDefinition = "LONGTEXT", nullable = false)
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_id", length = 45)
    private Status status_id = Status.SENT;
}
