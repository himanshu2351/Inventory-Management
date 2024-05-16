package com.inventory.management.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cust_id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;


    public Customer() {

    }


    public int getCust_id() {
        return cust_id;
    }


    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        /*
         * Base64.Encoder encoder = Base64.getEncoder(); String normalString = password;
         * String encodedString = encoder.encodeToString(
         * normalString.getBytes(StandardCharsets.UTF_8) );
         */
        this.password = password;
    }

    @Column(unique = true)
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


}
