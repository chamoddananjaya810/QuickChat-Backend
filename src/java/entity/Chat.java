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
