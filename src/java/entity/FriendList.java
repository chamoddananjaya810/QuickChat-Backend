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
@Table(name = "friend_list")
public class FriendList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private User user_id;

    @ManyToOne
     @JoinColumn(name = "friend_id", nullable = false) 
    private User friend_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_id", length = 45)
    private Status status_id = Status.SENT;
}
