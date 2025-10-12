/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author User
 */

@Entity
@Table(name = "user")
public class User extends BaseEntity {

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
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the country_code
     */
    public String getCountry_code() {
        return country_code;
    }

    /**
     * @param country_code the country_code to set
     */
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    /**
     * @return the contact_number
     */
    public String getContact_number() {
        return contact_number;
    }

    /**
     * @param contact_number the contact_number to set
     */
    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    /**
     * @return the last_seen
     */
    public String getLast_seen() {
        return last_seen;
    }

    /**
     * @param last_seen the last_seen to set
     */
    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    /**
     * @return the profile_pic
     */
    public String getProfile_pic() {
        return profile_pic;
    }

    /**
     * @param profile_pic the profile_pic to set
     */
    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name", length = 45, nullable = false)
    private String first_name;

    @Column(name = "last_name", length = 45, nullable = false)
    private String last_name;

    @Column(name = "country_code", length = 45, nullable = false)
    private String country_code;

    @Column(name = "contact_number", length = 10, nullable = false)
    private String contact_number;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen", nullable = false)
    private String last_seen;

    @Column(name = "profile_pic", length = 100, nullable = false)
    private String profile_pic;

}
