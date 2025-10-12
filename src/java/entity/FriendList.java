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
public class FriendList extends BaseEntity{

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
     * @return the user_id
     */
    public User getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the friend_id
     */
    public User getFriend_id() {
        return friend_id;
    }

    /**
     * @param friend_id the friend_id to set
     */
    public void setFriend_id(User friend_id) {
        this.friend_id = friend_id;
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

    public FriendList() {
    }

    public FriendList(int id, User user_id, User friend_id) {
        this.id = id;
        this.user_id = user_id;
        this.friend_id = friend_id;
    }
    
    
    
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
